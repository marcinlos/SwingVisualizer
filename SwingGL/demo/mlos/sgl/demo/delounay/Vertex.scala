package mlos.sgl.demo.delounay

sealed trait Vertex {
  def next: Vertex
  def prev: Vertex
  def edge: Edge
}

case object Va extends Vertex { 
  val next = Vb
  val prev = Vc
  val edge = Ea
}

case object Vb extends Vertex { 
  val next = Vc
  val prev = Va
  val edge = Eb
}

case object Vc extends Vertex { 
  val next = Va
  val prev = Vb
  val edge = Ec
}

