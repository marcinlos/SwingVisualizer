package mlos.sgl.demo

import mlos.sgl.ui.InputAdapter
import java.awt.event.MouseWheelEvent

trait HasAdjustableSpeed extends AbstractVisualizer {

  val rate = 1.1

  scene.getHandlerStack().push(new InputAdapter {
    override def mouseWheelMoved(e: MouseWheelEvent) {
      if (e.isShiftDown()) {
        val amount = e.getPreciseWheelRotation()
        val speed = Math.pow(rate, amount) * getSpeed()
        setSpeed(speed)
        e.consume()
        println(getSpeed())
        println(amount)
      }
    }
  })

}