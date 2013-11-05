package mlos.sgl.demo.delounay

import mlos.sgl.core.Geometry
import mlos.sgl.core.Vec2d

case class Triangle(
  val a: Vec2d,
  val b: Vec2d,
  val c: Vec2d,
  var na: Triangle = null,
  var nb: Triangle = null,
  var nc: Triangle = null) {

  def apply(v: Vertex) = v match {
    case Va => a
    case Vb => b
    case Vc => c
  }

  def apply(e: Edge) = e match {
    case Ea => na
    case Eb => nb
    case Ec => nc
  }

  def update(e: Edge, t: Triangle) = e match {
    case Ea => na = t
    case Eb => nb = t
    case Ec => nc = t
  }

  def segment(e: Edge) = e match {
    case Ea => (a, b)
    case Eb => (b, c)
    case Ec => (c, a)
  }

  def normal(e: Edge): Vec2d = {
    val (p, q) = segment(e)
    Geometry.normal(p, q)
  }

  override def hashCode =
    31 * (a.hashCode + 31 * (b.hashCode + 31 * c.hashCode))

  def contains(v: Vec2d) = Geometry.inTriangle(v, a, b, c)

  def center = Geometry.center(a, b, c)

  def commonEdge(t: Triangle): Edge = {
    if (t == na) Ea
    else if (t == nb) Eb
    else if (t == nc) Ec
    else null
  }

  def edge(p: Vec2d, q: Vec2d) = (p, q) match {
    case (`a`, `b`) => Ea
    case (`b`, `c`) => Eb
    case (`c`, `a`) => Ec
  }

  def connect(e: Edge, t: Triangle) {
    this(e) = t
    if (t != null) {
      val (p, q) = segment(e)
      val otherEdge = t.edge(q, p)
      t(otherEdge) = this
    }
  }
  
  def connect(na: Triangle, nb: Triangle, nc: Triangle) {
    List(na, nb, nc) zip Edge.all foreach { p => connect(p._2, p._1) }
  }

  def points = (a, b, c)
  def pointSeq = Seq(a, b, c)
}