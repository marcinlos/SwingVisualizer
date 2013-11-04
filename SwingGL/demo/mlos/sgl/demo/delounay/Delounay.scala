package mlos.sgl.demo.delounay

import scala.collection.mutable.HashSet
import scala.collection.mutable.Queue

import Edge._
import Vertex._
import mlos.sgl.core.Geometry
import mlos.sgl.core.Rect
import mlos.sgl.core.Vec2d

trait DelounayListener {
  def point(a: Vec2d)
  def nextHop(t: Triangle)
  def foundContaining(v: Vec2d, t: Triangle)
  def break(t: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, tc: Triangle)
  def swap(p: Triangle, q: Triangle)
  def triangle(t: Triangle)
}

class Delounay(listener: Delounay#Listener) {

  type Listener = DelounayListener

  var root: Triangle = null

  def run(points: Seq[Vec2d]) {
    val bounds = Rect.scale(Geometry.aabb(points: _*), 1.1, 1.1)
    val lb = bounds.leftBottom
    val lt = bounds.leftTop
    val rb = bounds.rightBottom
    val rt = bounds.rightTop

    root = Triangle(lb, rb, rt, null, null, null)
    val top = Triangle(rt, lt, lb, null, null, root)
    root.nc = top

    listener.triangle(root)
    listener.triangle(top)

    points foreach add
  }

  def findTriangle(v: Vec2d): Triangle = {
    val visited = new HashSet[Triangle]

    def visit(t: Triangle): Triangle = {
      listener.nextHop(t)
      if (t.contains(v)) {
        listener.foundContaining(v, t)
        t
      } else {
        val diff = Geometry.diff(v, t.center)
        def worstDir(e: Edge) = -Geometry.dot(diff, t.normal(e))

        var found: Triangle = null
        val edges = Edge.values.toSeq sortBy (worstDir)
        edges.toStream takeWhile (_ => found == null) foreach { e =>
          val neighbour = t.n(e)
          if (neighbour != null && visited.add(neighbour))
            found = visit(neighbour)
        }
        found
      }
    }
    visit(root)
  }

  def add(v: Vec2d) {
    listener.point(v)
    val t = findTriangle(v)

    val na = Triangle(v, t.a, t.b, null, t.n(Ea), null)
    val nb = Triangle(v, t.b, t.c, null, t.n(Eb), null)
    val nc = Triangle(v, t.c, t.a, null, t.n(Ec), null)

    na(Ea) = nc
    na(Ec) = nb
    nb(Ea) = na
    nb(Ec) = nc
    nc(Ea) = nb
    nc(Ec) = na
    listener.break(t, v, na, nb, nc)

    if (na.n(Eb) != null)
      na.n(Eb).replaceNeighbour(na, t)
    if (nb.n(Eb) != null)
      nb.n(Eb).replaceNeighbour(nb, t)
    if (nc.n(Eb) != null)
      nc.n(Eb).replaceNeighbour(nc, t)

    if (root eq t)
      root = na
      
    fix(na, nb, nc)
  }

  def fix(ts: Triangle*) {
    val visited = new HashSet[Triangle]
    val queue = new Queue[Triangle]
    queue.enqueue(ts: _*)

    while (!queue.isEmpty) {
      val t = queue.dequeue()
      if (visited.add(t)) {

        def trySwap(n: Triangle): Boolean = {
          var split = false
          if (n != null) {
            val v = opposite(n adjacentBy t)
            if (Geometry.incircle(t.a, t.b, t.c, n(v)) > 0) {
              val (r, s) = swapDiagonals(t, n)
              queue.enqueue(r, s)
              split = true
            }
          }
          split
        }
        trySwap(t.na) || trySwap(t.nb) || trySwap(t.nc)
      }
    }
  }

  def opposite(e: Edge) = e match {
    case Ea => Vc
    case Eb => Va
    case Ec => Vb
  }

  def swapDiagonals(p: Triangle, q: Triangle): (Triangle, Triangle) = {

    val ep = p.adjacentBy(q)
    val eq = q.adjacentBy(p)
    val vp = opposite(ep)
    val vq = opposite(eq)

    val r = Triangle(p(vp), p(next(vp)), q(vq), null, null, null)
    val s = Triangle(q(vq), q(next(vq)), p(vp), null, null, null)

    r(Ea) = p.n(edgeOf(vp))
    r(Eb) = q.n(edgeOf(prev(vq)))
    r(Ec) = s

    s(Ea) = q.n(edgeOf(vq))
    s(Eb) = p.n(edgeOf(prev(vp)))
    s(Ec) = r

    if (r.n(Ea) != null) r.n(Ea).replaceNeighbour(r, p)
    if (r.n(Eb) != null) r.n(Eb).replaceNeighbour(r, q)
    if (s.n(Ea) != null) s.n(Ea).replaceNeighbour(s, q)
    if (s.n(Eb) != null) s.n(Eb).replaceNeighbour(s, p)

    listener.swap(p, q)
    listener.triangle(r)
    listener.triangle(s)

    if ((root eq p) || (root eq q))
      root = s
      
    return (r, s)
  }

  def next(v: Vertex) = v match {
    case Va => Vb
    case Vb => Vc
    case Vc => Va
  }

  def vertexOf(e: Edge) = e match {
    case Ea => Va
    case Eb => Vb
    case Ec => Vc
  }

  def edgeOf(v: Vertex) = v match {
    case Va => Ea
    case Vb => Eb
    case Vc => Ec
  }

  def prev(v: Vertex) = v match {
    case Va => Vc
    case Vb => Va
    case Vc => Vb
  }

  //  def next(e: Edge) = e match {
  //    case Ea => Eb
  //    case Eb => Ec
  //    case Ec => Ea
  //  }
  //  
  //  def pref(e: Edge) = e match {
  //    case Ea => Ec
  //    case Eb => Ea
  //    case Ec => Eb
  //  }

}