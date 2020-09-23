import time, random
import requests, json
import uuid, hashlib, hmac
import paho.mqtt.client as mqtt

from threading import Thread
from datetime import datetime

DEVICE_NO = '02-0000b5'
PRODUCT_KEY = 'A4MU33FGXY4URY6Z'
CLOUD_PASSWORD =''
SECRET_KEY = 'a234d8152d2f6ef163baa68acbfb5db0'

GATEWAY_URL = 'http://192.168.85.138:8080'
CLOUD_URL = 'http://192.168.83.166:8088/cloud-web-api'

# 扫描URL
url_w_get_dev_list = "/light_w/dev_list"
url_wy_get_dev_list = "/light_wy/dev_list"
url_wrgb_get_dev_list = "/light_wrgb/dev_list"

# 控制URL
url_w_set_dim_level = "/light_w/set_dim_level"
url_wy_set_dim_level = "/light_wy/set_dim_level"
url_wrgb_set_dim_level = "/light_wrgb/set_dim_level"

def get_mac_addr():
    mac = uuid.UUID(int=uuid.getnode()).hex[-12:]
    return ":".join([mac[e:e + 2] for e in range(0, 11, 2)]).upper()

MAC_ADDR = get_mac_addr()

# 获取当前时间戳
CURRENT_TIME_MILLIS = lambda: int(round(time.time() * 1000))

# mqtt客户端
client_id = time.strftime('%Y%m%d%H%M%S', time.localtime(time.time()))
client = mqtt.Client(client_id)

class GatewayDevice(object):
    def __init__(self, addr, visibility, dtype, pan, mac, fw_ver, connect_type, detail):
        self.addr = addr
        self.visibility = visibility
        self.type = dtype
        self.pan = pan
        self.mac = mac
        self.fw_ver = fw_ver
        self.connect_type = connect_type
        self.detail = detail

    def convert_to_dict(self):
        return self.__dict__

def json2GatewayDevice(json_str):
    return GatewayDevice(json_str['addr'], json_str['visibility'], json_str['type'], json_str['pan'], 
                         json_str['mac'], json_str['fw_ver'], json_str['connect_type'], None)

class CloudDevice(object):
    def __init__(self):
        self.deviceCode = ''
        self.deviceMac = ''
        self.state = ''
        self.deviceName = ''
        self.productKey = ''
        self.productName = ''
        self.deviceOriginalType = ''
        self.categoryName = ''
        self.brandName = ''
        self.parent = 0
        self.detail = ''
        self.switchStatus = ''
        self.detailState = ''

    def convert_to_dict(self):
        return self.__dict__

def log(msg):
    print(str(datetime.now())+': '+msg)

def get_message_id():
    return int(1e5 * random.random())

def get_gateway_info():
    reqUrl = GATEWAY_URL + '/info'
    resp = requests.post(reqUrl, verify=False)
    data = json.loads(resp.text)
    if (data['success'] == 'true'):
        return data['object']
    return None

def hmac_md5(ekey, to_enc):
    return hmac.new(str.encode(ekey), str.encode(to_enc), hashlib.md5).hexdigest().upper()

def get_topic(product_key, device_no):
    return '/' + str(product_key) + '/' + str(device_no)

def get_pub_api(product_key, device_no):
    return '/topic/' + str(product_key) + '/' + str(device_no) + '/pub' + '?level=1'

def get_device_list():
    req_url = GATEWAY_URL + '/get_dev_list'
    resp = requests.post(req_url, verify=False)
    data = json.loads(resp.text)
    if data['success'] == 'true':
        return data['objects']

def get_device_detail(device_dict, url):
    req_url = GATEWAY_URL + url
    resp = requests.post(req_url, verify=False)
    data = json.loads(resp.text)
    if (data['success'] == 'true'):
        result = data['objects']
        for json_str in result:
            try:
                device = json.loads(json.dumps(json_str))
                if 'w' in device:
                    device['level'] = device['w']
                    device['levelW'] = device['w']
                device_dict[json_str['mac']].detail = json.dumps(device)
            except:
                log('json type error')

# 搜索网关下所有设备
def get_all_device():
    device_dict = dict()
    device_list = get_device_list()
    if device_list:
        for json_str in device_list:
            try:
                device = json.loads(json.dumps(json_str), object_hook=json2GatewayDevice)
                device_dict[json_str['mac']] = device
            except AttributeError:
                log('json attribute error')
            except TypeError:
                log('json type error')
    # 映射转换
    get_device_detail(device_dict, url_wy_get_dev_list)
    get_device_detail(device_dict, url_wrgb_get_dev_list)
    get_device_detail(device_dict, url_w_get_dev_list)
    
    # 去除unknow设备
    for key in device_dict.keys():
        if device_dict[key].detail is None:
            del device_dict[key]
    return device_dict

def get_cloud_device_list(device_dict):
    device_list = list()
    for baseDevice in  device_dict.items():
        device = CloudDevice()
        device.deviceName = baseDevice.mac
        device.deviceOriginalType = baseDevice.type
        device.deviceMac = baseDevice.addr
        device.brandName = 'homw'
        device.switchStatus = 'on'
        device.state = 'online'
        detail = dict()
        detail['addr'] = baseDevice.addr
        detail['connect_type'] = 'HOMwNet'
        detail['detail'] = baseDevice.detail
        device.detail = detail
        device_list.append(device.convert_to_dict())
    return device_list

# 上报网关下子设备
def upload_device_list(token):
    log('开始上传数据到云平台.....')
    # 网关设备
    device_dict = get_all_device()
    # 转换为平台设备
    device_list = get_cloud_device_list(device_dict)
    
    up_data = dict()
    up_data['messageId'] = get_message_id()
    up_data['msgType'] = 'notify'
    up_data['services'] = json.dumps(device_list)
    
    header = {
        'Content-Type': 'application/json',
        'password': token
    }
    req_url = CLOUD_URL + get_pub_api(PRODUCT_KEY, DEVICE_NO)
    requests.post(req_url, headers=header, json=up_data, verify=False)

def cloud_auth():
    # gateway = get_gateway_info()
    to_enc = 'clientId' + MAC_ADDR + 'deviceName' + DEVICE_NO + 'productKey' + PRODUCT_KEY + 'timestamp' + \
             str(CURRENT_TIME_MILLIS())
    # 签名
    sign = hmac_md5(SECRET_KEY, to_enc)
    # 请求参数
    params = 'productKey=' + PRODUCT_KEY + '&sign=' + sign + '&signmethod=' + 'hmacmd5' + '&timestamp=' + str(CURRENT_TIME_MILLIS()) \
             +'&version=default&clientId=' + MAC_ADDR + '&resources=mqtt&deviceName=' + DEVICE_NO
    reqUrl = CLOUD_URL + '/auth?' + params
    # 请求头
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
    resp = requests.post(reqUrl, headers=headers, verify=False)
    return  json.loads(resp.text)

# mqtt连接回调
def on_connect(client, user_data, flags, code):
    log("mqtt连接信息状态码：" + str(code))
    topic = get_topic(PRODUCT_KEY, DEVICE_NO)
    client.subscribe(topic, qos=1)

# mqtt消息到达回调
def on_message(client, user_data, msg):
    try:
        recv_msg = str(msg.payload.decode("utf-8-sig"))
        json_msg = json.loads(recv_msg)
        msg_type = json_msg['msgType']
        if msg_type == 'set':
            set_command(json.loads(json_msg['services']))
    except ValueError:
        log('recv msg error')

def start_gateway_loop(auth_info):
    mqtt_dict = auth_info['result']['resources']['mqtt']
    user_name = mqtt_dict['userName']
    pass_word = mqtt_dict['password']
    host = mqtt_dict['host']
    port = mqtt_dict['port']

    # 绑定回调
    client.on_connect = on_connect
    client.on_message = on_message
    client.username_pw_set(user_name, pass_word)
    
    # 建立连接
    back = client.connect(host, port, 60)
    log('mqtt客户端连接返回（0表示连接成功）：' + str(back))
    client.loop_forever()

def set_command(msg):
    command = json.loads(msg['command'])
    dtype = msg['orgType']
    addr = msg['seqNo']
    if dtype == 'light_w':
        dimUrl = GATEWAY_URL + url_w_set_dim_level + '?' + 'dev=' + addr + '&' + 'level=' + str(command[0]['ptValue'])
        log(dimUrl)
        resp = requests.post(dimUrl, verify=False)
        # 此处可添加睡眠1秒 再次请求网关，将设备状态传给平台
        log(resp.text)
        
    if dtype == 'light_wy':
        dimUrl = GATEWAY_URL + url_wy_set_dim_level + '?' + 'dev=' + addr + '&' + 'levelW=' + str(command[0]['ptValue']) \
                    + '&' + 'levelY=' + str(command[1]['ptValue'])
        log(dimUrl)
        resp = requests.post(dimUrl, verify=False)
        log(resp.text)

    if dtype == 'light_wrgb':
        pass

def get_cloud_auth():
    auth_info = cloud_auth()
    log(auth_info)
    if (auth_info['code'] != '200'):
        time.sleep(5)
        log('连接平台认证识别,进行重试....!')
        return get_cloud_auth()
    else:
        return auth_info

def upload_task():
    while True:
        upload_device_list(CLOUD_PASSWORD)
        time.sleep(300)

if __name__ == '__main__':
    # 云平台认证
    auth_info = get_cloud_auth()
    if (auth_info['code'] == '200'):
        log("认证成功")
        mqtt_dict = auth_info['result']['resources']['mqtt']
        CLOUD_PASSWORD = mqtt_dict['password']
    
    # 定时上报
    upload_thread = Thread(target=upload_task)
    upload_thread.start()
    
    # 监听指令
    start_gateway_loop(auth_info)
    upload_thread.join()
