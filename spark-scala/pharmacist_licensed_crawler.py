#!/usr/bin/python
# -*- coding:utf-8 -*-
import time
import datetime
import string
import urllib2
import re
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
        s = (x.select("td")[0].get_text(),x.select("td")[1].get_text())
        j = ":".join(s)
        arr.append(j)
#        print("=======================")
    print(",".join(arr)
    return ",".join(arr)


if __name__ == "__main__":
    f = open("/data/study/licensed_pharmacist.txt","w+")
    for x in range(1100,1112):
        print("===>>> %d " %(x))
        s = (uri,str(x))
        url = "".join(s)
#        print(" url ===>>> %s " %(url))
        info = get_pharmacist_info(url)
        f.write(info)

        time.sleep(2)
    f.close() 
