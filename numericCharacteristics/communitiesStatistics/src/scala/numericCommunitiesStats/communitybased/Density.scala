package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import numericCommunitiesStats.utils.Utils
import ru.ispras.modis.NetBlox.dataStructures.Graph
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes
import scala.collection.JavaConversions._


/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 */
/**
 * I take description of density in
 * Defining and Evaluating Network Communities based on Ground-truth, Jaewon Yang and Jure Leskovec
 * page 5
 */
object Density extends CommunityBased[Double] {
    /**
     * calculate fraction of internal edges in community to theoretical maximum: n * (n - 1) / 2 
     * where n is a size of community. 
     * @param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return
     */
    protected def processOneCommunity(community: IGroupOfNodes, graph: Graph,
                    directed: Boolean, weighted: Boolean, maxWeight: Float, minWeight: Float, connectednessType: String,
                  trianglesType: String, icdfType: String) = {
        if (community.size > 1) {
          val internalWeights = community.foldLeft(0d){
              (sum,member) => {
                var interSum = sum;
                val nextIdIterator = graph.getOutcomingNeighbours(member).iterator()
                while (nextIdIterator.hasNext) {
                  val nextId = nextIdIterator.next()
                  if (community.contains(nextId)){
                    var edgeWeight = graph.getEdgeWeight(member, nextId) 
                    if (edgeWeight != null) interSum = edgeWeight   + interSum
                  }
                }
                interSum
              }
            }
            internalWeights /  (2 * maxWeight * Utils.binomialCoefficient(community.size, 2))
        }
        else Double.NaN 
    }
}