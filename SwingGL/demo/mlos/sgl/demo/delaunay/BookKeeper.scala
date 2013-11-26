package mlos.sgl.demo.delaunay

import mlos.sgl.core.Vec2d

class BookKeeper extends Delaunay#Listener {

  var hops = 0
  var incircleTests = 0
  var brokenTriangles = 0
  var brokenEdges = 0
  var visits = 0
  var flips = 0

  def point(a: Vec2d) = ()

  def nextHop(t: Triangle) {
    hops += 1
  }

  def foundContaining(v: Vec2d, t: Triangle) = ()

  def beginFixup() = ()

  def testCircle(t: Triangle, n: Triangle, v: Vertex) {
    incircleTests += 1
  }

  def break(t: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, tc: Triangle) {
    brokenTriangles += 1
  }

  def breakEdge(t: Triangle, s: Triangle, v: Vec2d, ta: Triangle, tb: Triangle, 
      sa: Triangle, sb: Triangle) {
    brokenEdges += 1
  }
  
  def visit(t: Triangle) {
    visits += 1
  }
  
  def flip(p: Triangle, q: Triangle) {
    flips += 1
  }

  def endFixup() = ()
  def triangle(t: Triangle) = ()
  def finished() = ()
  
  def makeStats(t: Long) = {
    Stats(t, hops, incircleTests, brokenTriangles, brokenEdges, visits, flips)
  }
}