#!/usr/bin/python
# -*- coding:utf-8 -*-
# author : seed
# date : 20180327
"""
下载历史数据进行python解析入库
"""
import urllib2
import os
import time
import json
import sys
import MySQLdb
import ast
reload(sys)
sys.setdefaultencoding('utf-8')
#stock_code = "sh600637"
#stock_code = "sz000002"
stock_code = sys.argv[1]
# 获取个股历史交易数据
ifeng_url="http://api.finance.ifeng.com/akdaily/?code=%s&type=last" %(stock_code)
def get_stock_history_data(url):
    myPage = urllib2.urlopen(url).read()
    myjson = json.dumps(myPage)
    records = myjson[0]
    d = ast.literal_eval(myPage)
    print type(d)
    print("length of records : %d" %(len(d['record'])))
#    for line in d['record']:
#        print("line[0] : %s" %(line[0]))
#        print("line[1] : %s" %(line[1]))
#        print("line[2] : %s" %(line[2]))
#        print("line[3] : %s" %(line[3]))
#        print("line[4] : %s" %(line[4]))
#        print("line[5] : %s" %(line[5]))
#        print("line[6] : %s" %(line[6]))
#        print("line[7] : %s" %(line[7]))
#        print("line[8] : %s" %(line[8]))
#        print("line[9] : %s" %(line[9]))
#        print("line[10] : %s" %(line[10]))
#        print("line[11] : %s" %(line[11]))
#        print("line[12] : %s" %(line[12]))
#        print("line[13] : %s" %(line[13]))
#        print("line[14] : %s" %(line[14]))
    return d['record']

def insert_into_mysql_tbl(lists):
    print(" =====>>>>> %d " %(len(lists))) 
    datas = []
    for line in lists:
#        print("line ===>>> %s " %(line))
#        data=(str(stock_code),str(line[0]),float(line[1]),float(line[2]))
#        print("-------------------------------------------------")
#        print("data[0] ===>>> %s" %(data[0]))
#        print("data[1] ===>>> %s" %(data[1]))
#        print("data[2] ===>>> %.3f" %(float(data[2])))
#        print("data[3] ===>>> %.3f" %(float(data[3])))
        data=(stock_code,line[0],float(line[1]),float(line[2]),float(line[3]),float(line[4]),float(line[5]),float(line[6]),float(line[7]),float(line[8]),float(line[9]),float(line[10]),float(line[11].replace(',','')),float(line[12].replace(',','')),float(line[13].replace(',','')),float(line[14]))
        datas.append(data)
        
    db = MySQLdb.connect("hadoop03","root","hadoop","test" )    
    cursor = db.cursor()
#    sql_str = "insert into test.stock_history_data (stock_code,n_date,open,high) values (%s,%s,%.3f,%.3f)" 
    sql_str = "insert  into test.stock_history_data (stock_code,n_date,open,high,close,low,volume,chg,p_chg,ma5,ma10,ma20,avgma5,avgma10,avgma20,turnover) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)" 
    del_sql = "delete from  test.stock_history_data where stock_code='%s'" %(stock_code)
    print("del_sql --->>> %s " %(del_sql)) 
    cursor.execute(del_sql)
    cursor.executemany(sql_str,datas)
    db.commit()#没有提交的话，无法完成插入
    db.close()


if __name__ == "__main__":
    records = get_stock_history_data(ifeng_url)
    insert_into_mysql_tbl(records)

