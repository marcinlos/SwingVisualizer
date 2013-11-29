package mlos.sgl.demo.delaunay

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
import mlos.sgl.core.Vec2d
import mlos.sgl.util.Randomizer
import mlos.sgl.core.Rect
import mlos.sgl.core.Geometry

class DelaunayScene(s: String) extends AlgorithmScene(s) { self =>

  handlerStack pushBack new InputAdapter {
    override def keyPressed(e: KeyEvent) {
      async {
        e.getKeyCode match {
          case KeyEvent.VK_F5 =>
            val alg = prepareDelounay()
            alg.run(points map { _.getPoint } toSeq, _.findByWalk)
          case KeyEvent.VK_F6 =>
            val alg = prepareDelounay()
            alg.run(points map { _.getPoint } toSeq, _.findByHistory)
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
    new Delaunay(new DelaunayVisualizer(self))
  }

  def points = extract(classOf[CanvasPoint])

  def this(s: String, ps: Iterable[Vec2d]) = {
    this(s)
    for (p <- ps) {
      addPoint(p)
    }
  }

}

object DelaunayScene extends scala.App {

  def onSegment(n: Int, a: Vec2d, b: Vec2d) =
    Range.Double(0, 1 + 1.0 / n, 1.0 / n) map { Geometry.lerp(_, a, b) }

  def squareBorder(n: Int) = {
    val r = Rect.aroundOrigin(1)
    val left = onSegment(n, r.leftTop, r.leftBottom)
    val right = onSegment(n, r.rightTop, r.rightBottom)
    val top = onSegment(n, r.leftTop, r.rightTop)
    val bottom = onSegment(n, r.leftBottom, r.rightBottom)
    Iterable.concat(left, right, top, bottom)
  }

  App.create(
    new DelaunayScene("Free"),
    new DelaunayScene("Square", squareBorder(10)))
}