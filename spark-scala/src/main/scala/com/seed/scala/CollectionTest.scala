package com.formax.scala
/**
 * scala集合
 * 
 */
object CollectionTest {
  def main(args:Array[String]){
    printf("程序开始启动了,测试集合 ===> \n")
    var l1 = List(1,2,3,4)
    println("l1 : " + l1)
    println("l1 reverse : " + l1.reverse)
    var l2 = List()
    var l3 :List[List[Int]] = List(
      List(1,3,4,7),
      List(2,4,6,8)
    )
    println("the head of l3 is : " + l3.head)
    println("the tail of l3 is : " + l3.tail)
    println("empty l3 ? : " + l3.isEmpty)
    println("the length of l1 is :" + l1.length)
    println("the length of l2 is : " + l2.size)
    println("the length of l3 is : " + l3.size)
    for(z <- l3){
      println("===> z = " + z)
      for(y <- 0 until z.size){
        println("===> z(" + y + ") = " + z(y))
      }
    }
    var s1 = Set(1,2,3,4)
    println("the length of s1 is : " + s1.size)
    println("the class of s1 is : " + s1.getClass.getName)
    println("s1 : " + s1)
    var s2 = Set(2,5,7,8)//set集合
    println("s1 与s2的两个集合交集是：" + s1.&(s2))//获取两个元素的交集
    var m1 = Map("one" -> 1,"two" -> 2,"three" -> 3)
    var m2 :Map[String,Int] = Map("zhangfei" -> 10,"guanyu" -> 20)//map
    println("m2 : " + m2)
    println("m2(key)'s value = " + m2("zhangfei"))
    println("guanyu in m2 ? " + m2.contains("guanyu"))
    var t1 = (20,"zhangfei")//tuple元组
    println("t1._1 = " + t1._1)
    println("t1._2 = " + t1._2)
    //元组迭代
    println(t1.productElement(1))
    
    //Option
    var v1:Option[Int] = m2.get("zhangfei")//Scala 使用 Option[Int] 来告诉你：「我会想办法回传一个 Int，但也可能没有 Int 给你
    var v2 :Option[Int] = m2.get("liubei")
    println("v1 is : " + v1 + " ; and v2 is : " + v2)//v1 is : Some(10) ; and v2 is : None
    
    var i1 = Iterator("zhangfei","liubei","guanyu","zhaoyun")//迭代器
    while(i1.hasNext){
      println("===> i1.element = " + i1.next())
    }
  }
}