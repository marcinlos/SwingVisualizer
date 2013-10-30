package mlos.sgl.demo.monotonic

import java.awt.Color
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import mlos.sgl.Scene
import mlos.sgl.canvas.CanvasSegment
import mlos.sgl.core.Segment
import mlos.sgl.core.Vec2d
import mlos.sgl.demo.AbstractVisualizer
import mlos.sgl.canvas.CanvasPoint
import mlos.sgl.core.Geometry

class SplitVisualizer(s: Scene) extends AbstractVisualizer(s)
  with Splitter#EventListener
  with CanSignalPoint
  with CanSignalSegment {

  val line = new CanvasSegment
  val segments = new ArrayBuffer[CanvasSegment]
  val active = new HashMap[Segment, CanvasSegment]
  
  val helpers = new HashMap[Segment, (CanvasSegment, CanvasPoint)]
  
  line setDashed true
  line setThickness 1
  
  override def start(y: Double) {
    moveLine(y)
    scene addObject line
  }
  
  def cut(p: Vec2d, q: Vec2d, y: Double): Vec2d = {
    if (p.y != q.y) {
      val t = (y - p.y) / (q.y - p.y);
      return Geometry.lerp(t, p, q);
    } else {
      return p
    }
  }
  
  override def next(v: Vec2d) {
    signalPoint(v, Color.red)
    delay(500)
    hideFocusPoint()
  }

  override def segment(p: Vec2d, q: Vec2d) {
    signalSegment(p, q, Color.red)
    delay(800)
    
    val s = new CanvasSegment(new Segment(p, q))
    s setColor Color.red
    scene addObject s
    segments += s
  }
  
  override def moveLine(y: Double) {
//    for ((s, (seg, pt)) <- helpers) {
//      val v = cut(s.a, s.b, y)
//      seg setSegment new Segment(v, pt.getPoint)
//    }
    val x = 1e1
    val s = new Segment(new Vec2d(-x, y), new Vec2d(x, y))
    line setSegment s
    delay(500)
  }
  
  override def addActive(p: Vec2d, q: Vec2d) {
    val s = new Segment(p, q)
    signalSegment(s, Color.green)
    val cs = new CanvasSegment(s)
    cs setColor Color.green
    cs setZ 0.2
    scene addObject cs
    active put (s, cs)
    delay(1000)
  }
  
  override def removeActive(p: Vec2d, q: Vec2d) {
    val s = new Segment(p, q)
    active remove s match {
      case Some(cs) => scene removeObject cs
      case None => 
    }
    helpers remove s match {
      case Some((seg, pt)) =>
        scene removeObject seg
        scene removeObject pt
      case None =>
    }
    delay(1000)
  }
  
  def changeHelper(p: Vec2d, q: Vec2d, aux: Vec2d) {
    signalPoint(aux, Color.magenta)
    val pt = new CanvasPoint(aux)
    pt setColor Color.magenta
    pt setZ 0.12
    scene addObject pt
    
    val paux = cut(p, q, aux.y)
    signalSegment(paux, aux, Color.yellow)
    delay(300)
    hideFocusSegment()
    
    val s = new Segment(p, q)
    helpers remove s match {
      case Some((seg, pt)) =>
        scene removeObject seg
        scene removeObject pt
      case None =>
    }
    val seg = new CanvasSegment(new Segment(paux, aux))
    seg setColor Color.blue
    seg setDashed true
    seg setThickness 2
    seg setZ 0.15
    scene addObject seg
    
    helpers put (s, (seg, pt))
  }
  
  override def finished() {
//    delay(1000)
//    for (s <- segments) {
//      scene removeObject s
//    }
//    segments clear()
  }

}