package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import ru.ispras.modis.NetBlox.dataStructures.Graph
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 */
/**
 * 
 * I take description of separability in
 * Defining and Evaluating Network Communities based on Ground-truth, Jaewon Yang and Jure Leskovec
 * page 5
 */
object Separability extends CommunityBased[Double] {
    /**
     * calculate separability for getting community. Separability is a ratio of number of internal edges to
     * number of external edges.
     * @param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return separability for given community
     */
     protected def processOneCommunity(community: IGroupOfNodes, graph: Graph,
         directed: Boolean, weighted: Boolean, maxWeight: Float, minWeight: Float, connectednessType: String,
                  trianglesType: String, icdfType: String) = {
         var externalEdgesWeight = (community.foldLeft(0d){
              (sum, user) => {
                var externalSum = sum.toDouble;
                val nextInIdIterator = graph.getIncomingNeighbours(user).iterator()
                while (nextInIdIterator.hasNext) {
                  val nextId = nextInIdIterator.next()
                  if (!community.contains(nextId)){
                    var edgeWeight = graph.getEdgeWeight(nextId, user) 
                    if (edgeWeight != null) externalSum = edgeWeight / maxWeight + externalSum
                  }
                }
                val nextOutIdIterator = graph.getOutcomingNeighbours(user).iterator()
                while (nextOutIdIterator.hasNext) {
                  val nextId = nextOutIdIterator.next()
                  if (!community.contains(nextId)){
                    var edgeWeight = graph.getEdgeWeight(user, nextId) 
                    if (edgeWeight != null) externalSum = edgeWeight / maxWeight + externalSum
                  }
                }
                externalSum
                }
              }
            )
        if (!directed) externalEdgesWeight = externalEdgesWeight/ 2
    	  val (communitySize, numberInternalEdges) = 
    	              NumberOfEdgesFromSize.processOneCommunity(community, graph, directed, weighted,
    	                  maxWeight, minWeight, connectednessType, trianglesType, icdfType)
    	  numberInternalEdges.toDouble / externalEdgesWeight
    }
}
