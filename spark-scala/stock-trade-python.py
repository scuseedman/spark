#!/usr/bin/python
# -*- coding:utf-8 -*-
import time
import datetime
import string
import urllib2
import re
import sys
import urllib
import random

import smtplib
from email.mime.text import MIMEText
from email.header import Header
from email.mime.multipart import MIMEMultipart
from email.mime.application import MIMEApplication
reload(sys)
sys.setdefaultencoding('utf-8')
_pwd  = sys.argv[1]
"""
假设你以10元/股的价格买入100手(1万股)上海A股，券商佣金为千分之1(含规费)，以此为例，对该笔交易买入成本计算如下：
　　买入股票所用金额：10元/股×10000股=100000元；
　　过户费：0.6元÷1000股×10000股=6元(沪市股票计算，深市为0)；
　　交易佣金：100000×1‰=100元(其中包含经手费：10万*0.0696‰=6.96元；证管费：10万*0.02‰=2元)；
　　买入总成本：100000元+6元+100元=100106元(买入10000股，每股10元，所需总资金)。
　　每股多少钱卖出才不赔钱？
　　可按如下公式计算：(买入总成本+卖出过户费)÷(1-印花税率-交易佣金率)÷股票数量=(100106元+6元)÷(1-0.001-0.001)÷10000=10.03126元=10.03元(四舍五入)。
　　若以10.10元/股卖出计算：
　　股票金额：10.10元/股×10000股=101000元；
　　印花税：101000元×1‰=101元；
　　过户费：0.6元÷1000股×10000股=6元；
　　交易佣金：101000元×1‰=101元(其中包含经手费：101000元*0.0696‰=7.03元；证管费：101000元*0.02‰=2.02元)；
　　卖出后收入：101000元-101元-6元-101元=100792元；
　　最终实际盈利为：卖出后收入-买入总成本=100792-100106=686元；即当该股票涨幅1%时，获利686元。
"""
# 股票代码
stock_code = "sz000002"
# 判断是否盈利发送邮件的基准线
sale_level = -60
# 股票价格
#pri = raw_input("购买价格：")
pri = 33.05
# 设置购买日期
buy_date = '2018-02-28'
# 购买数量 
#nums = raw_input("购买数量：")
nums = 100
# 股票成本总量
amount = float(pri)*float(nums)
# 交易佣金 ,假设最低佣金为5元
commission = amount*0.001
if commission <=5:
    commission = 5
# 过户费（沪市有，深市无）
transfer = float(nums)*(0.6/1000)
# 总成本
total = amount + transfer + commission

# ---------------------------------------
# 买入之后平本线价格
pri_ping = (total+transfer)/(1-0.001-0.001)/float(nums)


# ---------------------------------------
# 卖出
percent = 0.02
# 实时行情获取地址，从东方财富
#url = "http://finance.ifeng.com/app/hq/stock/sz300104/"
url = "http://hq.sinajs.cn/?format=text&list=%s" %(stock_code)

def transaction_fee():
    print(" 买入过户费 : %s " %(transfer))
    print(" 买入购买价格 : %s " %(pri))
    print(" 买入数量 : %s " %(nums))
    print(" 买入交易佣金 : %s " %(commission))
    print(" 买入市值 : %s " %(amount))
    print(" 买入总成本 : %s " %(total))
    print(" 卖出平本参考价格 : %s " %(pri_ping))

"""
0: "华泰证券": 股票名字
1: "20.280": 今日开盘价
2: "20.250": 昨日收盘价
3: "20.340": 当前价格
4: "20.400": 今日最高价
5: "20.200": 今日最低价
6: "20.320": 竞买价，即“买一”报价
7: "20.340": 竞卖价，即“卖一”报价
8: "5737081": 成交的股票数（单位为“个”）
9: "116441306.000": 成交金额（单位为“元”）
10: "1200": “买一”申请 1200 股
11: "20.320": “买一”报价
12: "4900": “买二”申请 4900 股
13: "20.310": “买二”报价（以下依次类推）
14: "44300": 买三
15: "20.300": 买三
16: "30200": 买四
17: "20.290": 买四
18: "18900": 买五
19: "20.280": 买五
(20,21), (22,23), (24,25), (26,27), (28,29): 卖一，……，卖五
30: "2016-11-22": 日期
31: "09:48:11": 时间
"""
def get_realtime_price(url):
    myPage = urllib2.urlopen(url).read().decode("gbk")
    print("-------------------------")
    print(str(myPage))
    arr = myPage.split(",")
    print(" 实时价格 : %s" %(arr[3]))
    print(" 今日开盘价 %s -- 昨日收盘价 %s " %(arr[1],arr[2]))
    print(" 今日最高价 %s -- 今日最低价 %s " %(arr[4],arr[5]))
    print(" 买一申报价格-股数 (%s,%s) " %(arr[11],arr[10]))
    print(" 买二申报价格-股数 (%s,%s) " %(arr[13],arr[12]))
    print(" 买三申报价格-股数 (%s,%s) " %(arr[15],arr[14]))
    print(" 买四申报价格-股数 (%s,%s) " %(arr[17],arr[16]))
    print(" 买五申报价格-股数 (%s,%s) " %(arr[19],arr[18]))
    print(" 卖一申报价格-股数 (%s,%s) " %(arr[21],arr[20]))
    print(" 卖二申报价格-股数 (%s,%s) " %(arr[23],arr[22]))
    print(" 卖三申报价格-股数 (%s,%s) " %(arr[25],arr[24]))
    print(" 卖四申报价格-股数 (%s,%s) " %(arr[27],arr[26]))
    print(" 卖五申报价格-股数 (%s,%s) " %(arr[29],arr[28]))
    print("-------------------------")
    return float(arr[3]) 

# 当前价格卖出与买入的差价
def sale_is_profit(last_price):
    # 售出总价
    total_price = last_price * nums
    # 印花税
    stamp_duty = total_price * 0.001
    print(" 卖出印花税 : %s " %(stamp_duty))
    # 过户费
    sale_transfer = (0.02/1000)*nums
    print(" 卖出过户费 : %s" %(sale_transfer))
    # 售出交易佣金.有最低保护价
    sale_commission = (2.5/10000)*total_price
    if sale_commission <= 5:
        sale_commission = 5
    print(" 卖出交易佣金 : %s " %(sale_commission))
    sale_real_income = total_price - stamp_duty - sale_transfer - sale_commission
    print(" 该价格卖出总市值 : %s " %(total_price))
    print(" 卖出实际所得 : %s" %(sale_real_income))
    # 卖出总盈利
    sale_profit = sale_real_income - total
    print(" 卖出盈利 : %s" %(sale_profit))
    # 计算持有天数
    date_diff = Caltime(buy_date)
    # 计算年化收益率  (公式：收益额*10000*365天/持有天数/购买总成本/100)
    year_income_percent = sale_profit*10000*365.0/date_diff/total/100
    # 当要输出百分号时 需要 %% 这么写，并指定2位小数
    print(" 当前年化收益 : %.2f %% " %(year_income_percent))
    if sale_profit >= sale_level:
        content = " %s 当前价格 : %s ; 当前已可以盈利 : %s ; 请判断" %(stock_code,last_price,sale_profit)
        title = "%s price judge" %(stock_code)
        SendEmail("250239675@qq.com",'',content,title)

def Caltime(date1):  
    buy_time = time.strptime(date1,'%Y-%m-%d')
    y,m,d,H,M,S = buy_time[:6] 
    old_time = datetime.datetime(y,m,d,H,M,S)
    now_time = datetime.datetime.now()
    return float((now_time-old_time).days)
def SendEmail(fromAdd, toAdd,content,title):
    print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx 进入邮件发送 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
    toAdd = ','.join(['250239675@qq.com'])
    # 如名字所示： Multipart就是多个部分
    msg = MIMEMultipart()
    msg["Subject"] = title
    msg["From"]    = fromAdd
    msg["To"]      = toAdd

    # 下面是文字部分，也就是纯文本
    puretext = MIMEText(content)
    msg.attach(puretext)

    # 下面是附件部分 ，这里分为了好几个类型
    # 首先是xlsx类型的附件
    xlsxpart = MIMEApplication(open('/data/study/stock-trade-python.py', 'rb').read())
    xlsxpart.add_header('Content-Disposition', 'attachment', filename='stock-trade-python.py')
    msg.attach(xlsxpart)

    try:
        s = smtplib.SMTP_SSL("smtp.qq.com", 465)
        s.login(fromAdd, _pwd)
        s.sendmail(fromAdd,toAdd, msg.as_string())
        s.quit()
        print "Success!"

    except smtplib.Exception,e:
        print e.message

if __name__ == "__main__":
    print("...............................................................................................")
    transaction_fee()
    last_price = get_realtime_price(url)
    sale_is_profit(last_price)
    now_time = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
    print(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> end !!! ......... %s " %(now_time))



