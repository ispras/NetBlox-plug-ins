package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap


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

    protected def processOneCommunity(community: Set[Int], graph: TIntObjectHashMap[Array[Int]]) = {
        val icdf = community.foldLeft(0d)((max, user) => {
            val candMax = graph.get(user).count(community)
            math.max(max, candMax)
        })
        icdf / community.size
    }
}