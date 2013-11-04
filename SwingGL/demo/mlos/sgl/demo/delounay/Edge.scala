package mlos.sgl.demo.delounay

sealed trait Edge { 
  def vertex: Vertex
  def opposite: Vertex
}

case object Ea extends Edge { 
  val vertex = Va
  val opposite = Vc
}

case object Eb extends Edge { 
  val vertex = Vb
  val opposite = Va
}

case object Ec extends Edge { 
  val vertex = Vc 
  val opposite = Vb
}

object Edge {
  val all = List(Ea, Eb, Ec)
}
