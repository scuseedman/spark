package com.seed.scala2

object ArrayTest {
  def main(args:Array[String]):Unit={
//    arrayTest
    val a1 = Array(2,4,6,8)
    for(x <- a1)println("x === " + x)
    val a2 = for(x <- a1) yield x * x
    println("length of a2 >>> " + a2.length)
    for(y <- a2)println("y ===>>> " + y)
    var str = arrayTest1()
    println(" str >>> " + str)
  }
  def arrayTest1(){
    val a1 = Array(1,2,34,5,6)
    val a2 = for(x <- a1)yield x*x
    println("length of a2 ============= " + a2.length)
    for(z <- a2)println("z ===>>> " + z)
    a2.mkString("|")
  }
  def arrayTest(){
	  var arr = new Array[Int](3);
	  arr(0) = 1
			  arr(1) = 3
			  arr(2)= 5
			  
			  for(x <- arr)println(x)
			  var str = arr.mkString
			  println("str ===>>> " + str)  
			  str = arr.mkString(",")
			  println("str ===>>> " + str)  
			  str = arr.mkString("<", ",", ">")//左右中
			  println("str ===>>> " + str)  
    
  }
}