package com.seed.scala
/**
 * scala正则表达式
 * Scala 的正则表达式继承了 Java 的语法规则，Java 则大部分使用了 Perl 语言的规则。
 * 总体上来说跟java的正则类似，使用的时候不熟悉的地方请查阅相关文档
 */
import scala.util.matching.Regex
object RegexTest {
  def main(args:Array[String]){
    val pattern = "Java".r//使用String中的r()方法构造一个regexp对象
    val str = "Scala is Scalable and cool"
    println(pattern  findFirstIn str)//findFirstIn 找到首个匹配项 ：None
    val r1 = new Regex("(S|s)cala")
    println("==============>")
    println((r1 findAllIn str ).mkString(","))//使用逗号分割返回匹配的结果
    println(r1 replaceFirstIn(str, "Java"))//将匹配到的第一个替换为Java
    
    
  }
}