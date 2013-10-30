package mlos.sgl.demo.monotonic

import mlos.sgl.demo.AbstractVisualizer
import mlos.sgl.Scene
import mlos.sgl.core.Vec2d
import java.awt.Color

class SplitVisualizer(s: Scene) extends AbstractVisualizer(s)
  with Splitter#EventListener
  with CanSignalPoint
  with CanSignalSegment {

  override def next(v: Vec2d) {
    signalPoint(v, Color.red)
    delay(500)
  }

  def check(v: Vec2d, pNext: Vec2d, qNext: Vec2d, pPrev: Vec2d, qPrev: Vec2d) {
    signalPoint(v, Color.red)
    delay(200)
    signalSegment(pPrev, qPrev, Color.green)
    delay(200)
    signalSegment(pNext, qNext, Color.blue)
    delay(200)
  }
  
  override def segment(p: Vec2d, q: Vec2d) {
    signalSegment(p, q, Color.red)
    delay(800)
  }

}