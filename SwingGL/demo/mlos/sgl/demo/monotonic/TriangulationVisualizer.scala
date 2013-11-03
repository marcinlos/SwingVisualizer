package mlos.sgl.demo.monotonic

import java.awt.Color
import scala.collection.JavaConversions._
import scala.collection.JavaConversions
import scala.collection.immutable.Traversable
import scala.collection.mutable.Stack
import mlos.sgl.Scene
import mlos.sgl.canvas.CanvasPoint
import mlos.sgl.canvas.CanvasPolygon
import mlos.sgl.canvas.CanvasSegment
import mlos.sgl.core.Segment
import mlos.sgl.core.Vec2d
import mlos.sgl.demo.AbstractVisualizer
import mlos.sgl.demo.HasHorizontalSweepLine
import mlos.sgl.util.{ Color => HSV }
import scala.collection.immutable.Range
import scala.util.Random
import mlos.sgl.demo.CanSignalPoint

class TriangulationVisualizer(s: Scene) extends AbstractVisualizer(s)
  with Triangulate#EventListener 
  with CanSignalPoint
  with HasHorizontalSweepLine {
  
  setSpeed(2)

  val stack = new Stack[CanvasPoint]
  
  def nextColor(): Color = {
    val h = Random.nextDouble * 2 * math.Pi
    return HSV.hsv2rgb(h, 1, 1, 0.7)
  }

  def addPoint(v: Vec2d, c: Color, z: Double = 0.4) {
    val p = new CanvasPoint(v, z)
    p setColor c
    scene addObject p
  }

  override def foundInit(v: Vec2d) {
    signalPoint(v, Color.green)
    addPoint(v, Color.green)
  }

  override def foundFinal(v: Vec2d) {
    signalPoint(v, Color.red)
    addPoint(v, Color.red)
  }

  def signalAll(vs: Traversable[Vec2d], col: Color) {
    for (v <- vs) {
      signalPoint(v, col)
      addPoint(v, col)
    }
  }

  override def foundLeft(vs: Traversable[Vec2d]) {
    signalAll(vs, Color.cyan)
  }
  
  override def foundRight(vs: Traversable[Vec2d]) {
    signalAll(vs, Color.magenta)
  } 

  override def start() {
    showSweepLine()
    delay(1500)
  }

  override def push(v: Vec2d) {
    signalPoint(v, Color.red)
    val p = new CanvasPoint(v, 0.15)
    p setColor Color.red
    p setSize 14
    scene addObject p
    stack push p
    delay(1000)
  }

  override def pop() {
    val p = stack.pop()
    scene removeObject p
    refresh()
    delay(1000)
  }

  override def next(v: Vec2d) {
    signalPoint(v, Color.blue)
    moveLine(v.y)
    delay(500)
  }

  override def addSegment(a: Vec2d, b: Vec2d) {
    val col = Color.red
    val s = new CanvasSegment(new Segment(a, b))
    s setThickness 2
    s setColor col
    s setZ 0.45
    scene addObject s
    refresh()
    delay(1000)
  }
  
  override def addTriangle(a: Vec2d, b: Vec2d, c: Vec2d) {
    val poly = new CanvasPolygon(List(a, b, c))
    poly setFillColor nextColor
    poly setOpaque true
    poly setThickness 1
    scene addObject poly
  }
  
  override def finished() {
    stack foreach scene.removeObject
    stack.clear()
    hideFocusPoint()
    hideSweepLine()
  }

}