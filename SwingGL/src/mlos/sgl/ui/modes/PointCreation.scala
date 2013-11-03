package mlos.sgl.ui.modes

import java.awt.event.MouseEvent

import javax.swing.JPanel
import mlos.sgl.Scene
import mlos.sgl.canvas.CanvasPoint

class PointCreation(s: Scene) extends AbstractMode("Point creation", s) {

  override val getOptionPanel = new JPanel
  
  override def mouseClicked(e: MouseEvent) {
    val pos = getPlanePos(e)
    val point = new CanvasPoint(pos)
    scene.addObject(point)
  }

}