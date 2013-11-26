package mlos.sgl.demo.delaunay

sealed trait Edge { 
  def start: Vertex
  def end: Vertex
  def opposite: Vertex
  
  def next: Edge
  def prev: Edge
}

case object Eab extends Edge { 
  val start = Va
  val end = Vb
  val opposite = Vc
  val next = Ebc
  val prev = Eca
}

case object Ebc extends Edge { 
  val start = Vb
  val end = Vc
  val opposite = Va
  val next = Eca
  val prev = Eab
}

case object Eca extends Edge { 
  val start = Vc
  val end = Va
  val opposite = Vb
  val next = Eab
  val prev = Ebc
}

object Edge {
  val all = List(Eab, Ebc, Eca)
}
