package com.seed.scala2

object LazyDemo {
  /**
   * lazy 定义的变量，只有在第一次调用的时候才会进行实例化
   * lazy定义的变量，必须为final。
   */
  def main(args:Array[String]){
    lazy val name = init()
    println("after init called ...")
    println("name : " + name)
  }
  def init():String={
    println(" call init ()")
    "尼古拉斯.zhangfei"
  }
}