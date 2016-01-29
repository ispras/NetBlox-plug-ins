package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap

/**
 * Computes conductance as in
 * "Defining and Evaluating Network Communities based on Ground-truth" by Jaewon Yang and Jure Leskovec
 */
object ConductanceOnInternalEdges extends CommunityBased[Double] {

  protected def processOneCommunity(community: Set[Int], graph: TIntObjectHashMap[Array[Int]]) = {
	  val numberExternalEdges = community.foldLeft(0)((sum, id) => graph.get(id).count(!community.contains(_)) + sum)
	  val (communitySize, numberInternalEdges) = NumberOfEdgesFromSize.processOneCommunity(community, graph)
	  numberExternalEdges.toDouble / (2*numberInternalEdges + numberExternalEdges)
  }
}
