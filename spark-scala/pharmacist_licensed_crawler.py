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
uri = "http://appcfda.drugwebcn.com/datasearch/face3/content.jsp?tableId=122&tableName=TABLE122&tableView=%E6%89%A7%E4%B8%9A%E8%8D%AF%E5%B8%88%E6%B3%A8%E5%86%8C%E4%BA%BA%E5%91%98&Id="
send_headers = {
 'User-Agent':'Mozilla/5.0 (Windows NT 6.2; rv:16.0) Gecko/20100101 Firefox/16.0',
 'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
 'Connection':'keep-alive'
    }

#sql_str = "insert ignore into pharmacist_licensed (name,regist_num,license_area,license_type,license_range,license_company,validity_date) values (%s,%s,%s,%s,%s,%s,%s)"
def get_pharmacist_info(url):
    myPage = urllib2.urlopen(urllib2.Request(url,headers=send_headers)).read().decode("utf-8")
    #print(myPage)
    soup = BeautifulSoup(myPage,"html.parser")
    table = soup.select("table")[0]
    #print(table)
    arr = []
    trs = table.find_all("tr")
    for x in trs:
        if(len(x.select("td")) != 2):
            continue
        #print(x.select("td")[0].get_text())
        #print(x.select("td")[1].get_text())
#        s = (x.select("td")[0].get_text(),x.select("td")[1].get_text())
#        j = ":".join(s)
        j = x.select("td")[1].get_text()
        arr.append(j)
#        print("=======================")
    print(",".join(arr))

#    db = MySQLdb.connect(host="hadoop03",user="root",passwd="hadoop",db="test",charset="utf8" )
#    cursor = db.cursor()
#    print("arr[0] ===>>> %s" %(arr[0]))
#    print("arr[1] ===>>> %s" %(arr[1]))
#    print("arr[2] ===>>> %s" %(arr[2]))
#    print("arr[3] ===>>> %s" %(arr[3]))
#    print("arr[4] ===>>> %s" %(arr[4]))
#    print("arr[5] ===>>> %s" %(arr[5]))
#    print("arr[6] ===>>> %s" %(arr[6]))
#    print("arr[6] ===>>> %s" %(arr[6]))
#    sql_str = """insert into test.pharmacist_licensed (name,regist_num,license_area,license_type,license_range,license_company,validity_date) values ('%s','%s','%s','%s','%s','%s','%s')""" %(str(arr[0]),str(arr[1]),str(arr[2]),str(arr[3]),str(arr[4]),str(arr[5]),str(arr[6]))
#    print("sql_str ===>>>  %s" %(sql_str))

#    cursor.execute(sql_str)
#    db.commit()#没有提交的话，无法完成插入
#    db.close()
#    return ",".join(arr)
    return arr



if __name__ == "__main__":
    f = open("/data/study/licensed_pharmacist.txt","w+")
    # 1-1000
    datas = []
    sql_str = "insert ignore into test.pharmacist_licensed (name,regist_num,license_area,license_type,license_range,license_company,validity_date,tb_id) values (%s,%s,%s,%s,%s,%s,%s,%s)"
    for x in range(6127,10000):
#        print("===>>> %d " %(x))
        s = (uri,str(x))
        url = "".join(s)
#        print(" url ===>>> %s " %(url))
        arr = get_pharmacist_info(url)
        if(len(arr) < 6):
            continue
        data = arr[:7]
        f.write(",".join(arr))
#        sql_str = """insert ignore into test.pharmacist_licensed (name,regist_num,license_area,license_type,license_range,license_company,validity_date,tb_id) values ('%s','%s','%s','%s','%s','%s','%s',%d)""" %(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6],int(x))
        data.append(x)
        datas.append(data)
        if(len(datas) % 50 == 0):
            db = MySQLdb.connect(host="hadoop03",user="root",passwd="hadoop",db="test",charset="utf8" )
            cursor = db.cursor()
#            sql_str = "insert ignore into test.pharmacist_licensed (name,regist_num,license_area,license_type,license_range,license_company,validity_date,tb_id) values ('%s','%s','%s','%s','%s','%s','%s',%s)"
            cursor.executemany(sql_str,datas)
            db.commit()#没有提交的话，无法完成插入
            print("--------------------- insert once ----------------------------")
            db.close()
            datas = []

        time.sleep(1)
    print("======================================   end ========================")
    f.close() 
