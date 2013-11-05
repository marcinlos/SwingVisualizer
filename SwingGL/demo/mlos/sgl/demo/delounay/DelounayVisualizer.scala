package mlos.sgl.demo.delounay

import java.awt.Color
import java.awt.Graphics2D
import java.util.HashMap
import scala.collection.JavaConversions._
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.Stack
import scala.util.Random
import mlos.sgl.Scene
import mlos.sgl.canvas.CanvasPolygon
import mlos.sgl.canvas.CanvasSegment
import mlos.sgl.core.Geometry
import mlos.sgl.core.Segment
import mlos.sgl.core.Transform
import mlos.sgl.core.Vec2d
import mlos.sgl.demo.AbstractVisualizer
import mlos.sgl.demo.CanSignalPoint
import mlos.sgl.util.{ Color => HSV }
import mlos.sgl.view.Drawer
import mlos.sgl.view.Painter
import mlos.sgl.demo.HasAdjustableSpeed

class DelounayVisualizer(s: Scene) extends AbstractVisualizer(s)
  with Delounay#Listener
  with HasAdjustableSpeed
  with CanSignalPoint {

  setSpeed(1)

  type Tri = (Vec2d, Vec2d, Vec2d)

  val triangles = new HashMap[Tri, CanvasPolygon]

  val path = new Stack[CanvasSegment]
  var prev: Vec2d = null

  val fixup = new Stack[CanvasPolygon]
  
  val faceCol = new Color(0, 1, 0, 0.5f)
  val testBaseCol = Color.yellow
  val testedFaceCol = Color.cyan
  
  override def triangle(t: Triangle) {
    val poly = new CanvasPolygon(t.pointSeq)
    poly.setOpaque(true)
    poly.setFillColor(faceCol)
    poly.setThickness(1)
    scene.addObject(poly)
    triangles.put(t.points, poly)
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
    delay(600)
    scene.removeObject(p)
    refresh()
  }

  def signalTriangle(t: Triangle, c: Color) = signalPoly(t.pointSeq, c)

  override def foundContaining(v: Vec2d, t: Triangle) {
    lineTo(v)
    signalPoly(t.pointSeq, Color.green)
    path foreach scene.removeObject
    path.clear()
    refresh()
    prev = null
    delay(500)
  }

  override def beginFixup() {

  }

  override def endFixup() {
    fixup foreach scene.removeObject
    fixup.clear()
    refresh()
    delay(500)
  }

  def circumscribed(t: Triangle): Vec2d = {
    val mab = Geometry.lerp(0.5, t.a, t.b)
    val mbc = Geometry.lerp(0.5, t.b, t.c)
    val mca = Geometry.lerp(0.5, t.c, t.a)
    val abSym = new Segment(mab, Geometry.sum(mab, t.normal(Eab)))
    val bcSym = new Segment(mbc, Geometry.sum(mbc, t.normal(Ebc)))
    Geometry.intersectionPoint(abSym, bcSym)
  }

  class CirclePainter(center: Vec2d, r: Double) extends Painter {
    def paint(t: Transform, g: Graphics2D) {
      val col = new Color(1, 0, 0, 0.4f)
      new Drawer(g, t)
        .color(Color.black)
        .solid(1)
        .circle(center, r)
        .color(col)
        .fillCircle(center, r)
        .restore()
    }
  }

  override def testCircle(t: Triangle, n: Triangle, v: Vertex) {
    val p = new CanvasPolygon(t.pointSeq)
    p.setOpaque(true)
    p.setFillColor(testBaseCol)
    p.setZ(0.3)
    p.setThickness(1)
    scene.addObject(p)
    refresh()
    delay(300)
    val center = circumscribed(t)
    signalPoint(center, Color.red)
    val painter = new CirclePainter(center, Geometry.dist(center, t.a))
    scene.getView().addPostPainter(painter)
    refresh()
    delay(1000)

    val other = new CanvasPolygon(n.pointSeq)
    other.setZ(0.25)
    other.setThickness(1)
    other.setOpaque(true)
    other.setFillColor(testedFaceCol)
    scene.addObject(other)

    signalPoint(n(v), Color.red)
    hideFocusPoint()
    delay(300)
    scene.removeObject(p)
    scene.removeObject(other)
    scene.getView().removePostPainter(painter)
    refresh()
  }

  def lineTo(p: Vec2d) {
    val s = new CanvasSegment(new Segment(prev, p))
    s.setDashed(true)
    s.setColor(Color.red)
    s.setZ(0.2)
    scene.addObject(s)
    refresh()
    path.push(s)
    delay(500)
  }

  override def nextHop(t: Triangle) {
    if (prev != null) {
      lineTo(t.center)
    }
    prev = t.center
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

  override def visit(t: Triangle) {

  }

  override def flip(p: Triangle, q: Triangle) {
    val pobj = triangles.remove(p.points)
    val qobj = triangles.remove(q.points)

    pobj.setFillColor(Color.red)
    qobj.setFillColor(Color.red)
    refresh()
    delay(1000)
    scene.removeObject(pobj)
    scene.removeObject(qobj)
    refresh()
  }

  override def finished() {
    hideFocusPoint()
  }

}