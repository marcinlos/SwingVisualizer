package mlos.sgl.demo.delaunay

import mlos.sgl.core.Vec2d
import mlos.sgl.util.Randomizer
import mlos.sgl.core.Rect
import scala.collection.JavaConversions._
import scala.collection.mutable.HashMap
import mlos.sgl.core.Geometry
import scala.util.Random

object SpeedTest extends App {

  val dummy = new Delaunay#Listener {
    def point(a: Vec2d) = ()
    def nextHop(t: Triangle) = ()
    def foundContaining(v: Vec2d, t: Triangle) = ()
    def beginFixup() = ()
    def testCircle(t: Triangle, n: Triangle, v: Vertex) = ()
    def break(t: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, tc: Triangle) = ()
    def breakEdge(t: Triangle, s: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, sa: Triangle, sb: Triangle) = ()
    def visit(t: Triangle) = ()
    def flip(p: Triangle, q: Triangle) = ()
    def endFixup() = ()
    def triangle(t: Triangle) = ()
    def finished() = ()
  }

  def onSegment(n: Int, a: Vec2d, b: Vec2d) =
    Range.Double(0, 1, 1.0 / n).tail map { Geometry.lerp(_, a, b) }

  def squareBorder(n: Int) = {
    val r = Rect.aroundOrigin(1)
    val left = onSegment(n, r.leftTop, r.leftBottom)
    val right = onSegment(n, r.rightTop, r.rightBottom)
    val top = onSegment(n, r.leftTop, r.rightTop)
    val bottom = onSegment(n, r.leftBottom, r.rightBottom)
    IndexedSeq.concat(left, right, top, bottom)
  }

  private def measure(a: => Unit): Long = {
    val before = System.nanoTime
    a
    val after = System.nanoTime
    return (after - before) / 1000000
  }

  private def randomUniform(n: Int, f: Delaunay => Vec2d => Triangle) = {
    val points = Randomizer.inRect(Rect.aroundOrigin(1)).list(n)
    delounay.run(points, f)
  }

  val delounay = new Delaunay(dummy)

  //  val nums = Array(100, 500, 1000, 10000, 100000)
  val nums = Range(1000, 50000, 1000)
  // warmup
  randomUniform(1000, _.findByWalk)
  //  uniformComparison()
  //  uniformHistoryGraph()
  randVsSequential()

  def uniformComparison() = {
    println("==== Delaunay for uniform distribution ====")
    val mapWalk = new HashMap[Int, Long]
    val mapHist = new HashMap[Int, Long]
    val k = 5

    for (i <- 1 to k) {
      nums foreach { n =>
        val twalk = measure { randomUniform(n, _.findByWalk) }
        val thist = measure { randomUniform(n, _.findByHistory) }
        Console.print('*')
        Console.flush
        val walkTotal = mapWalk.getOrElse[Long](n, 0)
        val histTotal = mapHist.getOrElse[Long](n, 0)
        mapWalk(n) = walkTotal + twalk
        mapHist(n) = histTotal + thist
      }
      Console.println(s"Done $i")
    }
    nums foreach { n =>
      val twalk = mapWalk(n) / k
      val thist = mapHist(n) / k
      println(s"$n\t$twalk\t$thist")
    }
  }

  def uniformHistoryGraph() = {
    println("==== History graph method ====")
    Range(10000, 500000, 10000) foreach { n =>
      val thist = measure { randomUniform(n, _.findByHistory) }
      println(s"$n\t$thist")
    }
  }

  def fullStats(points: Seq[Vec2d], f: Delaunay => Vec2d => Triangle) = {
    val stats = new BookKeeper
    val del = new Delaunay(stats)
    val t = measure { del.run(points, f) }
    stats.makeStats(t)
  }

  def randVsSequential() {
    Range(100, 50000, 50) foreach { n =>
      val points = squareBorder(n)
      val r = new Random
      val seqStat = fullStats(points, _.findByHistory)

      val k = 100
      val rands = for (_ <- 1 to k) yield {
        val randOrder = r.shuffle(points)
        fullStats(randOrder, _.findByHistory)
      }
      val randOrder = r.shuffle(points)
      val randStat = Stats.avg(rands:_*)//fullStats(randOrder, _.findByHistory)

      println(s"$n\t${seqStat.formatAsRow}\t${randStat.formatAsRow}")
    }
  }

}