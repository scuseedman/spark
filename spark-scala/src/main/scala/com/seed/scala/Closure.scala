package com.seed.scala

/**
 * 闭包：闭包是一个函数，其返回值依赖于函数声明外面的变量
 */
object Closure {
  def main(args: Array[String]): Unit = {
    println("===> a(2) = " + a(2))
    println("the value multiplier(1) = " + multiplier(1))
    println("the value multiplier(2) = " + multiplier(2))
    
    var name:String = "zhangfei" //scala中的字符串为java 的String类，scala本身并没有String类型
    println("===> name is : " + name)
    //String对象不可变，如果需要使用可变的，请使用sb来进行创建
    var sb = new StringBuilder;
    sb ++= "liubei;"
    sb ++= "zhangfei;"
    sb ++= "guanyu"
    println("the sb toString is : " + sb.toString())
    println("===> the length of the sb is : " + sb.toString().length())
    println("name concact liubei : " + name.concat(";liubei"))
    printf("===> 格式化字符串，名字是：%s ; 年龄是： %d ; ","zhangfei",55) 
    
  }
  
  val a = (i:Int) => i * 10//闭包
  var factor = 3  
  val multiplier = (i:Int) => i * factor  //闭包
}