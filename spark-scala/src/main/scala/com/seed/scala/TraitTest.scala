package com.formax.scala
/**
 * scala trait特征
 * scala trait 特征类似于java的接口，但是功能比接口更强大
 * 与接口不同的是，它可以定义属性和方法的实现
 * scala的类只能继承一个父类，但是使用trait特征标明的话，可以实现多继承
 */
trait Equal {
  def isEqual(x:Any):Boolean
  def isNotEqual(x:Any):Boolean = ! isEqual(x)
}
class Point2 (xc:Int,yc:Int) extends Equal{
  var x:Int = xc
  var y:Int = yc
  def isEqual(obj:Any)=obj.isInstanceOf[Point] && obj.asInstanceOf[Point].x == x
  
}
object TraitTest{
  def main(args:Array[String]){
    var p1 = new Point2(2,3)
    var p2 = new Point2(2,4)
    var p3 = new Point2(3,3)
    println("p1 is not equal p2 ? " + p1.isNotEqual(p2))
    println("p1 is not equal p3 ? " + p1.isNotEqual(p3))
    println("p1 is not equal 2 ? " + p1.isNotEqual(2) )
    println("is match test ===> " + matchTest("two"))//2
    println("is match test ===> " + matchTest("test"))//many
    println("is match test ===> " + matchTest(1))//one
    println("is match test ===> " + matchTest(6))//Scala.Int
  }
  /**
   * scala里的match类似于java的switch。而这个关键字比switch区别在于，如果switch语句中没有加上break关键字，当在匹配上之后，后续的所有匹配也会招待，而
   * match并不会，只会执行匹配上的语句 
   */
  def matchTest(x:Any):Any = x match {  //任意类型的匹配
    case 1 => "one"
    case "two" => 2 
    case y:Int => "Scala.Int"//判断传入的参数类型是否为int?如果是int则输出scala.Int
    case _ => "many"
  }
}