package com.seed.scala2

class Student {
  /**
   * 定义了非private 修饰的field，自动有getter setter方法？
   */
  var name = "guanyu"
  var age = 12
  def sayHello{
    println("my name is ===>>> " + name + " , I'm ===>>> " + age)
  }
}