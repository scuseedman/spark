#!/usr/bin/python
# -*- coding:utf-8 -*-
# author : seed
# date : 20180301
"""
下载该代码股票的历史交易明细数据
"""
import urllib2
import os
import time
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
# 股票代码
stock_code = "sz000002"
get_date = time.strftime('%Y-%m-%d',time.localtime(time.time() - 86400))
print(get_date)
def Schedule(a,b,c):
    '''''
    a:已经下载的数据块
    b:数据块的大小
    c:远程文件的大小
   '''
    per = 100.0 * a * b / c
    if per > 100 :
        per = 100
    print '%.2f%%' % per
url = "http://market.finance.sina.com.cn/downxls.php?date=%s&symbol=%s" %(get_date,stock_code)
f = urllib2.urlopen(url).read().decode("gbk") 
with open("%s_%s" %(stock_code,get_date), "wb") as code:     
    code.write(f)

