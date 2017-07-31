package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import numericCommunitiesStats.utils.Utils
import ru.ispras.modis.NetBlox.dataStructures.Graph
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes
import scala.collection.JavaConversions._
import ru.ispras.modis.NetBlox.dataStructures.SetOfGroupsOfNodes
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes
import numericCommunitiesStats.ParametersSetStats


/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 */
/**
 * A lot of statistics may be calculated for single community,
 * @tparam A type of return value
 */
trait CommunityBased[A] extends Writable {
    /**
     *
     * @param communities sequence of communities. Community is a set of members id.
     * @param graph map from node id to sequence of neighbours
     * @return sequence of statistics for every community
     */
    def apply(communities: ISetOfGroupsOfNodes, graph: Graph, parameters: ParametersSetStats): List[A] = {
        val directed: Boolean = graph.isDirected()
        val weighted: Boolean = graph.isWeighted()
        val start = System.currentTimeMillis()
        var i = 1d
        val size = communities.size
        val communitiesIterator = communities.iterator()
        var sdf: Set[Int] = Set(1)
        val maxWeight = graph.getMaxEdgeWeight 
        var minWeight: Float = if (weighted) parameters.getMinWeight else 0f
        var connectednessType = parameters.getConnectednessType
        var trianglesType = parameters.getTrianglesType
        var icdfType = parameters.getIcdfType
        val result = communitiesIterator.map {
            community =>
                i += 100d / size
                if (i > 1) {
                    i -= 1; print("=")
                }
                processOneCommunity(community, graph, directed, weighted, maxWeight, 
                                minWeight, connectednessType, trianglesType, icdfType)
        }
        
        println()
        println(this.getClass.getName + "\t" + (System.currentTimeMillis() - start) * 0.001)
        result.toList
    }

    /**
     * Returns a sequence of values for the sequence of communities.
     */
    def apply(communitiesPath: String, graphPath: String, directed: Boolean, weighted: Boolean, 
              parameters: ParametersSetStats): List[A] = {
        val graph = new Graph(graphPath, directed, weighted);
        val communities = new SetOfGroupsOfNodes(communitiesPath, graph)
        apply(communities, graph, parameters)
    }

    /**
     * calculate statistics for getting community.
     * @param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return
     */
    protected def processOneCommunity(community: IGroupOfNodes, graph: Graph, 
                                      directed: Boolean, weighted: Boolean, maxWeight: Float, minWeight: Float,
                                      connectednessType: String, trianglesType: String, icdfType: String): A


    /**
     * calculate statistics for every community and write result to file
     * @param communities  sequence of communities. Community is a set of members id.
     * @param graph  map from node id to sequence of neighbours
     * @param path path to file with results.
     */
    def writeToFile(communities: SetOfGroupsOfNodes, graph: Graph, path: String, 
                directed: Boolean, weighted: Boolean, parameters: ParametersSetStats) {
        val maxWeight = graph.getMaxEdgeWeight()
        val result = apply(communities: SetOfGroupsOfNodes, graph: Graph, parameters)
        saveResult(result, path)
    }
}
