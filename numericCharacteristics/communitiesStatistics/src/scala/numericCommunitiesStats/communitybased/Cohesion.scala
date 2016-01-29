package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import numericCommunitiesStats.utils.Utils

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 27.02.14
 * Time: 14:20
 */

/**
 * Cohesion is a fraction of number of internal triangle to it theoretical maximum: binomialCoefficient(size, 3), multiply
 * by fraction of internal triangles to (internal + external triangles)
 * if there are no one triangle or the size of community less than 3 it return -1
 */
object Cohesion extends CommunityBased[(Int, Double)] {
    /**
     *
     * @param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return (internalTriangle / binomialCoefficient(community.size, 3)) * (internalTriangle / (internalTriangle + externalTriangle))
     */
    protected def processOneCommunity(community: Set[Int], graph: TIntObjectHashMap[Array[Int]])= {
        var internalTriangle = 0d
        var externalTriangle = 0d

        community.foreach{
            user => graph.get(user).filter(community).filter(neighbor => neighbor > user).foreach{
                neighbor => internalTriangle += graph.get(neighbor).intersect(graph.get(user)).count(community)
                            externalTriangle += graph.get(neighbor).intersect(graph.get(user)).count(!community.contains(_))
            }
        }

        internalTriangle /= 3
        externalTriangle /= 1
        val size = community.size
        (size,
            if ((internalTriangle + externalTriangle > 0) && size > 2) internalTriangle / Utils.binomialCoefficient(size, 3) * internalTriangle / (internalTriangle + externalTriangle)
            else -1)
    }

}
