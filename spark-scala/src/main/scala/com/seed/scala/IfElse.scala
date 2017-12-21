package com.formax.scala
/**
 * if else条件判断语句
 * 语法结构：
 * if(布尔表达式){
 *  true:执行语句块
 *  }else{
 *  false:执行else语句块
 *  }
 */
object IfElse {
  def main(args: Array[String]): Unit = {
    var a = -20
    if (a > 10){
      println("a > 20 条件 成立")
    }else{
      println("a < 20")
    }
  }
}