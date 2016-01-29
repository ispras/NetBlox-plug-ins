package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import numericCommunitiesStats.utils.{User, Utils}
import scala.collection.JavaConversions._


/**
 * calculate the dependency between number of internal edges in community and its size
 */
object NumberOfEdgesFromSize extends Writable {
    /**
     *
     * @param community set of members id
     * @param graph map node id to the set of neighbours id
     * @return number of internal edges in community
     */
    def apply(communitiesPath: String, graphPath: String) = {
      val graph = Utils.toNodeMap(Utils.readEdgeList(graphPath, "\t"))
      val communities = Utils.readCommunities(communitiesPath, graph)
      val start = System.currentTimeMillis()
      var i = 1d
      val size = communities.size
      //val connectedUserByNumberOfCommonCommunities = mutable.Map[Int, Int]().withDefaultValue(0)

      val result = communities.map {
        community =>
          i += 100d / size
          if (i > 1) {
            i -= 1; print("=")
          }

          processOneCommunity(community, graph)
      }.toMap
      println()
      println(this.getClass.getName + "\t" + (System.currentTimeMillis() - start) * 0.001)
      //val map = Map(result.map{ a => a._1 -> a._2 }: _*)

      //
      //
      // map
      val s = mapAsJavaMap(result)
      s

    }

  /**
   * Returns a sequence of values for the sequence of communities.
   */


    def processOneCommunity(community: Set[Int], graph: TIntObjectHashMap[Array[Int]]) = {

        (community.size.toDouble, (community.foldLeft(0){(sum, user) => graph.get(user).count(community) + sum} / 2).toDouble)
    }
}
