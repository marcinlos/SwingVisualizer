package mlos.sgl.demo.delounay

import scala.collection.mutable.HashSet

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
  def triangle(t: Triangle)
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

    root = Triangle(lb, rb, rt, null, null, null)
    val top = Triangle(rt, lt, lb, null, null, root)
    root.nc = top

    listener.triangle(root)
    listener.triangle(top)

    points foreach add
  }

  def findTriangle(v: Vec2d): Triangle = {
    val visited = new HashSet[Triangle]
    
    def visit(t: Triangle):Triangle = {
      listener.nextHop(t)
      if (t.contains(v)) {
        listener.foundContaining(v, t)
        t
      } else {
        val diff = Geometry.diff(v, t.center)
        def worstDir(e: Edge) = - Geometry.dot(diff, t.normal(e))
        
        var found: Triangle = null
        val edges = Edge.values.toSeq.sortBy(worstDir)
        edges.toStream.takeWhile(_ => found == null).foreach { e =>
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

    if (na.n(Eb) != null) na.n(Eb).replaceNeighbour(na, t)
    if (nb.n(Eb) != null) nb.n(Eb).replaceNeighbour(nb, t)
    if (nc.n(Eb) != null) nc.n(Eb).replaceNeighbour(nc, t)

    if (root eq t)
      root = na
  }

}