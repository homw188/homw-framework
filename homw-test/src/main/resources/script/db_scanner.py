'''
Created on 2020年10月10日

@author: Hom
'''
import pymysql
from time import time

db_config = {
    'host': '127.0.0.1', 
    'port': 3306, 
    'user': 'root', 
    'password': 'pass', 
    'db': 'test'
}

KEYWORD_NUM = 346
KEYWORD_STR = '\"346\"'
TABLE_ID_COLUMN = 'id'

result_dict = {}
result_file = 'db_scanner_result.txt'

def get_conn(**kwargs):
    conn = pymysql.connect(**kwargs)
    cursor = conn.cursor(pymysql.cursors.DictCursor)
    return conn, cursor

def close_conn(conn, cursor):
    if cursor:cursor.close()
    if conn:conn.close()

def add_result(tbl, tid, col):
    if tbl in result_dict:
        result_dict[tbl].append(str(tid) + ':' + col)
    else:
        result_dict[tbl] = [str(tid) + ':' + col]
        
def save_result(file):
    with open(file, 'w+') as fd:
        for tbl, ids in result_dict.items():
            fd.write(tbl + '=> ' + ', '.join(ids) + '\n\r')

def filtered_table(tbl):
    if '_log' in tbl or '_bak' in tbl or '.bak' in tbl or 'tmp_' in tbl:
        return True
    return False

def filtered_colmun(col):
    if col == TABLE_ID_COLUMN:
        return True
    return False

def match(val):
    if isinstance(val, int) and val == KEYWORD_NUM:
        return True
    elif isinstance(val, str) and KEYWORD_STR in val:
        return True
    return False

def scan(**kwargs):
    conn, cursor = get_conn(**kwargs)
    scaned_cnt = 0
    try:
        cnt = cursor.execute('show tables')
        print('find %d tables.' % cnt)
        for item in cursor.fetchall():
            for tbl in item.values():
                if filtered_table(tbl):
                    continue
                print('scan [%s]...' % tbl)
                try:
                    cursor.execute('select * from %s' % tbl)
                    for record in cursor.fetchall():
                        for col, val in record.items():
                            if filtered_colmun(col):
                                continue
                            if match(val):
                                add_result(tbl, record[TABLE_ID_COLUMN], col)
                except Exception as e:
                    print('occurred error: %s' % str(e))
                    continue
                scaned_cnt += 1
    except Exception as e:
        print('occurred error: %s' % str(e))
    finally:
        close_conn(conn, cursor)
    return scaned_cnt

if __name__ == '__main__':
    start = time()
    print('start scan task...')
    
    cnt = scan(**db_config)
    print('scan finished, total %d tables.' % cnt)
    
    save_result(result_file)
    print('save result finished.')
    
    print('running time: %s seconds.' % int(time() - start))