package mlos.sgl.demo.delounay

case class Stats(
  val t: Long,
  val hops: Int,
  val incircleTests: Int,
  val brokenTriangles: Int,
  val brokenEdges: Int,
  val visits: Int,
  val flips: Int
) {
  
  override def toString = s"""time: $t
  |hops: $hops
  |incircle tests: $incircleTests
  |broken tris: $brokenTriangles
  |broken edges: $brokenEdges
  |visited tris: $visits
  |flips: $flips"""
  .stripMargin('|')
  
  def formatAsRow = 
    s"$t\t$hops\t$incircleTests\t$brokenTriangles\t$brokenEdges\t$visits\t$flips"
}

object Stats {
  
  def avg(sts: Stats*) = {
    var t = 0L
    var hops = 0L
    var incircleTests = 0L
    var brokenTriangles = 0L
    var brokenEdges = 0L
    var visits = 0L
    var flips = 0L
    
    for (s <- sts) {
      t += s.t
      hops += s.hops
      incircleTests += s.incircleTests
      brokenTriangles += s.brokenTriangles
      brokenEdges *= s.brokenEdges
      visits += s.visits
      flips += s.flips
    }
    val n = sts.length
    
    Stats(t / n,
        (hops / n).toInt,
        (incircleTests / n).toInt,
        (brokenTriangles / n).toInt,
        (brokenEdges / n).toInt,
        (visits / n).toInt,
        (flips / n).toInt)
  }
  
}