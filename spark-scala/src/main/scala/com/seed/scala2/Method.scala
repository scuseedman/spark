package com.seed.scala2

/**
 * 
 */
object Method {
  def main(args:Array[String]):Unit={
    var name = sayHello("zhangfei",22)
    var sum = sums(10)
    println("sum ===>>> " + sum)
    sayHello("zhangfei",33)
    
    var s_fab = fab(4)
    println("s_fab ===>>> " + s_fab)   //??? why is 5?
    
    
    defaultValue("zhangfei")
  }
  /**
   * scala中的返回值与java有区别 ，不使用return 关键字，以最后一个表达式的结果做为返回值
   * 返回值的类型不需要显式指定，但是如果使用return 关键字 ，则必须显式指定返回值的类型
   */
  def sayHello(name:String,age:Int):String={
    printf(" %s , your age is : %d \n",name,age)
    "guanyu"
  }
  def sums(z:Int):Int={
    var sum = 0
    for(i <- 1 to z)sum += i
    sum
  }
  
  /**
   * 斐波那契数列
   */
  def fab(n:Int):Int={
    if(n <= 1){
      1
    } else{
      println(" enter here once !!! " + n )
      fab(n - 1 ) + fab(n -2)
    }
  }
  
  /**
   * 默认参数
   */
  def defaultValue(name:String,middleName:String="尼古拉斯"){
//    printf("first name ===>>> %s ; middleName ===>>> %s \n " ,name,middleName)
    printf("%s.%s \n" , middleName,name)
  }
}