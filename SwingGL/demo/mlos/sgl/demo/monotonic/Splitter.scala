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

class Splitter(
  poly: Polygon,
  types: Seq[VertexType],
  listener: Splitter#EventListener) {

  type EventListener = SplitterListener

  def connect(p: Vertex, q: Vertex) = listener.segment(p.v, q.v)

  class Vertex(
    val v: Vec2d,
    val t: VertexType,
    val left: Edge,
    val right: Edge,
    val ord: Int
  ) {
    def x = v.x
    def y = v.y
    def next = left.q
    def prev = right.p
  }

  class Edge(var p: Vertex, var q: Vertex, var helper: Vertex = null) {
    def aux = helper
    def aux_=(v: Vertex) {
      helper = v
      val pos = if (v != null) v.v else null
      listener.changeHelper(p.v, q.v, pos)
    }
  }

  private val vs: IndexedSeq[Vec2d] = Array(poly.vs: _*)
  private def N = vs.length

  def buildEventQueue: PriorityQueue[Vertex] = {
    val order = Ordering.by { (p: Vertex) => (p.v.y, -p.ord) }
    val m = vs.maxBy((v: Vec2d) => (v.y, v.x))
    val maxIdx = vs.indexOf(m)
    val events = new PriorityQueue[Vertex]()(order)

    var first = new Edge(null, null)
    var prev = first
    var v: Vertex = null;

    for (k <- 0 until N - 1) yield {
      val idx = (maxIdx + k) % N
      val next = new Edge(null, null)
      v = new Vertex(vs(idx), types(idx), next, prev, k)
      events enqueue v
      prev.q = v
      next.p = v
      prev = next
    }
    val lastIdx = (maxIdx + N - 1) % N
    v = new Vertex(vs(lastIdx), types(lastIdx), first, prev, N - 1)
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

  def addActiveEdgee(e: Edge) {
    active add e
    listener addActive (e.p.v, e.q.v)
  }

  def removeActiveEdge(e: Edge) {
    e.aux = null
    if (active remove e) {
      listener removeActive (e.p.v, e.q.v)
    } else {
      println("fuck")
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

    def onInitial(v: Vertex) = {
      addActiveEdgee(v.left)
      v.left.aux = v
    }

    def onFinal(v: Vertex) = {
      if (v.right.aux.t == VertexType.JOIN) {
        connect(v, v.right.aux)
      }
      removeActiveEdge(v.right)
    }

    def onSplit(v: Vertex) = {
      val ev = leftOf(v)
      connect(v, ev.aux)
      ev.aux = v
      onInitial(v)
    }

    def updateLeft(v: Vertex) {
      val ev = leftOf(v)
      if (ev.aux.t == VertexType.JOIN) {
        connect(v, ev.aux)
      }
      ev.aux = v
    }

    def onJoin(v: Vertex) {
      onFinal(v)
      updateLeft(v)
    }

    while (!events.isEmpty) {
      val v = nextEvent()

      v.t match {
        case VertexType.INITIAL => onInitial(v)
        case VertexType.FINAL => onFinal(v)
        case VertexType.SPLIT => onSplit(v)
        case VertexType.JOIN => onJoin(v)

        case VertexType.NORMAL =>
          if (v.prev.y > v.y && v.y > v.next.y) {
            onFinal(v)
            onInitial(v)
          } else if (v.prev.y < v.y && v.y < v.next.y) {
            updateLeft(v)
          } else {
            if (v.y == v.prev.y) {

            } else if (v.y == v.next.y) {

            } else {

            }
            //            if (v.y > v.next.y) { // :(((((
            //              if (v.right.aux != null) {
            //                onInitial(v)
            //                onFinal(v) //removeActiveEdge(v.right)
            //              }
            //            } else if (v.y < v.next.y) {
            //              if (v.right.aux != null) {
            //                onFinal(v)
            //              } else {
            //                updateLeft(v)
            //                //              onFinal(v)
            //              }
            //            } else {
            //              onFinal(v)
            //              onInitial(v)
            //            }
          }
      }
    }
    listener finished ()
  }

}