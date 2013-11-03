package mlos.sgl.demo.monotonic

import scala.collection.immutable.Traversable
import mlos.sgl.core.Polygon
import mlos.sgl.core.Vec2d
import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Stack
import scala.collection.mutable.ListBuffer
import scala.collection.immutable
import mlos.sgl.core.Geometry.{ ccw, cw }
import scala.collection.mutable.Queue
import scala.collection.mutable.LinkedList
import scala.collection.JavaConversions._
import scala.collection.immutable.Nil

object Side extends Enumeration {
  type Side = Value
  val Top, Left, Right, Bottom = Value
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
  def addTriangle(a: Vec2d, b: Vec2d, c: Vec2d)
  def finished()
}

class Triangulate(poly: Polygon, listener: Triangulate#EventListener) {

  import Side._

  type EventListener = TriangulationListener

  class Vertex(val v: Vec2d, val side: Side)

  private def toOrd(v: Vec2d) = (v.y, -v.x)
  private val order = Ordering.by { (p: Vertex) => toOrd(p.v) }
  private val queue = new Queue[Vertex]
  private val stack = new Stack[Vertex]

  private val vs: IndexedSeq[Vec2d] = Array(poly.vs: _*)
  private def N = vs.length

  private def extremeIndices: Tuple2[Int, Int] = {
    val order = (k: Int) => toOrd(vs(k))
    val bottom = (0 until N) minBy (order)
    val top = (0 until N) maxBy (order)
    return (top, bottom)
  }

  def findSides(top: Int, bottom: Int): Pair[List[Vec2d], List[Vec2d]] = {
    def fromTop(x: Int) = (x + top) % N
    def itemFromTop(x: Int) = vs(fromTop(x))

    val (lefts, rest) = (1 until N) span (fromTop(_) != bottom)
    val rights = rest.drop(1)

    return (lefts.map(itemFromTop).toList, rights.map(itemFromTop).toList)
  }

  private def pop(): Vertex = {
    listener.pop()
    stack.pop()
  }

  private def push(p: Vertex) {
    listener.push(p.v)
    stack.push(p)
  }

  private def inside(prev: Vec2d, top: Vec2d, next: Vec2d, s: Side) = s match {
    case Left => ccw(prev, top, next)
    case Right => cw(prev, top, next)
  }

  private def takeNext(): Vertex = {
    val p = queue.dequeue()
    listener.next(p.v)
    return p
  }

  private def topTwo = (stack.iterator.drop(1).next, stack.top)

  private def fillQueue() {
    val (top, bottom) = extremeIndices
    val initVertex = vs(top)
    val finalVertex = vs(bottom)

    listener.foundInit(initVertex)
    listener.foundFinal(finalVertex)

    val (lefts, rights) = findSides(top, bottom)
    listener.foundLeft(lefts)
    listener.foundRight(rights)

    queue enqueue new Vertex(initVertex, Top)

    def merge(left: List[Vertex], right: List[Vertex]): List[Vertex] = {
      (left, right) match {
        case (xs, Nil) => xs
        case (Nil, ys) => ys
        case (x :: xs, y :: ys) =>
          if (x.v.y >= y.v.y) x :: merge(xs, right)
          else y :: merge(left, ys)
      }
    }
    val lvs = lefts map { new Vertex(_, Left) }
    val rvs = rights reverseMap { new Vertex(_, Right) }
    queue ++= merge(lvs, rvs)
    queue enqueue new Vertex(finalVertex, Bottom)
  }

  def run() {
    fillQueue()
    listener.start()

    def drainStack(n: Vertex) {
      while (!stack.isEmpty) {
        if (stack.size > 1)
          cut(n)
        pop()
      }
    }

    def reduceChain(n: Vertex) {
      val (prev, top) = topTwo
      if (inside(prev.v, top.v, n.v, n.side)) {
        cut(n)
        pop()
        if (stack.size > 1)
          reduceChain(n)
      }
    }

    def cut(n: Vertex) {
      val (p, top) = topTwo
      if (top.side == Left)
        listener.addTriangle(p.v, top.v, n.v)
      else
        listener.addTriangle(top.v, p.v, n.v)
    }

    push(takeNext())
    push(takeNext())

    while (!queue.isEmpty) {
      val n = takeNext()
      if (n.side == Bottom) {
//        stack.pop()
        drainStack(n)
      } else {
        if (n.side == stack.top.side) {
          reduceChain(n)
        } else {
          val top = stack.top
          drainStack(n)
          push(top)
        }
        push(n)
      }
    }
    listener.finished()
  }

}