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
  def break(t: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, tc: Triangle)
  def flip(p: Triangle, q: Triangle)
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
    top.connect(Ec, root)

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

    na.connect(nc, t(Ea), nb)
    nb.connect(na, t(Eb), nc)
    nc.connect(nb, t(Ec), na)
    
    listener.break(t, v, na, nb, nc)
    if (root eq t)
      root = na
      
    fix(na, nb, nc)
  }

  private def fix(ts: Triangle*) {
    val visited = new HashSet[Triangle]
    val queue = new Queue[Triangle]
    queue.enqueue(ts: _*)
    
    def incircle(t: Triangle, v: Vec2d) = 
      Geometry.incircle(t.a, t.b, t.c, v) > 0

    while (!queue.isEmpty) {
      val t = queue.dequeue()
      if (visited.add(t)) {

        def trySwap(n: Triangle): Boolean = {
          var split = false
          if (n != null) {
            val v = (n commonEdge t).opposite
            if (incircle(t, n(v))) {
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
  }

  private def flip(p: Triangle, q: Triangle): (Triangle, Triangle) = {

    val vp = (p commonEdge q).opposite
    val vq = (q commonEdge p).opposite

    val r = Triangle(p(vp), p(vp.next), q(vq))
    val s = Triangle(q(vq), q(vq.next), p(vp))

    r.connect(p(vp.edge), q(vq.prev.edge), s)
    s.connect(q(vq.edge), p(vp.prev.edge), r)
    
    listener.flip(p, q)
    listener.triangle(r)
    listener.triangle(s)

    if (root == p || root == q)
      root = s
      
    return (r, s)
  }

}