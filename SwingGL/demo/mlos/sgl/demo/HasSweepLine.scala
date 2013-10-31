package mlos.sgl.demo

import mlos.sgl.canvas.CanvasSegment
import mlos.sgl.view.Painter
import mlos.sgl.core.Transform
import java.awt.Graphics2D
import mlos.sgl.view.Drawer
import java.awt.Color
import mlos.sgl.core.Geometry
import mlos.sgl.core.Rect
import mlos.sgl.core.Segment

trait HasSweepLine extends AbstractVisualizer {
  val sweepLine = new CanvasSegment
  
  sweepLine setDashed true
  sweepLine setThickness 1
  
  val painter = new Painter {
    def paint(t: Transform, gfx: Graphics2D) {
      val d = new Drawer(gfx, t)
      d color(sweepLine.getColor()) dashed(sweepLine.getThickness(), 3, 4)
      val toNorm = scene.getView().planeToNorm()
      
      val a = sweepLine.getSegment().a
      val b = sweepLine.getSegment().b
      val ta = toNorm apply a
      val tb = toNorm apply b
      val s = Geometry.clipLine(ta, tb, Rect.aroundOrigin(1))
      val sPlane = new Segment(toNorm invert s.a, toNorm invert s.b)
      d line(sPlane)
    }
  }
  
  def showSweepLine() = scene.getView().addPrePainter(painter)
  def hideSweepLine() = scene.getView().removePrePainter(painter)
  
}