#!/usr/bin/env python
#-*-coding:utf-8-*-

'''
@summary: RabbitMQ Monitor
@author: Hom
@version: 1.0
@since: 2020-07-16
@note: linux backend run: nohup python rabbitmq_monitor.py > /dev/null 2>&1 &
'''

import sys
# validate python version
if sys.version_info[0] < 2 or (sys.version_info[2] == 2 and sys.version_info[1] < 6):
    sys.stderr.write('This monitor requires at least python2.6')
    sys.exit(1)
    
import time
import logging
import logging.handlers

# log settings
log = logging.getLogger('monitor_logger')
log.setLevel(logging.DEBUG)

stream_handler = logging.StreamHandler(sys.stdout)
stream_handler.setLevel(logging.DEBUG)
stream_handler.setFormatter(logging.Formatter(fmt='%(asctime)s [%(levelname)s] [%(threadName)s] - %(message)s'))

file_handler = logging.handlers.TimedRotatingFileHandler('mq_monitor.log', when='midnight', backupCount=7)
file_handler.setLevel(logging.INFO)
file_handler.setFormatter(logging.Formatter(fmt='%(asctime)s [%(levelname)s] [%(threadName)s] %(module)s(:%(lineno)d) - %(message)s'))

log.addHandler(stream_handler)
log.addHandler(file_handler)

from base64 import b64encode
from socket import error as socket_error

from json import loads as load_json
from re import search
from threading import Thread

from smtplib import SMTP, SMTPException
from email.mime.text import MIMEText
from email.header import Header

# python2 compatible
if sys.version_info[0] == 2:
    reload(sys)
    sys.setdefaultencoding('utf-8')
    
    import httplib
    import urlparse
    from urllib import quote_plus
    def b64(s):
        return b64encode(s)
else:
    import http.client as httplib
    import urllib.parse as urlparse
    from urllib.parse import quote_plus
    def b64(s):
        return b64encode(s.encode('utf-8')).decode('utf-8')

# const
MQ_HOST = 'mq.homw.com'
MQ_PORT = 15672
MQ_USER = 'admin'
MQ_PASSWD = '123456'

MQ_VHOST = quote_plus('/homw_vhost')
MQ_QUERY = 'columns=name,messages'
MQ_QUEUE_PATH = '/api/queues/' + MQ_VHOST + '?' + MQ_QUERY

SMTP_HOST = 'smtp.qq.com'
SMTP_PORT = 25

MAIL_SENDER = 'admin@qq.com'
MAIL_PASSWD = '123456'
MAIL_RECVERS = ['aaa@qq.com', 'bbb@qq.com']
MAIL_SUBJECT = 'RabbitMQ Monitor Alarm'

TIMEOUT = 30
blocked_timer = {}

def http(path, method='GET', header={}, body=None):
    '''
    @summary: send http request
    @param path: request path
    @param method: default is 'GET'
    @param header: request header dict
    @param body: request body, json format
    @return: response data
    '''
    conn = httplib.HTTPConnection(MQ_HOST, MQ_PORT)
    if body is not None:
        header['Content-Type'] = 'application/json'
    try:
        conn.request('GET', MQ_QUEUE_PATH, body, header)
    except socket_error as e:
        exception(e, 'could not connect: {0}\n'.format(e))
        
    resp = conn.getresponse()
    # redirect
    if resp.status == 301:
        url = urlparse.urlparse(resp.getheader('location'))
        log.info('redirect url: %s', url)
        return http(url.path + '?' + url.query, method, header, body)
    if resp.status != 200:
        raise Exception('received %d %s for path %s\n%s' 
                        % (resp.status, resp.reason, path, resp.read()))
    return resp.read().decode('utf-8')

def exception(err, msg=None):
    '''
    @summary: log exception
    @param err: exception object
    @param msg: exception description
    '''
    log.error(msg, exc_info=True)

def email(content, format):
    '''
    @summary: send email to manager
    @param content: email content
    @param format: html or plain
    '''
    try:
        msg = MIMEText(content, format)
        msg['From'] = Header('{0} <{1}>'.format(search('(\w+)@', MAIL_SENDER).group(1), MAIL_SENDER))
        msg['To'] = Header(';'.join(['{0} <{1}>'.format(search('(\w+)@', r).group(1), r) for r in MAIL_RECVERS]))
        msg['Subject'] = Header(MAIL_SUBJECT)
        
        smtp = SMTP()
        smtp.connect(SMTP_HOST, SMTP_PORT)
        smtp.login(MAIL_SENDER, MAIL_PASSWD)
        smtp.sendmail(MAIL_SENDER, MAIL_RECVERS, msg.as_string())
        log.debug('send email to %s success', msg['To'])
    except SMTPException as e:
        exception(e, 'send email failed, %s' % str(e))
    finally:
        if smtp is not None:
            smtp.quit()

def he_format(item, qname, cnt):
    '''
    @summary: html email format
    @param item: blocked info
    @param qname: queue name
    @param cnt: current message count
    @return: html format
    '''
    return '''[RabbitMQ Monitor] A queue message blocking issue has been found, it details are as follows:<br><br>
Space_No: <span style="color: red; font-weight: bold;">{0}</span>&nbsp;&nbsp;&nbsp;&nbsp;
Queue_Name: <span style="color: red; font-weight: bold;">{1}</span>&nbsp;&nbsp;&nbsp;&nbsp;
Blocked_Message_Count: <span style="color: red; font-weight: bold;">{2}</span>&nbsp;&nbsp;&nbsp;&nbsp;
Discovery_Time: <span style="color: red; font-weight: bold;">{3}</span>'''.format(str(item['space']), qname, str(cnt), time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(item['time'])))

def query():
    '''
    @summary: query queue message status
    @return: queue list, json object
    '''
    auth = MQ_USER + ':' + MQ_PASSWD
    header = {'Authorization': 'Basic ' + b64(auth)}
    ret = http(MQ_QUEUE_PATH, header=header)
    return load_json(ret)

def analysis(queue_list):
    '''
    @summary: analyze blocked situation, and notify manager
    @param queue_list: queue status list
    '''
    for queue in queue_list:
        cnt = queue['messages']
        qname = queue['name']
        if qname in blocked_timer:
            item = blocked_timer[qname]
            # while raise
            if cnt > 0 and cnt >= item['cnt']:
                if time.time() - item['time'] > TIMEOUT and not item['notified']:
                    log.info('discovery a queue message blocking issue:')
                    log.info('item: %s\nqname: %s\ncnt: %d' % (str(item), qname, cnt))
                    # send email async
                    t = Thread(target=email(he_format(item, qname, cnt), 'html'), 
                               name='Thread-Email-' + item['space'])
                    t.start()
                    item['notified'] = True
                # once again
                if item['time'] is None:
                    item['time'] = time.time()
                    
            # while down
            if cnt < item['cnt']:
                item['time'] = None
                item['notified'] = False
            item['cnt'] = cnt
        else:
            if cnt > 0:
                blocked_timer[qname] = {'space': search('\d{3}', qname).group(), 
                                       'cnt': cnt, 'time': time.time(), 'notified': False}
    
def main():
    '''
    @summary: main loop
    '''
    while True:
        try:
            # query
            queue_list = query()
            log.debug('queue_list: %s', str(queue_list))
            if queue_list is not None:
                # analysis
                analysis(queue_list)
                log.debug('blocked_timer: %s', str(blocked_timer))
            # wait
            time.sleep(5)
        except Exception as e:
            exception(e, 'rabbitmq monitor loop occurs an exception: %s' % str(e))
    
if __name__ == '__main__':
    main()
