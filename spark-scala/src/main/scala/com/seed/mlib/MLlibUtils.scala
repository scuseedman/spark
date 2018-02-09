package com.seed.mlib
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.distributed._
import org.apache.spark.mllib.linalg.{Matrices, Vectors, Vector}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.ml.classification.LogisticRegression
object MLlibUtils {
    //屏蔽不必要的日志显示在终端上
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  Logger.getLogger("org.apache.eclipse.jetty.server").setLevel(Level.OFF)
  //程序入口
  val conf = new SparkConf().setAppName("MLlibUtils").setMaster("local[1]")
  val sc = new SparkContext(conf)

  val LIBSVM_PATH = "E:\\tmp\\a.txt"
  val MATRIX_PATH = "peoples_ages"

  val array_Int = Array(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)
  def main(args: Array[String]) {

//    use_Vector()
    //use_LoadLibSVMFile(LIBSVM_PATH)
//    use_Matrix(3, 2, array_Int)//把一个数组切分成为x行*y列的矩阵
    use_RowMatrix(MATRIX_PATH)
//    use_InDexRowMatrix(MATRIX_PATH)
//    use_CoordinateRowMatrix(MATRIX_PATH)

  }
  
  /**
    * Function Feature:建立向量、标签的具体使用
    *
    * dense方法可以理解为MLlib专用的一种集合形式，与Array类相似
    * spare方法是将给定的数据Array数据分解成几个部分进行处理
    * LabeledPoint是建立向量标签的静态类，主要有两个方法：
    *   1、Features用于显示打印标记点所代表的数据内容
    *   2、Label用于显示标记数
    *
    */
  def use_Vector(): Unit = {
    //建立密集向量
    val vd: Vector = Vectors.dense(2, 0, 6)
    
    //打印密集向量的第3个值
    println("vd(2)===>>> " + vd(2))
    //对密集向量建立标记点
    val pos = LabeledPoint(1, vd)
    //打印标记点内容数据
    println(pos.features)
    //打印既定标记(比如你标记1，打印就是1.0)
    println(pos.label)
    //建立稀疏向量
    val vs: Vector = Vectors.sparse(4, Array(0, 1, 2, 3), Array(9, 5, 2, 7))
    //打印稀疏向量的第3个值
    println("vs(2) ===>>> " + vs(2))
    //对稀疏向量建立标记点
    val neg = LabeledPoint(2, vs)
    println(" ===============================>>> ")
    //打印标记点的内容
    println(neg.features)
    //打印标记点
    println(neg.label)

  }

  /**
    *
    * @param path  传入数据路径
    * Function Feature:调用MLutils.loadLibSVMFile方法
    *
    * MLlib除了程序用到的两种方法建立向量标签外还支持直接从数据库获取固定格式的数据集方法
    * 其数据格式如下：
    *   label index1:value1 index:value2 ...
    * 这里label代表给定的标签，index是索引数，value是每个索引代表的数据值,下面用程序来跑一下
    */
  def use_LoadLibSVMFile(path: String) = {
    MLUtils.loadLibSVMFile(sc, path).foreach(println)
  }

  /**
    * 本地矩阵
 *
    * @param rows 行
    * @param cols  列
    * @param array 数组数据
    * 将Array重组成一个rows行，cols列的矩阵；
    * Matrices.dense方法是矩阵重组的调用方法，该方法有三个参数：
    *   第一个参数是新矩阵的行数
    *   第二个参数是新矩阵的列数
    *   第三个参数为传入的数据值
    */
  def use_Matrix(rows: Int, cols: Int, array: Array[Double]) = {
    val mx = Matrices.dense(rows, cols, array)
    println(mx)
  }


  /**
    * 行矩阵
    *
    * @param path 传入文件路径
    * 可以告诉大家这里要是打印rm的具体内容，最终结果显示的是数据的内存地址
    * 表明RowMatrix在MLlib中仍旧是一个Transformation，并不是最终运算结果
    *
    */
  def use_RowMatrix(path: String) = {
    //按‘’分割后转成Double类型和Vector格式
    val rdd = sc.textFile(path)
      .map(_.split(' ')
      .map(_.toDouble))
      .map(line => Vectors.dense(line))
    //读取行列式格式
    val rm = new RowMatrix(rdd)
    //打印rm
    println(rm)
    //打印行数
    println(rm.numRows())
    //打印列数
    println(rm.numCols())
  }

  /**
    * 带有行索引的行矩阵
    * @param path  传入数据路径
    *
    */
  def use_InDexRowMatrix(path: String) = {
    val rdd = sc.textFile(path)
      .map(_.split(' ')
      .map(_.toDouble))
      .map(line=> Vectors.dense(line))   //转化成向量存储
      .map((vd) => new IndexedRow(vd.size, vd))  //转化格式
    //建立带有索引的行矩阵实例
    val irm = new IndexedRowMatrix(rdd)
    //打印类型
    println(irm.getClass)
    //打印内容数据
    println(irm.rows.foreach(println))
  }

  /**
    * 坐标矩阵
    * @param path  传入数据路径
    * 坐标矩阵一般用于数据比较多且数据较为分散的情形，即矩阵中含0或者某个具体值较多的情况下
    * 坐标矩阵类型格式如下:
    *   (x: Long, y: Long, value: Double)
    *   从格式上看，x和y分别代表标示坐标的坐标轴标号，value是具体内容
    *   x是行坐标，y是列坐标
    * rdd下的最后一个map里，_1和_2这里是scala语句中元祖参数的序数专用标号;
    * 下划线前面有空格，告诉MLlib这里分别是传入的第二个和第三个值
    * 注意：直接打印CoordinateMarix实例对象也仅仅是内存地址
    *
    */
  def use_CoordinateRowMatrix(path: String) = {
    val rdd = sc.textFile(path)
      .map(_.split(" ")
      .map(_.toDouble))
      .map(line => (line(0).toLong, line(1).toLong, line(2)))   //转换成坐标格式
      .map(line2 => new MatrixEntry(line2 _1, line2 _2, line2 _3)) //转化成坐标矩阵格式
    val crm = new CoordinateMatrix(rdd)
    println(crm.entries.foreach(println))
  }
}