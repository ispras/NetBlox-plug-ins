package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import numericCommunitiesStats.utils.{User, Utils}
import scala.collection.JavaConversions._
import ru.ispras.modis.NetBlox.dataStructures.Graph
import scala.collection.JavaConverters.asJavaIteratorConverter
import java.util.Iterator
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes
import scala.collection.JavaConversions._
import ru.ispras.modis.NetBlox.dataStructures.SetOfGroupsOfNodes
import numericCommunitiesStats.ParametersSetStats

/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 */
/**
 * calculate the dependency between number of internal edges in community and its size
 */
object NumberOfEdgesFromSize extends Writable {
    /**
     *
     * @param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return AVERAGE weight of internal edges in community of each size
     */
    def apply(communitiesPath: String, graphPath: String, directed: Boolean, weighted: Boolean, 
                      parameters: ParametersSetStats) = {
      val graph = new Graph(graphPath, directed, weighted);
      val maxWeight = graph.getMaxEdgeWeight
      val minWeight: Float = if (weighted) parameters.getMinWeight else 0f
      val connectednessType = parameters.getConnectednessType
      val trianglesType = parameters.getTrianglesType
      val icdfType = parameters.getIcdfType
      val communities = new SetOfGroupsOfNodes(communitiesPath, graph)

      val start = System.currentTimeMillis()
      var i = 1d
      val size = communities.size
      
      val result = communities.map{
        community =>
          i += 100d / size
          if (i > 1) {
            i -= 1; print("=")
          }
          processOneCommunity(community, graph, directed, weighted, maxWeight, minWeight, 
                                connectednessType, trianglesType, icdfType)
      }.groupBy(_._1).mapValues(l => l.map { v =>
        v._2 / l.size
        }.sum).toMap
      println()
      println(this.getClass.getName + "\t" + (System.currentTimeMillis() - start) * 0.001)
      val s = mapAsJavaMap(result)
      s
    }

  /**
   * Returns a sequence of values for the sequence of communities.
   */

    def processOneCommunity(community: IGroupOfNodes, graph: Graph, directed: Boolean, weight: Boolean, maxWeight: Float, minWeight: 
        Float, connectednessType: String, trianglesType: String, icdfType: String) = {
        val totalSum = (community.foldLeft(0d){
          (sum, user) => {
            var interSum = sum.toDouble;
            val nextIdIterator = graph.getOutcomingNeighbours(user).iterator()
            while (nextIdIterator.hasNext) {
              val nextId = nextIdIterator.next()
              if (community.contains(nextId)){
                var edgeWeight = graph.getEdgeWeight(user, nextId)
                if (edgeWeight != null) interSum = edgeWeight / maxWeight + interSum
              }
            }
            interSum
            }
          }
        )
        if (directed)(community.size.toDouble, totalSum)
        else (community.size.toDouble, totalSum/2)
    }
}
