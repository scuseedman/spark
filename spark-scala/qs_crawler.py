#!/usr/bin/python
# -*- coding:utf-8 -*-
import time
import datetime
import string
import urllib2
import re
import MySQLdb
import sys
from bs4 import BeautifulSoup
import urllib
reload(sys)
sys.setdefaultencoding('utf-8')

# 该URI不能获取到正确的数据
#uri = "http://app1.sfda.gov.cn/datasearch/face3/content.jsp?tableId=122&tableName=TABLE122&tableView=%E6%89%A7%E4%B8%9A%E8%8D%AF%E5%B8%88%E6%B3%A8%E5%86%8C%E4%BA%BA%E5%91%98&Id="
uri = "http://appcfda.drugwebcn.com/datasearch/face2/content.jsp?tableId=91&tableName=TABLE91&tableView=%E9%A3%9F%E5%93%81%E7%94%9F%E4%BA%A7%E8%AE%B8%E5%8F%AF%E8%8E%B7%E8%AF%81%E4%BC%81%E4%B8%9A(QS)&Id="
send_headers = {
'User-Agent':'Mozilla/5.0 (Windows NT 6.2; rv:16.0) Gecko/20100101 Firefox/16.0',
'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
'Connection':'keep-alive'
}

def get_qs_credentials(url):
    page = urllib2.urlopen(urllib2.Request(url,headers=send_headers)).read().decode("utf-8")
#    print(page)
    soup = BeautifulSoup(page,"html.parser")  
    table = soup.select("table")[0]
#    print(table)
    trs = table.select("tr")
    data = []
    print(len(trs))
    if(len(trs)>=10):
        data.append(trs[1].select("td")[1].get_text())
        data.append(trs[2].select("td")[1].get_text())
        data.append(trs[3].select("td")[1].get_text())
        data.append(trs[4].select("td")[1].get_text())
        data.append(trs[5].select("td")[1].get_text())
        data.append(trs[6].select("td")[1].get_text())
        data.append(trs[7].select("td")[1].get_text())
        data.append(trs[8].select("td")[1].get_text())
        data.append(trs[9].select("td")[1].get_text())
        return data

"""
DROP TABLE IF EXISTS qs_credentials;
CREATE TABLE qs_credentials (
  qs_num varchar(40) DEFAULT NULL COMMENT 'qs编号',
  comp_name varchar(200) DEFAULT NULL COMMENT '公司名称',
  prod_name varchar(200) DEFAULT NULL COMMENT '产品名称',
  home_addr varchar(200) DEFAULT NULL COMMENT '住所',
  prod_addr varchar(200) DEFAULT NULL COMMENT '生产地址',
  test_type varchar(90) DEFAULT NULL COMMENT '检验方式',
  allow_date varchar(30) DEFAULT NULL COMMENT '发证日期',
  validity_date varchar(30) DEFAULT NULL COMMENT '有效期',
  allow_unit varchar(200) DEFAULT NULL COMMENT '发证单位',
  tb_id int(11) DEFAULT NULL COMMENT '表中id',
  c_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY qs_num (qs_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
"""

if __name__ == "__main__":
    print("------------------------  start -------------------------")
    f = open("/data/study/qs_credentials.txt","w+")
    sql_str = ""
    datas = []
    sql_str = "insert ignore into test.qs_credentials (qs_num,comp_name,prod_name,home_addr,prod_addr,test_type,allow_date,validity_date,allow_unit,tb_id) values (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
    for x in range(3971,3972):
        s = (uri,str(x))
        url = "".join(s)
        arr = get_qs_credentials(url)
        for y in arr:
            print(y)
        if(len(arr)==9):
            arr.append(x)
            datas.append(arr)
        if(len(datas) % 1 == 0):
            db = MySQLdb.connect(host="hadoop03",user="root",passwd="hadoop",db="test",charset="utf8" )
            cursor = db.cursor()
            cursor.executemany(sql_str,datas)
            db.commit()#没有提交的话，无法完成插入
            print("--------------------- insert once ----------------------------")
            db.close()
            datas = []
        
