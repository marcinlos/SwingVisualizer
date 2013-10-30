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
}

object Splitter {
  def make(poly: Polygon, types: java.util.List[VertexType], listener: Splitter#EventListener) = 
    new Splitter(poly, types, listener)
}

class Splitter(
    val poly: Polygon, 
    val types: Seq[VertexType], 
    val listener: Splitter#EventListener
) {

  type EventListener = SplitterListener
  
  def addSegment(p: Vertex, q: Vertex) = listener.segment(p.v, q.v)

  class Vertex(val v: Vec2d, val t: VertexType, val left: Edge, val right: Edge) {
    def x = v.x
    def y = v.y
    def next = left.q
    def prev = right.p
  }

  class Edge(var p: Vertex, var q: Vertex, var aux: Vertex = null)

  def buildEventQueue: PriorityQueue[Vertex] = {
    val events = new PriorityQueue[Vertex]()(Ordering.by { _.v.y })
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
    if (p != q) {
      val t = (line - p.y) / (q.y - p.y);
      return Geometry.lerp(t, p.x, q.x);
    } else {
      return p.x
    }
  }

  def leftOf(v: Vertex) = active.to(new Edge(v, v)).last
  
  def nextEvent(): Vertex = {
    val p = events.dequeue()
    listener next p.v
    return p
  }

  def run() {

    while (!events.isEmpty) {
      val v = nextEvent()
      line = v.y

      v.t match {
        case VertexType.INITIAL =>
          active add v.left
          v.left.aux = v

        case VertexType.FINAL =>
          if (v.left.aux.t == VertexType.JOIN) {
            addSegment(v, v.left.aux)
          }
          active remove v.left

        case VertexType.SPLIT =>
          val ev = leftOf(v)
          addSegment(v, ev.aux)
          ev.aux = v
          v.right.aux = v
          active add v.right

        case VertexType.JOIN =>
          if (v.right.aux.t == VertexType.JOIN) {
            addSegment(v, v.right.aux)
          }
          active remove v.right
          val ev = leftOf(v)
          if (ev.aux.t == VertexType.JOIN) {
            addSegment(v, ev.aux)
          }
          ev.aux = v

        case VertexType.NORMAL =>
          if (v.y < v.prev.y) { // ???
            val eg = v.right
            val ed = v.left
            if (eg.aux.t == VertexType.JOIN) {
              addSegment(v, eg.aux)
            }
            active remove eg
            ed.aux = v
            active add ed
          } else {
            val ev = leftOf(v)
            if (ev.aux.t == VertexType.JOIN) {
              addSegment(v, ev.aux)
            }
            ev.aux = v
          }
      }
    }

  }

}