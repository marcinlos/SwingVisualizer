package mlos.sgl.demo

import mlos.sgl.ui.InputAdapter
import java.awt.event.MouseWheelEvent
import java.awt.event.KeyEvent

trait Steppable extends AbstractVisualizer {

  scene.getHandlerStack().push(new InputAdapter {
    override def keyPressed(e: KeyEvent) {
      e.getKeyCode match {
        case KeyEvent.VK_SPACE => step()
        case KeyEvent.VK_Z => start()
        case KeyEvent.VK_X => pause()
        case _ =>
      }
    }
  })

}