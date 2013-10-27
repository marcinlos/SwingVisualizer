package mlos.sgl.demo.monotonic

import java.awt.Color

import mlos.sgl.Scene
import mlos.sgl.canvas.CanvasPoint
import mlos.sgl.core.Vec2d
import mlos.sgl.demo.AbstractVisualizer

class ClassificationVisualizer(s: Scene) extends AbstractVisualizer(s)
  with ClassifyVertices.EventListener {

  val focus: CanvasPoint = new CanvasPoint

  def showFocusPoint() = scene addObject focus
  def hideFocusPoint() = scene removeObject focus

  protected def signalPoint(p: Vec2d, c: Color) {
    focus setPoint p
    focus setColor c
    focus setBorderColor Color.black
    focus setBorderSize 1
    focus setSize 18
    focus setZ 0.2
    showFocusPoint
    focus signalUpdate ()
    delay(200)
    focus setSize 12
    focus signalUpdate ()
  }

  private def typeToColor(t: VertexType): Color = t match {
    case VertexType.FINAL => Color.red
    case VertexType.INITIAL => Color.green
    case VertexType.JOIN => Color.blue
    case VertexType.SPLIT => Color.cyan
    case VertexType.NORMAL => Color.darkGray
    case _ => throw new IllegalArgumentException("Invalid color")
  }

  override def start = Unit

  override def examining(v: Vec2d, prev: Vec2d, next: Vec2d) {
    signalPoint(prev, Color.blue)
    signalPoint(v, Color.red)
    signalPoint(next, Color.blue)
  }

  override def classified(v: Vec2d, t: VertexType) {
    val col = typeToColor(t)
    signalPoint(v, col)
    delay(800)

    val p = new CanvasPoint(v, 0.4)
    p setColor col
    scene addObject p
  }

  override def finished = hideFocusPoint

}