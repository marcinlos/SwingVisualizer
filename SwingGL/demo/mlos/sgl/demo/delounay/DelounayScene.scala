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
import java.awt.Color

class DelounayScene(s: String) extends AlgorithmScene(s) { self =>

  handlerStack pushBack new InputAdapter {
    override def keyPressed(e: KeyEvent) {
      async {
        e.getKeyCode match {
          case KeyEvent.VK_F5 =>
            val alg = prepareDelounay()
            alg.run(points map { _.getPoint } toSeq, alg.findByWalk)
          case KeyEvent.VK_F16 =>
            val alg = prepareDelounay()
            alg.run(points map { _.getPoint } toSeq, alg.findByHistory)
          case _ =>
        }
      }
    }
  }

  def prepareDelounay() = {
    val ps = points
    ps foreach { p =>
      p.setColor(Color.gray)
      p.setSize(4)
      p.setZ(0.9)
    }
    new Delounay(new DelounayVisualizer(self))
  }

  def points = extract(classOf[CanvasPoint])

}

object DelounayScene extends scala.App {
  App.create(new DelounayScene("Free"))
}