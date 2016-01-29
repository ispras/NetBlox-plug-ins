package numericCommunitiesStats.communitybased

import numericCommunitiesStats.utils.{Utils, User}

import scala.collection.mutable
import gnu.trove.map.hash.TIntObjectHashMap
import collection.JavaConversions._




/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 * Date: 10.12.14
 */
object CommonEdgeProbability extends Writable {



  def apply(communitiesPath: String, graphPath: String) = {
    val graph = Utils.toNodeMap(Utils.readEdgeList(graphPath, "\t"))
    val communities = Utils.readCommunities(communitiesPath, graph)
    val workSize = communities.foldLeft(0)((sum, community) => sum + community.size * community.size)
    var workDone = 1d
    val start = System.currentTimeMillis()
    val users = Utils.toUserRepresentation(graph, communities)
    val pairByNumberOfCommonCommunities = mutable.Map[Int, Int]().withDefaultValue(0)
    val connectedUserByNumberOfCommonCommunities = mutable.Map[Int, Int]().withDefaultValue(0)
    val probabilityOfCommonEdge = new java.util.LinkedHashMap[Double,Double].withDefaultValue(0)
    users.keys().foreach{
      id =>
        val user = users.get(id)
        user.neighbors.foreach{
          otherGuy => if (user.id != otherGuy)
            connectedUserByNumberOfCommonCommunities(users.get(otherGuy).communities.intersect(user.communities).size) += 1
            // count number of users with common membership
        }
    }
    connectedUserByNumberOfCommonCommunities.foreach{
      case(key, value) => connectedUserByNumberOfCommonCommunities(key) = value / 2
    }

    communities.foreach{
      community =>
        processOneCommunity(community, pairByNumberOfCommonCommunities, users)
        workDone += 100d * community.size * community.size / workSize
        if (workDone > 1) {print("="); workDone -= 1}
    }
    println()

    pairByNumberOfCommonCommunities.foreach{
      case(key, value) => pairByNumberOfCommonCommunities(key) = value / 2 / key
    }
    println(this.getClass.getName + "\t" + (System.currentTimeMillis() - start) * 0.001)
    pairByNumberOfCommonCommunities.map{
      case(key, value) => (key, connectedUserByNumberOfCommonCommunities(key).toDouble / value)
      // compute the total number of pairs of users with common membership
    }.toArray.sortBy(_._1)
    pairByNumberOfCommonCommunities.map{
      case(key, value) => probabilityOfCommonEdge(key) = connectedUserByNumberOfCommonCommunities(key).toDouble / pairByNumberOfCommonCommunities(key)
      // compute the probability of edge between users with common membership
    }
    val s = mapAsJavaMap(probabilityOfCommonEdge)
    s
  }
  /*def writeToFile(users:  TIntObjectHashMap[User], communities: Array[Set[Int]], path: String) {
    saveResult(apply(users, communities), path)
  }*/

  private def processOneCommunity(community: Set[Int], pairByNumberOfCommonCommunities: mutable.Map[Int, Int], users: TIntObjectHashMap[User]) = {
    community.foreach{
      thatGuy => community.foreach {
        otherGuy =>
          if(thatGuy != otherGuy){
            pairByNumberOfCommonCommunities(users.get(thatGuy).communities.intersect(users.get(otherGuy).communities).size) += 1

          }
      }
    }
  }

  def quadraticSolution(users: Map[Int, User]) = {
    val pairByNumberOfCommonCommunities = mutable.Map[Int, Int]().withDefaultValue(0)
    val connectedUserByNumberOfCommonCommunities = mutable.Map[Int, Int]().withDefaultValue(0)
    users.values.foreach{
      user => users.values.foreach {
        otherGuy =>
          if(user != otherGuy) {
            pairByNumberOfCommonCommunities(user.communities.intersect(otherGuy.communities).size) += 1
            if (user.neighbors.contains(otherGuy.id)) connectedUserByNumberOfCommonCommunities(user.communities.intersect(otherGuy.communities).size) += 1
          }
      }
    }

    pairByNumberOfCommonCommunities.map{
      case(key, value) => (key, connectedUserByNumberOfCommonCommunities(key).toDouble / value)
    }.toArray.sortBy(_._1)
  }


}


