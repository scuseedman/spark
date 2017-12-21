package com.formax.scala
/**
 * scala类与对象 
 */
 class Point(val xc:Int,val yc:Int){
	var x:Int = xc
  var y :Int = yc
  def move(dx:Int,dy:Int){
	println("x坐标点是： " + x)
	println("y坐标点是： " + y)
   x = x + dx
   y = y - dy
   println("新的x坐标点是： " + x)
   println("新的y坐标点是： " + y)
  }
  
}
/**
 * extend继承，使用者需要注意以下3点：
 * 1、重写一个非抽象方法必须使用override关键字 
 * 2、只有主构造函数才能往基类的构造函数里写参数
 * 3、在子类中重写父类的抽象方法时，不需要override关键字
 */
class location (override val xc:Int,override val yc :Int,val zc:Int) extends Point(xc, yc){
  var z :Int = zc
  def move(dx:Int,dy:Int,dz:Int){
	printf("===> 移动前的坐标x/y/z分别为：(%d,%d,%d)\n",x,y,z)
    x += dx
    y += dy
    z -= dz
    printf("===> 移动后的新坐标x/y/z分别为：(%d,%d,%d)\n",x,y,z)
    
  }
}
object ClassObject {
  def main(args:Array[String]){
    var p = new Point(12,29)
    p.move(7, 2)
    var l = new location(1,3,5)
    l.move(2, 4, 6)
  }
}