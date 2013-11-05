package mlos.sgl.demo.delounay

import scala.collection.mutable.HashSet
import scala.collection.mutable.Queue

import mlos.sgl.core.Geometry
import mlos.sgl.core.Rect
import mlos.sgl.core.Vec2d

trait DelounayListener {
  def point(a: Vec2d)
  def nextHop(t: Triangle)
  def foundContaining(v: Vec2d, t: Triangle)
  def beginFixup()
  def testCircle(t: Triangle, n: Triangle, v: Vertex)
  def break(t: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, tc: Triangle)
  def visit(t: Triangle)
  def flip(p: Triangle, q: Triangle)
  def endFixup()
  def triangle(t: Triangle)
  def finished()
}

class Delounay(listener: Delounay#Listener) {

  type Listener = DelounayListener

  var root: Triangle = null

  def run(points: Seq[Vec2d]) {
    val bounds = Rect.scale(Geometry.aabb(points: _*), 1.2, 1.2)
    val lb = bounds.leftBottom
    val lt = bounds.leftTop
    val rb = bounds.rightBottom
    val rt = bounds.rightTop

    root = Triangle(lb, rb, rt)
    val top = Triangle(rt, lt, lb)
    top.connect(Eca, root)

    listener.triangle(root)
    listener.triangle(top)

    points foreach add
    listener.finished()
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
        val edges = Edge.all.toSeq sortBy (worstDir)
        edges.toStream takeWhile (_ => found == null) foreach { e =>
          val neighbour = t(e)
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

    val na = Triangle(v, t.a, t.b)
    val nb = Triangle(v, t.b, t.c)
    val nc = Triangle(v, t.c, t.a)

    na.connect(nc, t(Eab), nb)
    nb.connect(na, t(Ebc), nc)
    nc.connect(nb, t(Eca), na)

    listener.break(t, v, na, nb, nc)
    if (root eq t)
      root = na

    fix(na, nb, nc)
  }

  private def fix(ts: Triangle*) {
    val visited = new HashSet[Triangle]
    val queue = new Queue[Triangle]
    queue.enqueue(ts: _*)

    def incircle(t: Triangle, n: Triangle, v: Vertex) = {
      listener.testCircle(t, n, v)
      Geometry.incircle(t.a, t.b, t.c, n(v)) > 0
    }

    listener.beginFixup()
    while (!queue.isEmpty) {
      val t = queue.dequeue()
      if (visited.add(t)) {
        listener.visit(t)

        def trySwap(n: Triangle): Boolean = {
          var split = false
          if (n != null) {
            val v = (n commonEdge t).opposite
            if (incircle(t, n, v)) {
              val (r, s) = flip(t, n)
              queue.enqueue(r, s)
              split = true
            }
          }
          split
        }
        trySwap(t.na) || trySwap(t.nb) || trySwap(t.nc)
      }
    }
    listener.endFixup()
  }

  private def flip(p: Triangle, q: Triangle): (Triangle, Triangle) = {
    val ep = p commonEdge q
    val eq = q commonEdge p
    val vp = ep.opposite
    val vq = eq.opposite

    val r = Triangle(p(vp), p(vp.next), q(vq))
    val s = Triangle(q(vq), q(vq.next), p(vp))

    r.connect(p(ep.prev), q(eq.next), s)
    s.connect(q(eq.prev), p(ep.next), r)

    listener.flip(p, q)
    listener.triangle(r)
    listener.triangle(s)

    if (root == p || root == q)
      root = s

    return (r, s)
  }

}