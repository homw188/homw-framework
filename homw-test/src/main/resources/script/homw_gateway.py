#-*-coding:utf-8-*-
import socket,uuid
import hashlib
import hmac
import time,datetime,random,threading
import requests
import json
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish

'''
版本:python2.7
'''
# linux 后台运行 nohup python homw_gateway.py > my.log &
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

#获取本机mac地址
def get_mac_address():
    mac=uuid.UUID(int = uuid.getnode()).hex[-12:]
    return ":".join([mac[e:e+2] for e in range(0,11,2)]).upper()

HOMW_MAC = get_mac_address()

PRODUCT_KEY = 'HOMWA4MU33FGXY4URY6Z'
DEVICE_NO = 'homw02-0000b5'
#设备网关地址
HWGD_URL = 'http://192.168.85.138:8080'
#云平台地址
HWCLOUD_URL = 'http://192.168.83.166:8088/homw-cloud-web-api'
HOMW_SECRET = 'a234d8152d2f6ef163baa68acbfb5db0'
#云平台认证后的密码
CLOUD_PASSWORD ='';

#扫描设备URL
url_wrgb_get_dev_list = "/light_wrgb.get_dev_list"
url_wy_get_dev_list = "/light_wy.get_dev_list"
url_w_get_dev_list = "/lighting.get_dev_list"

#控制URL
light_wrgb_set_dim_level = "/light_wrgb.set_dim_level"
light_wy_set_dim_level = "/light_wy.set_dim_level"
light_w_set_dim_level = "/lighting.set_dim_level"

#获取当前时间戳
CURRENT_MILLI_TIME = lambda: int(round(time.time() * 1000))

#mqtt客户端
client_id = time.strftime('%Y%m%d%H%M%S', time.localtime(time.time()))
client = mqtt.Client(client_id)

class HomwBaseDevice(object):
    def __init__(self,addr,visibility,type,pan,mac,fw_ver,connect_type,detail):
        self.addr = addr
        self.visibility = visibility
        self.type = type
        self.pan = pan
        self.mac = mac
        self.fw_ver = fw_ver
        self.connect_type = connect_type
        self.detail = detail

    def convert_to_dict(self):
       return self.__dict__

def json2HomwBaseDevice(d):
     return HomwBaseDevice(d['addr'],d['visibility'],d['type'],d['pan'],d['mac'],d['fw_ver'],d['connect_type'],None)

class  HomwDetailDevice(object):
    def __init__(self,dev,mac,pan,lt,rr,rt,visibility,rtc,levelW,levelR,levelG,levelB,levelY,dev_status,level,w,enable):
        self.dev = dev
        self.mac = mac
        self.pan = pan
        self.lt = lt
        self.rr = rr
        self.rt = rt
        self.visibility = visibility
        self.rtc = rtc
        self.levelW = levelW
        self.levelR = levelR
        self.levelG = levelG
        self.levelB = levelB
        self.levelY = levelY
        self.dev_status = dev_status
        self.level = level
        self.w = w
        self.enable = enable

def json2HomwDetailDevice(d):
    return HomwDetailDevice(d['dev'],d['mac'],d['pan'],d['lt'],d['rr'],d['rt'],d['visibility'],d['rtc'],
                                d['levelW'],d['levelR'],d['levelG'],d['levelB'],d['levelY'],d['dev_status'],d['level'],
                                d['w'],d['enable'])


class CloudDevice(object):
      def __init__(self):
          self.deviceCode = ''
          self.deviceMac = ''
          self.state = ''
          self.deviceName = ''
          self.productKey = ''
          self.productName = ''
          self.deviceOriginalType=''
          self.categoryName = ''
          self.brandName = ''
          self.parent = 0
          self.detail = ''
          self.switchStatus = ''
          self.detailState = ''

      def convert_to_dict(self):
          return self.__dict__

#日志打印
def log(msg):
     print(str(datetime.datetime.now())+' :'+msg)

def getMessageId():
    return long(1e5 * random.random())

#获取网关信息
def get_gatewayinfo():
    reqUrl = HWGD_URL + '/info'
    resp = requests.post(reqUrl, verify=False)
    #print resp.text
    data = json.loads(resp.text)
    if (data['success'] == 'true'):
        return data['object']
    return None

'''
hmac_md5 加密
'''
def hmac_md5(ekey,to_enc):
    enc_res = hmac.new(ekey, to_enc, hashlib.md5).hexdigest()
    return enc_res.upper()

#设备列表
def get_dev_list():
    reqUrl = HWGD_URL + '/get_dev_list'
    resp = requests.post(reqUrl, verify=False)
    data = json.loads(resp.text)
    if (data['success'] == 'true'):
        return data['objects']
    return None

# 特殊转换
def getDevicesByCategory(deviceDict,url):
    reqUrl = HWGD_URL + url
    resp = requests.post(reqUrl, verify=False)
    data = json.loads(resp.text)
    if (data['success'] == 'true'):
        result = data['objects']
        for json_str in result:
            try:
                #print json.dumps(json_str)
                d = json.loads(json.dumps(json_str))
                if 'w' in d:
                    d['level'] = d['w']
                    d['levelW'] = d['w']
                deviceDict[json_str['mac']].detail = json.dumps(d)
            except TypeError,e:
                print 'json type error'


#搜索网关下所有设备
def get_all_deviceList():
    deviceDict = dict()
    deviceBaseInfo = get_dev_list()
    if deviceBaseInfo != None:
       for json_str in deviceBaseInfo:
           try:
             device = json.loads(json.dumps(json_str), object_hook=json2HomwBaseDevice)
             deviceDict[json_str['mac']]  = device
           except AttributeError,e:
               print 'json attribute error'
           except TypeError,e:
               print 'json type error'
    # 映射转换
    getDevicesByCategory(deviceDict,url_wy_get_dev_list)
    getDevicesByCategory(deviceDict,url_wrgb_get_dev_list)
    getDevicesByCategory(deviceDict,url_w_get_dev_list)
    
    #迭代 去除unknow设备
    keys = deviceDict.keys()
    for key in keys:
        #print deviceDict[key].detail
        if deviceDict[key].detail is None:
            del deviceDict[key]
    return deviceDict

# create upload homw-cloud devices data
def createDevice(deviceDict):
    devices = list()
    for key in  deviceDict:
        homwBaseDevice = deviceDict[key]
        device = CloudDevice()
        #print homwBaseDevice.addr
        device.deviceName = homwBaseDevice.mac
        device.deviceOriginalType = homwBaseDevice.type
        device.deviceMac = homwBaseDevice.addr
        device.brandName = 'homw'
        device.switchStatus = 'on'
        device.state = 'online'
        dt = dict()
        dt['addr'] = homwBaseDevice.addr
        dt['connect_type'] = 'HOMwNet'
        dt['detail'] = homwBaseDevice.detail
        device.detail = dt
        devices.append(device.convert_to_dict())
    return devices

# 上报网关下子设备
def upload_deviceList(token):
    print('开始上传数据到云平台.....')
    # 请求头
    headers = {
        'Content-Type': 'application/json',
        'password':token
    }
    deviceList = get_all_deviceList()
    devices = createDevice(deviceList)
    #print json.dumps(devices)
    updata = dict()
    updata['messageId'] = getMessageId()
    updata['msgType'] = 'notify'
    updata['services'] = json.dumps(devices)
    reqUrl = HWCLOUD_URL + get_pubApi(PRODUCT_KEY,DEVICE_NO)
    resp = requests.post(reqUrl, headers=headers, json=updata, verify=False)
    print resp.text

'''
调用平台接口进行授权认证
'''
def auth():
    gateway = get_gatewayinfo()
    to_enc = 'clientId' + HOMW_MAC + 'deviceName' + DEVICE_NO + 'productKey' + PRODUCT_KEY + 'timestamp' + \
             str(CURRENT_MILLI_TIME())
    # 签名
    sign = hmac_md5(HOMW_SECRET, to_enc)
    # 请求参数
    params = 'productKey=' + PRODUCT_KEY + '&sign=' + sign + '&signmethod=' + 'hmacmd5' + '&timestamp=' + str(CURRENT_MILLI_TIME()) \
             + '&version=default&clientId=' + HOMW_MAC + '&resources=mqtt&deviceName=' + DEVICE_NO
    reqUrl = HWCLOUD_URL + '/auth?' + params
    # 请求头
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
    resp = requests.post(reqUrl, headers=headers, verify=False)
    return  json.loads(resp.text)

#mqtt 连接回调
def on_connect(client,userdata,flags,rc):
    log("消息连接信息:homw gateway.............Connected with result code" + str(rc))
    topic = get_topic(PRODUCT_KEY,DEVICE_NO)
    print topic
    client.subscribe('/'+PRODUCT_KEY+'/'+DEVICE_NO,qos=1)

#消息到达
def on_message(client,userdata,msg):
    try:
        receiveMsg = str(msg.payload.decode("utf-8-sig"))
        print receiveMsg
        jsonMsg = json.loads(receiveMsg)
        # print(type(jsonMsg))
        msgType = jsonMsg['msgType']
        print msgType
        if msgType == 'set':
            # print jsonMsg['services']
            set_command(json.loads(jsonMsg['services']))
    except ValueError,e:
        print('msg error')

#获取topic
def get_topic(product_key,device_no):
    return '/'+str(product_key)+'/'+str(device_no)

#获取上传接口api
def get_pubApi(product_key,device_no):
    return '/topic/' + str(product_key) + '/' + str(device_no)+'/pub'+'?level=1'


#云mqtt客户端
def cloud_mqtt_client():
    #认证信息
    auth_info = auth()
    print(auth_info)
    if (auth_info['code'] != '200'):
        time.sleep(5)
        print('连接平台认证识别,进行重试....!')
        return cloud_mqtt_client();
    else:
        return auth_info

def gateway_client_run(auth_info):
    # 认证返回的信息
    mqtt_dict = auth_info['result']['resources']['mqtt']
    user_name = mqtt_dict['userName']
    pass_word = mqtt_dict['password']
    host = mqtt_dict['host']
    port = mqtt_dict['port']

    client.on_connect = on_connect
    client.on_message = on_message
    client.username_pw_set(user_name, pass_word)
    back = client.connect(host, port, 60)
    log('0表示连接成功=' + str(back))
    time.sleep(1)
    client.loop_forever()

#云端上报数据
def up_data(token):
    pass

def set_command(serviceMsg):
    command = json.loads(serviceMsg['command'])
    #print command[1]['ptValue']
    dtype = serviceMsg['orgType']
    #addr = serviceMsg['seqNo'].split('-')[1] #设备控制地址
    addr = serviceMsg['seqNo']

    if dtype == 'light_w': # 单色灯
       dimUrl = HWGD_URL+light_w_set_dim_level+'?'+'dev='+addr+'&'+'level='+str(command[0]['ptValue'])
       print dimUrl
       resp = requests.post(dimUrl, verify=False)
       #此处可添加睡眠1秒 再次请求网关，将设备状态传给平台
       print resp.text
    if dtype == 'light_wy':
        dimUrl = HWGD_URL + light_wy_set_dim_level + '?' + 'dev=' + addr + '&' + 'levelW=' + str(command[0]['ptValue'])+'&'+'levelY='+str(command[1]['ptValue'])
        print dimUrl
        resp = requests.post(dimUrl, verify=False)
        print resp.text

    if dtype == 'light_wrgb':
        pass

#定时上报信息到平台
def upload_task():
    while True:
        upload_deviceList(CLOUD_PASSWORD)
        time.sleep(300)

if __name__ == '__main__':
    auth_info = cloud_mqtt_client()
    if (auth_info['code'] == '200'):
        log("认证成功")
        mqtt_dict = auth_info['result']['resources']['mqtt']
        CLOUD_PASSWORD = mqtt_dict['password']

    #每5分钟向平台上报一次数据
    t = threading.Thread(target=upload_task)
    t.start()
    
    #启动mqtt客户端监听指令
    gateway_client_run(auth_info)
    t.join()