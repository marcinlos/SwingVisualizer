package mlos.sgl.util

import java.awt.{ Color => JColor }

object Color {

  type Triple[T] = Tuple3[T, T, T]
  
  private def rgbComps(h: Double, v: Double, s: Double): Triple[Double] = {
    val H = h * 3 / math.Pi
    val c = v * s
    val x = c * (1 - math.abs(H % 2 - 1))
    if (H < 1) (c, x, 0)
    else if (H < 2) (x, c, 0)
    else if (H < 3) (0, c, x)
    else if (H < 4) (0, x, c)
    else if (H < 5) (x, 0, c)
    else (c, 0, x)
  }

  def hsv2rgb(h: Double, v: Double, s: Double, a: Double = 1): JColor = {
    val (r, g, b) = rgbComps(h, v, s)
    val m = v * (1 - s)
    val (rr, gg, bb) = ((r + m).toFloat, (g + m).toFloat, (b + m).toFloat)
    return new JColor(rr, gg, bb, a.toFloat)
  }

}