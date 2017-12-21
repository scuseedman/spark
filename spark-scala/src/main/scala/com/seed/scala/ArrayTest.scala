package com.formax.scala


/**
 * scala数组：
 */
import Array._
object ArrayTest {
  def main(args:Array[String]):Unit ={
    var arr = new Array[String](3)
    arr(0) = "zhangfei"
    arr(1) = "liubei"
    arr(2) = "guanyu"
    println("arr" + arr.toString())
    for (name <- arr){
      println("===> name is : " + name)
    }
    var a1 = Array(1,3,5,7,9)
    for (y <- a1){
      println("elem is : " + y)
    }
    var a2 = Array(2,4)
    var a3 = concat(a1,a2)
    for (z <- a3){
      println("===> z = " + z)
    }
    println("==========> 萌萌的分割线")
    var a4 = range(10,20,3)//创建一个区间数组，步进为2
    println("length of a4 is : " + a4.length)
    for ( z <- a4){
      println("===> z = " + z)
    }
    println("==========> 萌萌的分割线")
    var a5 = range(10,20)//默认步进为1
    for (x <- a5){
      println("=========> x = " + x)
    }
    for (x <- 0 until a5.length){
      println("===> a5[" + x + "] = " + a5(x))
      printf("=====> a5[%d] = %d \n",x,a5(x))
    }
  }
}