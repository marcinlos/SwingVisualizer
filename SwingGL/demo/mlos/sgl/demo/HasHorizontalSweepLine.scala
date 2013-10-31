package mlos.sgl.demo

import mlos.sgl.core.Segment
import mlos.sgl.core.Vec2d

trait HasHorizontalSweepLine extends HasSweepLine {
  
  def moveLine(y: Double) {
    val s = new Segment(new Vec2d(-1, y), new Vec2d(1, y))
    sweepLine setSegment s
  }
  
}