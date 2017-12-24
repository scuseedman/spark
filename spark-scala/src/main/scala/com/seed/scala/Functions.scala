package com.seed.scala

/**
 * scala中函数表达式
 */
object Functions {
  //Unit的返回值，类似于java的void的关键字作用
  def main(args:Array[String]) :Unit ={
    var d = calc(2,8)
    println("====> d = " + d)
    var e = calc_a
    println(" ===> e = " + e)
    delayed(time())
    printInt(a=10,b=20)
    // 可变参数
    lengthStringArgs("zhangfei","guanyu","liubei")
    var f = printDefaultArgValues(3)//参数带了默认值，如果该参数没有赋值传入，则使用默认值进行计算
    println(" f is : " + f)
    
  }
  
  /**
   * 默认参数值
   */
  def printDefaultArgValues(a:Int= 5,b :Int= 7) :Int ={
    return a + b 
  }
  /**
   * 可变参数
   */
  def lengthStringArgs(args:String*) ={
    var i : Int = 0
    for (arg <- args ){
      println("args [" + i + "] is : " + arg)
      i += 1 
    }
  }
  def printInt(a:Int,b:Int){
    println("a * b = " + a * b)
    println("value of a is : " + a)
    println("value of b is : " + b )
  }
  def time()={
    println("获取时间，单位毫秒")
    System.nanoTime()
    System.currentTimeMillis()
  }
  def delayed(t : => Long) ={
    println("在方法delayed 内 ......")
    println("===> 参数 t 是：" + t)
  }
  
  def calc(a:Int , b : Int) :Int ={
    return a * b 
  }
  def calc_a() :Int ={
    println("===> 这是一个没有参数的函数调用 ")
    println("===>  5 + 6 = 11")
    return 5 + 6
  }
}