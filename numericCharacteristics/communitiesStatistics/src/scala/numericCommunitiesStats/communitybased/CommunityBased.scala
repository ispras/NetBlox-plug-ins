package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import numericCommunitiesStats.utils.Utils


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 26.02.14
 * Time: 15:28
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
    def apply(communities: Seq[Set[Int]], graph: TIntObjectHashMap[Array[Int]]): List[A] = {
        val start = System.currentTimeMillis()
        var i = 1d
        val size = communities.size
        val result = communities.map {
            community =>
                i += 100d / size
                if (i > 1) {
                    i -= 1; print("=")
                }

                processOneCommunity(community, graph)
        }
        println()
        println(this.getClass.getName + "\t" + (System.currentTimeMillis() - start) * 0.001)
        result.toList
    }

    /**
     * Returns a sequence of values for the sequence of communities.
     */
    def apply(communitiesPath: String, graphPath: String): List[A] = {
        val graph = Utils.toNodeMap(Utils.readEdgeList(graphPath, "\t"))
        val communities = Utils.readCommunities(communitiesPath, graph)
        apply(communities, graph)
    }

    /**
     * calculate statistics for getting community.
     * @param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return
     */
    protected def processOneCommunity(community: Set[Int], graph: TIntObjectHashMap[Array[Int]]): A


    /**
     * calculate statistics for every community and write result to file
     * @param communities  sequence of communities. Community is a set of members id.
     * @param graph  map from node id to sequence of neighbours
     * @param path path to file with results.
     */
    def writeToFile(communities: Seq[Set[Int]], graph: TIntObjectHashMap[Array[Int]], path: String) {
        val result = apply(communities: Seq[Set[Int]], graph: TIntObjectHashMap[Array[Int]])
        saveResult(result, path)
    }
}
