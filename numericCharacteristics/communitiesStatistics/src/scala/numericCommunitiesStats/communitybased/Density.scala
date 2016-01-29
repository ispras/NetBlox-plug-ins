package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import numericCommunitiesStats.utils.Utils

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 25.02.14
 * Time: 21:15
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
     *@param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return
     */
    protected def processOneCommunity(community: Set[Int], graph: TIntObjectHashMap[Array[Int]]) = {
        if (community.size > 1) {
            val internalEdges = 0.5 * community.foldLeft(0d){
                (sum, member) => graph.get(member).count(community) + sum
            }
            internalEdges / Utils.binomialCoefficient(community.size, 2)
        }
        else -1d
    }
}
