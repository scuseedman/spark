package com.seed.scala
/**
 * scala提取器
 * 从传递给它的对象中提取出构造该对象的参数
 * Scala 提取器是一个带有unapply方法的对象。unapply方法算是apply方法的反向操作：
 * unapply接受一个对象，然后从对象中提取值，提取的值通常是用来构造该对象的值。
 */
object ExtractorTest {
  def main(args:Array[String]){
    println("=====> scala提取器 ......")
    println("apply 方法 ===> " + apply("zhangfei","gmail.com"))//zhangfei@gmail.com
    println("unapply 方法 ===>" + unapply("zhangfei@gmail.com"))//Some((zhangfei,gmail.com))
    println("unapply 方法 ===>" + unapply("zhangfei guanyu"))//None
  }
  // 注入方法 (可选)
   def apply(user: String, domain: String) = {
      user +"@"+ domain
   }
    // 提取方法（必选）
   def unapply(str: String): Option[(String, String)] = {
      val parts = str split "@"
      if (parts.length == 2){
         Some(parts(0), parts(1)) 
      }else{
         None
      }
   }
}