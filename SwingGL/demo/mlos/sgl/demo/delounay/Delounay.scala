package mlos.sgl.demo.delounay

import scala.collection.mutable.HashSet
import scala.collection.mutable.Queue
import mlos.sgl.core.Geometry
import mlos.sgl.core.Rect
import mlos.sgl.core.Vec2d
import scala.annotation.tailrec
import mlos.sgl.core.Segment

trait DelounayListener {
  def point(a: Vec2d)
  def nextHop(t: Triangle)
  def foundContaining(v: Vec2d, t: Triangle)
  def beginFixup()
  def testCircle(t: Triangle, n: Triangle, v: Vertex)
  def break(t: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, tc: Triangle)
  def breakEdge(t: Triangle, s: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, sa: Triangle, sb: Triangle)
  def visit(t: Triangle)
  def flip(p: Triangle, q: Triangle)
  def endFixup()
  def triangle(t: Triangle)
  def finished()
}

class Delounay(listener: Delounay#Listener) {

  type Listener = DelounayListener

  var root1: Triangle = null
  var root2: Triangle = null

  var init: Triangle = null

  def run(points: Seq[Vec2d], findFunc: Delounay => Vec2d => Triangle) {
    val bounds = Rect.scale(Geometry.aabb(points: _*), 1.2, 1.2)
    val lb = bounds.leftBottom
    val lt = bounds.leftTop
    val rb = bounds.rightBottom
    val rt = bounds.rightTop

    root1 = Triangle(lb, rb, rt)
    root2 = Triangle(rt, lt, lb)
    root2.connect(Eca, root1)

    listener.triangle(root1)
    listener.triangle(root2)

    init = root1
    points foreach add(findFunc(this))
    listener.finished()
  }

  def findByWalk(v: Vec2d): Triangle = {
    val visited = new HashSet[Triangle]

    @tailrec
    def visit(t: Triangle): Triangle = {
      listener.nextHop(t)
      if (t.contains(v)) {
        listener.foundContaining(v, t)
        t
      } else {
        val d = Geometry.diff(v, t.center)
        val diff = new Segment(v, t.center)
        def worstDir(e: Edge) = -Geometry.dot(d, t.normal(e))

        var found: Triangle = null
        Edge.all takeWhile (_ => found == null) foreach { e =>
          val p = t(e.start)
          val q = t(e.end)
          if (Geometry.properIntersect(new Segment(p, q), diff)) {
            found = t(e)
          }
        }
        visit(found)
      }
    }
    visit(init)
  }

  def findByHistory(v: Vec2d): Triangle = {

    def visit(t: Triangle): Triangle = {
      listener.nextHop(t)
      if (!t.children.isEmpty) {
        val Some(triangle) = t.children.find(_.contains(v))
        visit(triangle)
      } else {
        listener.foundContaining(v, t)
        return t
      }
    }
    val Some(first) = List(root1, root2).find(_.contains(v))
    visit(first)
  }

  def add(find: Vec2d => Triangle)(v: Vec2d) {
    listener.point(v)
    val t = find(v)

    if (t.inside(v)) {
      val ta = Triangle(v, t.a, t.b)
      val tb = Triangle(v, t.b, t.c)
      val tc = Triangle(v, t.c, t.a)

      ta.connect(tc, t(Eab), tb)
      tb.connect(ta, t(Ebc), tc)
      tc.connect(tb, t(Eca), ta)

      t.children = List(ta, tb, tc)

      listener.break(t, v, ta, tb, tc)
      if (init eq t)
        init = ta

      fix(ta, tb, tc)
    } else {
      val et = t.containingEdge(v)
      val n = t(et)
      val en = n.containingEdge(v)

      val ta = Triangle(t(et.opposite), v, t(et.end))
      val tb = Triangle(t(et.opposite), t(et.start), v)

      val na = Triangle(n(en.opposite), v, n(en.end))
      val nb = Triangle(n(en.opposite), n(en.start), v)

      ta.connect(tb, nb, t(et.next))
      tb.connect(t(et.prev), na, ta)
      na.connect(nb, tb, n(en.next))
      nb.connect(n(en.prev), ta, na)

      t.children = List(ta, tb)
      n.children = List(na, nb)
      listener.breakEdge(t, n, v, ta, tb, na, nb)

      if (init eq t)
        init = ta
      if (init eq n)
        init = na
      fix(ta, tb, na, nb)
    }
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
        trySwap(t.na) ||
      trySwap(t.nb) ||
          trySwap(t.nc)
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

    val flipped = List(r, s)
    p.children = flipped
    q.children = flipped

    listener.flip(p, q)
    listener.triangle(r)
    listener.triangle(s)

    if (init == p || init == q)
      init = s

    return (r, s)
  }

}