package mlos.sgl.demo.monotonic

import mlos.sgl.core.Polygon
import mlos.sgl.core.Vec2d
import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.TreeSet
import scala.collection.JavaConversions._
import mlos.sgl.core.Geometry
import java.util.Collection

trait SplitterListener {
  def next(v: Vec2d)
  def segment(p: Vec2d, q: Vec2d)
  def start(y: Double)
  def moveLine(y: Double)
  def addActive(p: Vec2d, q: Vec2d)
  def changeHelper(p: Vec2d, q: Vec2d, aux: Vec2d)
  def removeActive(p: Vec2d, q: Vec2d)
  def finished()
}

object Splitter {
  def make(poly: Polygon, types: java.util.List[VertexType], listener: Splitter#EventListener) =
    new Splitter(poly, types, listener)
}

class Splitter(
  val poly: Polygon,
  val types: Seq[VertexType],
  val listener: Splitter#EventListener) {

  type EventListener = SplitterListener

  def addSegment(p: Vertex, q: Vertex) = listener.segment(p.v, q.v)

  class Vertex(val v: Vec2d, val t: VertexType, val left: Edge, val right: Edge) {
    def x = v.x
    def y = v.y
    def next = left.q
    def prev = right.p
  }

  class Edge(var p: Vertex, var q: Vertex, var helper: Vertex = null) {
    def aux = helper
    def aux_=(v: Vertex) {
      helper = v
      listener.changeHelper(p.v, q.v, v.v)
    }
  }

  def buildEventQueue: PriorityQueue[Vertex] = {
    val order = Ordering.by { (p: Vertex) => (p.v.y, -p.v.x) }
    val events = new PriorityQueue[Vertex]()(order)
    val N = poly.vertexCount

    var first = new Edge(null, null)
    var prev = first
    var v: Vertex = null;

    for (k <- 0 until N - 1) yield {
      val next = new Edge(null, null)
      v = new Vertex(poly.v(k), types(k), next, prev)
      events enqueue v
      prev.q = v
      next.p = v
      prev = next
    }
    v = new Vertex(poly.v(N - 1), types(N - 1), first, prev)
    prev.q = v
    first.p = v
    events enqueue v

    return events
  }

  var line: Double = 0;

  val events = buildEventQueue
  val active = new TreeSet[Edge]()(Ordering.by { cut(_) })

  def cut(s: Edge): Double = {
    val p = s.p.v
    val q = s.q.v
    if (p.y != q.y) {
      val t = (line - p.y) / (q.y - p.y);
      return Geometry.lerp(t, p.x, q.x);
    } else {
      return p.x
    }
  }

  def leftOf(v: Vertex) = active.to(new Edge(v, v)).last

  def addEdge(e: Edge) {
    active add e
    listener addActive (e.p.v, e.q.v)
  }

  def removeEdge(e: Edge) {
    if (active remove e) {
      listener removeActive (e.p.v, e.q.v)
    }
  }

  def nextEvent(): Vertex = {
    val p = events.dequeue()
    moveLine(p.v.y)
    listener next p.v
    return p
  }

  def moveLine(y: Double) {
    line = y
    listener moveLine y
  }
  
  def run() {
    listener.start(events.head.v.y)

    while (!events.isEmpty) {
      val v = nextEvent()

      v.t match {
        case VertexType.INITIAL =>
          addEdge(v.left)
          v.left.aux = v

        case VertexType.FINAL =>
          if (v.right.aux.t == VertexType.JOIN) {
            addSegment(v, v.right.aux)
          }
          removeEdge(v.right)

        case VertexType.SPLIT =>
          val ev = leftOf(v)
          addSegment(v, ev.aux)
          ev.aux = v
          v.left.aux = v
          addEdge(v.left)

        case VertexType.JOIN =>
          if (v.right.aux.t == VertexType.JOIN) {
            addSegment(v, v.right.aux)
          }
          removeEdge(v.right)
          val ev = leftOf(v)
          if (ev.aux.t == VertexType.JOIN) {
            addSegment(v, ev.aux)
          }
          ev.aux = v

        case VertexType.NORMAL =>
          if (v.y < v.prev.y) {
            val eg = v.right
            val ed = v.left
            if (eg.aux.t == VertexType.JOIN) {
              addSegment(v, eg.aux)
            }
            removeEdge(eg)
            ed.aux = v
            addEdge(ed)
          } else if (v.y == v.prev.y) {
            if (v.y > v.next.y) { // :(((((
              addEdge(v.left)
              removeEdge(v.right)
              v.left.aux = v
            } else if (v.y < v.next.y) {
              val ev = leftOf(v)
              if (ev.aux.t == VertexType.JOIN) {
                addSegment(v, ev.aux)
              }
              ev.aux = v
            }
          } else {
            val ev = leftOf(v)
            if (ev.aux.t == VertexType.JOIN) {
              addSegment(v, ev.aux)
            }
            ev.aux = v
          }
      }
    }
    listener finished()
  }

}