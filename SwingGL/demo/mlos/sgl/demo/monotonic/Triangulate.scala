package mlos.sgl.demo.monotonic

import scala.collection.immutable.Traversable
import mlos.sgl.core.Polygon
import mlos.sgl.core.Vec2d
import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Stack
import scala.collection.mutable.ListBuffer
import scala.collection.immutable
import mlos.sgl.core.Geometry.{ ccw, cw }

object Side extends Enumeration {
  type Side = Value
  val Left, Right = Value
}

trait TriangulationListener {
  def foundInit(v: Vec2d)
  def foundFinal(v: Vec2d)
  def foundLeft(vs: Traversable[Vec2d])
  def foundRight(vs: Traversable[Vec2d])
  def start()
  def next(v: Vec2d)
  def push(v: Vec2d)
  def pop()
  def addSegment(a: Vec2d, b: Vec2d)
}

class Triangulate(val poly: Polygon, val listener: Triangulate#EventListener) {

  import Side._

  type EventListener = TriangulationListener
  type Vertex = Tuple2[Vec2d, Side]

  class VertexWithSide(val v: Vec2d, val side: Side)

  private def toOrd(v: Vec2d) = (v.y, -v.x)
  private val order = Ordering.by { (p: VertexWithSide) => toOrd(p.v) }
  private val queue = new PriorityQueue[VertexWithSide]()(order)
  private val stack = new Stack[VertexWithSide]

  private def N = poly.vertexCount

  private def extremeIndices: Tuple2[Int, Int] = {
    val order = (k: Int) => toOrd(poly.v(k))
    val bottom = (0 until N) minBy (order)
    val top = (0 until N) maxBy (order)
    return (top, bottom)
  }

  def findSides(top: Int, bottom: Int): Tuple2[Seq[Vec2d], Seq[Vec2d]] = {
    def fromTop(x: Int) = (x + top) % N
    def itemFromTop(x: Int) = poly.v(fromTop(x))

    val (lefts, rest) = (1 until N) span (fromTop(_) != bottom)
    val rights = rest.drop(1)

    return (lefts map itemFromTop, rights map itemFromTop)
  }

  private def pop() {
    listener.pop()
    stack.pop
  }

  private def push(p: VertexWithSide) {
    listener.push(p.v)
    stack.push(p)
  }

  private def inside(prev: Vec2d, top: Vec2d, next: Vec2d, s: Side) = s match {
    case Left => ccw(prev, top, next)
    case Right => cw(prev, top, next)
  }

  private def takeNext(): VertexWithSide = {
    val p = queue.dequeue()
    listener.next(p.v)
    return p
  }

  private def topTwo = (stack.iterator.drop(1).next, stack.top)

  private def fillQueue() {
    val (top, bottom) = extremeIndices
    val initVertex = poly.v(top)
    val finalVertex = poly.v(bottom)

    listener.foundInit(initVertex)
    listener.foundFinal(finalVertex)

    queue enqueue new VertexWithSide(initVertex, null)
    queue enqueue new VertexWithSide(finalVertex, null)

    val (lefts, rights) = findSides(top, bottom)
    listener.foundLeft(lefts.to[immutable.Seq])
    listener.foundRight(rights.to[immutable.Seq])

    queue ++= lefts map { new VertexWithSide(_, Left) }
    queue ++= rights map { new VertexWithSide(_, Right) }
  }

  def run() {
    fillQueue()
    listener.start()

    for (_ <- 1 to 2) {
      push(queue.dequeue())
    }

    while (!queue.isEmpty) {
      val n = takeNext()
      if (n.side != stack.top.side) {
        val top = stack.top
        while (!stack.isEmpty) {
          if (stack.size > 1) {
            listener.addSegment(n.v, stack.top.v)
          }
          pop()
        }
        push(top)
        push(n)
      } else {
        var stop = false
        while (!stop && stack.size > 1) {
          val (prev, top) = topTwo
          if (inside(prev.v, top.v, n.v, n.side)) {
            listener.addSegment(n.v, prev.v)
            pop()
          } else {
            stop = true
          }
        }
        push(n)
      }
    }
  }

}