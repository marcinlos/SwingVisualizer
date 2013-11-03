package mlos.sgl

import scala.collection.JavaConversions._
import scala.actors.threadpool.Executors
import mlos.sgl.ui.InputHandler

abstract class AlgorithmScene(name: String) extends Scene(name) {

  val pool = Executors.newSingleThreadExecutor()

  def async(action: => Unit) {
    pool.execute(new Runnable {
      override def run() = action
    })
  }

  def extract[T](c: Class[T]) = canvas.getObjects().collect {
    case p if c.isInstance(p) => p.asInstanceOf[T]
  }

}