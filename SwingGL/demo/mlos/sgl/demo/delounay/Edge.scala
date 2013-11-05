package mlos.sgl.demo.delounay

sealed trait Edge { 
  def vertex: Vertex
  def opposite: Vertex
  
  def next: Edge
  def prev: Edge
}

case object Eab extends Edge { 
  val vertex = Va
  val opposite = Vc
  val next = Ebc
  val prev = Eca
}

case object Ebc extends Edge { 
  val vertex = Vb
  val opposite = Va
  val next = Eca
  val prev = Eab
}

case object Eca extends Edge { 
  val vertex = Vc 
  val opposite = Vb
  val next = Eab
  val prev = Ebc
}

object Edge {
  val all = List(Eab, Ebc, Eca)
}
