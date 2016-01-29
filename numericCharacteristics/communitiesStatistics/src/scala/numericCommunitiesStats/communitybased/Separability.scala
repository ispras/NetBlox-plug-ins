package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap

/**
 * Created with IntelliJ IDEA.
 * User: padre * Date: 25.02.14 * Time: 18:31
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
     protected def processOneCommunity(community: Set[Int], graph: TIntObjectHashMap[Array[Int]]) = {
        val in = community.foldLeft(0d)((sum, user) => sum + graph.get(user).count(community))
        val out = community.foldLeft(0d)((sum, user) => sum + graph.get(user).count(!community.contains(_)))

        0.5 * in.toDouble / out
    }
}
