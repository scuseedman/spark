package com.formax.scala

object HelloWorld {
  def main(args: Array[String]): Unit = {
    println("这是我的第一个scala程序。我的语法还没有学会呢 ......")
    println(args(0).split(",")(1))//spark 数组使用方式与java的Array还是有区别的，java为args[0] ,scala为args(0)
    //scala命名规则与java一致，遵循驼峰命名法
    //scala变量命名请注意不要使用保留字
    //scala包的引入注意事项
    import java.awt.{Color, Font}   //正常的引用
// 重命名成员
    import java.util.{HashMap => JavaHashMap}   //将引入的包重命名为一个新的名称
// 隐藏成员
    import java.util.{HashMap => _, _} // 引入了util包的所有成员，但是HashMap被隐藏了
    
  }
}