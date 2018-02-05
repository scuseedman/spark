package com.seed.scala2

object ObjectDemo {
  class HelloWorld{
    private var name = "尼古拉斯.guanyu"
    def sayHello{
      (println("hello" + name))
    }
    def getName = name
    
    
  }
  
  def main(args:Array[String]){
    var hello = new HelloWorld
    hello.sayHello
    var name = hello.getName
    println("name ===>>> " + name)
    
    var stu = new Student
    stu.sayHello
    println(stu.name)
    stu.name = "zhangfei"
    println(stu.name)
    println(stu.age)
  }
  
}