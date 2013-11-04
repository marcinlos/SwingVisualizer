package mlos.sgl.demo.delounay

import java.awt.Color
import java.util.HashMap
import scala.collection.JavaConversions.seqAsJavaList
import mlos.sgl.Scene
import mlos.sgl.canvas.CanvasPolygon
import mlos.sgl.core.Vec2d
import mlos.sgl.demo.AbstractVisualizer
import mlos.sgl.demo.CanSignalPoint
import mlos.sgl.util.{ Color => HSV }
import scala.util.Random
import mlos.sgl.core.Polygon
import scala.collection.JavaConversions._
import scala.collection.mutable.Stack
import mlos.sgl.canvas.CanvasSegment
import mlos.sgl.core.Geometry
import mlos.sgl.core.Segment

class DelounayVisualizer(s: Scene) extends AbstractVisualizer(s)
  with Delounay#Listener
  with CanSignalPoint {

  setSpeed(50)
  
  type Tri = (Vec2d, Vec2d, Vec2d)

  val triangles = new HashMap[Tri, CanvasPolygon]

  val path = new Stack[CanvasSegment]
  var prev: Vec2d = null

  def nextColor(alpha: Double = 0.3): Color = {
    val h = Random.nextDouble * 2 * math.Pi
    return HSV.hsv2rgb(h, 1, 1, alpha)
  }

  override def triangle(t: Triangle) {
    val (a, b, c) = t.points
    val vs = List(a, b, c)
    val poly = new CanvasPolygon(vs)
    poly.setOpaque(true)
    poly.setFillColor(nextColor())
    poly.setThickness(1)
    scene.addObject(poly)
    triangles.put((a, b, c), poly)
    refresh()
    delay(800)
    refresh()
  }

  override def point(v: Vec2d) {
    signalPoint(v, Color.red)
    delay(200)
  }

  def signalPoly(a: Traversable[Vec2d], c: Color) {
    val p = new CanvasPolygon(a.toSeq)
    p.setOpaque(true)
    p.setFillColor(c)
    p.setZ(0.3)
    scene.addObject(p)
    refresh()
    delay(300)
    scene.removeObject(p)
    refresh()
  }

  def signalTriangle(t: Triangle, c: Color) {
    val (p, q, r) = t.points
    signalPoly(List(p, q, r), c)
  }

  override def foundContaining(v: Vec2d, t: Triangle) {
    lineTo(v)
    val (a, b, c) = t.points
    signalPoly(List(a, b, c), Color.green)
    path foreach scene.removeObject
    path.clear()
    prev = null
    delay(500)
  }

  def lineTo(p: Vec2d) {
    val s = new CanvasSegment(new Segment(prev, p))
    s.setDashed(true)
    s.setZ(0.2)
    scene.addObject(s)
    refresh()
    path.push(s)
    delay(500)
  }

  override def nextHop(t: Triangle) {
    val (a, b, c) = t.points
    val center = Geometry.center(a, b, c)
    if (prev != null) {
      lineTo(center)
    }
    prev = center
  }

  override def break(t: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, tc: Triangle) {
    val p = new CanvasPolygon(t.pointSeq)
    p.setThickness(3)
    p.setBorderColor(Color.red)
    p.setZ(0.15)
    scene.addObject(p)
    refresh()
    delay(300)
    val poly = triangles.remove(t.points)
    scene.removeObject(poly)
    List(ta, tb, tc) foreach triangle
    scene.removeObject(p)
    refresh()
  }

  override def swap(p: Triangle, q: Triangle) {
    val pobj = triangles.remove(p.points)
    val qobj = triangles.remove(q.points)

    pobj.setFillColor(Color.cyan)
    qobj.setFillColor(Color.cyan)
    refresh()
    delay(1000)
    scene.removeObject(pobj)
    scene.removeObject(qobj)
    refresh()
  }
}