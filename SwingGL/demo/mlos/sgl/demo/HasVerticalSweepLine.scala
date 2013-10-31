package mlos.sgl.demo

import mlos.sgl.core.Segment
import mlos.sgl.core.Vec2d

trait HasVerticalSweepLine extends HasSweepLine {
  
  def moveLine(x: Double) {
    val s = new Segment(new Vec2d(x, -1), new Vec2d(x, 1))
    sweepLine setSegment s
  }
  
}