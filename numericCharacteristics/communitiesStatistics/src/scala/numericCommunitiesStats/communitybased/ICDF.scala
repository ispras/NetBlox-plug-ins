package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import ru.ispras.modis.NetBlox.dataStructures.Graph
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes
import ru.ispras.modis.NetBlox.dataStructures.IGraph.INode
import scala.collection.JavaConversions._

/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 */
/**
 * I take description of ICDF in
 * Structure and Overlaps of Communities in Networks, Jaewon Yang and Jure Leskovec
 * page 4
 */

object ICDF extends CommunityBased[Double] {
    /**
     * calculate ICDF for getting community. ICDF the maximal fraction of community members
     * any member node is connected to
     * @param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return ICDF for given community
     */

    protected def processOneCommunity(community: IGroupOfNodes, graph: Graph, 
        directed: Boolean, weighted: Boolean, maxWeight: Float, minWeight: Float, connectednessType: String,
                  trianglesType: String, icdfType: String) = {
        val icdf = community.foldLeft(0d)((max, user) => {
            val candMax = if (directed) directedIntersumComputer(graph, community, user, icdfType)
                          else undirectedIntersumComputer(graph, community, user, icdfType)
            math.max(max, candMax)
        })
        if (directed) icdf / (maxWeight * 2 * community.size)
        else icdf / (maxWeight * community.size)
    }
    
    protected def directedIntersumComputer(graph: Graph, community: IGroupOfNodes,  
                                          user: INode, icdfType: String): Double= {
      var interSum = 0d
      val nextIdIterator = {
                    if (icdfType=="IN") graph.getIncomingNeighbours(user).iterator()
                    else if (icdfType=="OUT") graph.getOutcomingNeighbours(user).iterator()
                    else graph.getNeighbours(user).iterator()
                  }
      while (nextIdIterator.hasNext) {
        val nextId = nextIdIterator.next()
        if (community.contains(nextId)){
          if (icdfType=="IN"){
            var edgeWeight = graph.getEdgeWeight(nextId, user) 
            if (edgeWeight != null) interSum = edgeWeight  + interSum
          }
          else if (icdfType=="OUT"){
            var edgeWeight = graph.getEdgeWeight(user, nextId) 
            if (edgeWeight != null) interSum = edgeWeight + interSum
          }
          else{
            var edgeWeight = graph.getEdgeWeight(user, nextId) 
            if (edgeWeight != null) interSum = edgeWeight  + interSum
            var returnEdgeWeight = graph.getEdgeWeight(nextId, user) 
            if (returnEdgeWeight != null) interSum = returnEdgeWeight + interSum
          }
        }
      }
      interSum
    }
    
    protected def undirectedIntersumComputer(graph: Graph, community: IGroupOfNodes,  
                                          user: INode, icdfType: String): Double= {
      var interSum = 0d
      val nextIdIterator = graph.getNeighbours(user).iterator()
      while (nextIdIterator.hasNext) {
        val nextId = nextIdIterator.next()
        if (community.contains(nextId)){
            var edgeWeight = graph.getEdgeWeight(user, nextId) 
            if (edgeWeight != null) interSum = edgeWeight  + interSum
        }
      }
      interSum
    }
}