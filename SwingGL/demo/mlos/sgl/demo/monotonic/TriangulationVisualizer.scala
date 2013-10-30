package mlos.sgl.demo.monotonic

import java.awt.Color
import scala.collection.JavaConversions._
import scala.collection.mutable.Stack
import mlos.sgl.Scene
import mlos.sgl.canvas.CanvasPoint
import mlos.sgl.canvas.CanvasSegment
import mlos.sgl.core.Segment
import mlos.sgl.core.Vec2d
import mlos.sgl.demo.AbstractVisualizer
import scala.collection.immutable.Traversable
import scala.collection.JavaConversions

class TriangulationVisualizer(s: Scene) extends AbstractVisualizer(s)
  with TriangulateJ.EventListener 
  with Triangulate#EventListener 
  with CanSignalPoint {
  
  setSpeed(2)

  val stack = new Stack[CanvasPoint]

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

  type JavaList[T] = java.util.List[T]

  override def foundLeft(vs: JavaList[Vec2d]) {
    signalAll(vs.to[Traversable], Color.cyan)
  }

  override def foundRight(vs: JavaList[Vec2d]) {
    signalAll(vs.to[Traversable], Color.magenta)
  }
  
  override def foundLeft(vs: Traversable[Vec2d]) {
    signalAll(vs, Color.cyan)
  }
  override def foundRight(vs: Traversable[Vec2d]) {
    signalAll(vs, Color.magenta)
  } 

  override def start() = delay(1500)

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
    val p = stack.pop
    scene removeObject p
    refresh()
    delay(1000)
  }

  override def next(v: Vec2d) {
    signalPoint(v, Color.blue)
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

}