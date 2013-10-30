package mlos.sgl.demo.monotonic

import org.w3c.dom.views.AbstractView
import mlos.sgl.demo.AbstractVisualizer
import mlos.sgl.canvas.CanvasSegment
import mlos.sgl.core.Vec2d
import java.awt.Color
import mlos.sgl.core.Segment

trait CanSignalSegment extends AbstractVisualizer {

  val focusSegment = new CanvasSegment

  def showFocusSegment() = scene addObject focusSegment
  def hideFocusSegment() = scene removeObject focusSegment

  def signalSegment(s: Segment, c: Color) {
    focusSegment setSegment s
    focusSegment setColor c
    focusSegment setThickness 3
    focusSegment setZ 0.25
    showFocusSegment()
    focusSegment signalUpdate ()
    delay(200)
    focusSegment setThickness 2
    focusSegment signalUpdate ()
  }

  def signalSegment(p: Vec2d, q: Vec2d, c: Color) {
    signalSegment(new Segment(p, q), c)
  }

}