package mlos.sgl.demo.delounay

import mlos.sgl.App
import mlos.sgl.Scene
import scala.collection.JavaConversions._
import mlos.sgl.canvas.CanvasPolygon
import mlos.sgl.core.Polygon
import mlos.sgl.canvas.CanvasPoint
import mlos.sgl.AlgorithmScene
import mlos.sgl.ui.InputHandler
import mlos.sgl.ui.InputAdapter
import java.awt.event.KeyEvent

class DelounayScene(s: String) extends AlgorithmScene(s) {

  val handler = new InputAdapter {
    override def keyPressed(e: KeyEvent) {
      async {
        e.getKeyCode match {
          case KeyEvent.VK_F5 =>
          case KeyEvent.VK_F6 =>
        }
      }
    }
  }
  
  def points = extract(classOf[CanvasPoint]) map { _.getPoint }
  
}

object DelounayScene extends scala.App {

  App.create(new DelounayScene("Free"))

}