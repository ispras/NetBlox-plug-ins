package numericCommunitiesStats.communitybased

import numericCommunitiesStats.utils.User

import scala.collection.mutable
import gnu.trove.map.hash.TIntObjectHashMap
import gnu.trove.map.hash
import gnu.trove.set.hash.TIntHashSet
import collection.mutable.ArrayBuffer

/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 */
object ConnectedComponentsInCommunities extends CommunityBased[Double]  {
  /**
   * calculate ConnectedComponentsInCommunities for getting community.
   * @param community set of members id
   * @param graph map node id to the set of neighbours id
   * @return Number of Connected Components for given community
   */

  protected def processOneCommunity(community: Set[Int], graph: TIntObjectHashMap[Array[Int]]) = {
      val bfs_labels = mutable.HashMap[Int, Int]()
      community.foreach(id => bfs_labels.put(id, 0))

      val ids_set = new TIntHashSet(community.toArray)

      val components: ArrayBuffer[Vector[Int]] = ArrayBuffer()

      for (mem <- community if (bfs_labels.get(mem).get == 0)) {

        val queue = mutable.Queue[Int](mem)
        bfs_labels.put(mem, 1)
        val component = Vector.newBuilder[Int]

        while (!queue.isEmpty) {
          val uid = queue.dequeue()
          bfs_labels.put(uid, 2)
          component += uid

          graph.get(uid).filter(e => ids_set.contains(e)).foreach(e => {
            if (bfs_labels.get(e).get == 0) {
              queue += e
              bfs_labels.put(e, 1)
            }
          })
        }

        components += component.result()
      }

      components.size
    }



}