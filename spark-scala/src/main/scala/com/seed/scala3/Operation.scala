package com.seed.scala3

object Operation {
  def main(args: Array[String]): Unit = {
//    sunsuOperation //算术运算符
//    relationOperation //关系运算符
//    logicOperation //逻辑运算符
//    bitOperation //位运算
//    assignmentOperation //赋值运算
  }
  /**
   * 赋值运算
   */
  def assignmentOperation(){
	  val str = """=  简单的赋值运算，指定右边操作数赋值给左边的操作数。 C = A + B 将 A + B 的运算结果赋值给 C
			  +=  相加后再赋值，将左右两边的操作数相加后再赋值给左边的操作数。  C += A 相当于 C = C + A
			  -=  相减后再赋值，将左右两边的操作数相减后再赋值给左边的操作数。  C -= A 相当于 C = C - A
			  *=  相乘后再赋值，将左右两边的操作数相乘后再赋值给左边的操作数。  C *= A 相当于 C = C * A
			  /=  相除后再赋值，将左右两边的操作数相除后再赋值给左边的操作数。  C /= A 相当于 C = C / A
			  %=  求余后再赋值，将左右两边的操作数求余后再赋值给左边的操作数。  C %= A is equivalent to C = C % A
			  <<= 按位左移后再赋值  C <<= 2 相当于 C = C << 2
			  >>= 按位右移后再赋值  C >>= 2 相当于 C = C >> 2
			  &=  按位与运算后赋值  C &= 2 相当于 C = C & 2
			  ^=  按位异或运算符后再赋值 C ^= 2 相当于 C = C ^ 2
			  |=  按位或运算后再赋值 C |= 2 相当于 C = C | 2"""
    for(line <- str.split("\\n")){
      println(" --->>> " + line.trim())
    }
  }
  /**
   * 位运算符
   */
  def bitOperation(){
	  val str = """&  按位与运算符  (a & b) 输出结果 12 ，二进制解释： 0000 1100
			  | 按位或运算符  (a | b) 输出结果 61 ，二进制解释： 0011 1101
			  ^ 按位异或运算符 (a ^ b) 输出结果 49 ，二进制解释： 0011 0001
			  ~ 按位取反运算符 (~a ) 输出结果 -61 ，二进制解释： 1100 0011， 在一个有符号二进制数的补码形式。
			  <<  左移动运算符  a << 2 输出结果 240 ，二进制解释： 1111 0000
			  >>  右移动运算符  a >> 2 输出结果 15 ，二进制解释： 0000 1111
			  >>> 无符号右移 A >>>2 输出结果 15, 二进制解释: 0000 1111"""
    for(line <- str.split("\\n")){
      println(" --->>> " + line.trim())
    }
  }
  /**
   * 逻辑运算符
   */
  def logicOperation(){
	  val str = """&& 逻辑与 (A && B) 运算结果为 false
			  ||  逻辑或 (A || B) 运算结果为 true
			  ! 逻辑非 !(A && B) 运算结果为 true"""
    for (line <- str.split("\\n")){
      println(" --->>> " + line.trim())
    }
      
  }
  /**
   * 关系运算符
   */
  def relationOperation(){
	  val str = """== 等于  (A == B) 运算结果为 false
			  !=  不等于 (A != B) 运算结果为 true
			  > 大于  (A > B) 运算结果为 false
			  < 小于  (A < B) 运算结果为 true
			  >=  大于等于  (A >= B) 运算结果为 false
			  <=  小于等于  (A <= B) 运算结果为 true"""
    for(x <- str.split("\\n")){
      println(" ===>>> " + x.trim() )
    }
  }
  /**
   * 算术运算符
   */
  def sunsuOperation(){
	  val str = """+  加号  A + B 运算结果为 30
			  - 减号  A - B 运算结果为 -10
			  * 乘号  A * B 运算结果为 200
			  / 除号  B / A 运算结果为 2
			  % 取余  B % A 运算结果为 0"""
    println(str.split("\\n").length)
    for(x <- str.split("\\n")){
      println(x.trim())
      println(" =================>>>>>>> ")
    }
  }
}