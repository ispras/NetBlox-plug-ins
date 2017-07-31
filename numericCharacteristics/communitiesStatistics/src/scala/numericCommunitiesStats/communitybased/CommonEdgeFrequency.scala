package numericCommunitiesStats.communitybased

import numericCommunitiesStats.utils.{Utils, User}

import scala.collection.mutable
import gnu.trove.map.hash.TIntObjectHashMap
import collection.JavaConversions._
import ru.ispras.modis.NetBlox.dataStructures.Graph
import numericCommunitiesStats.ParametersSetStats



/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 * Date: 10.12.14
 */
object CommonEdgeFrequency extends Writable {

  def apply(communitiesPath: String, graphPath: String, directed: Boolean, 
                    weighted: Boolean, parameters: ParametersSetStats ) = {
    val graph = new Graph(graphPath, directed, weighted);
    val graph1 = Utils.toNodeMap(Utils.readEdgeList(graphPath, "\t"))
    val communities = Utils.readCommunities(communitiesPath, graph)
    val workSize = communities.foldLeft(0)((sum, community) => sum + community.size * community.size)
    var workDone = 1d
    val start = System.currentTimeMillis()
    val users = Utils.toUserRepresentation(graph1, communities)
    val pairByNumberOfCommonCommunities = mutable.Map[Int, Int]().withDefaultValue(0)
    val connectedUserByNumberOfCommonCommunities = mutable.Map[Int, Float]().withDefaultValue(0f)
    val probabilityOfCommonEdge = new java.util.LinkedHashMap[Double,Double].withDefaultValue(0)
    val maxWeight = graph.getMaxEdgeWeight()
    val bidir  = parameters.getBidirectional
    graph.getNodes.foreach{
      node => 
        val user = users.get(node.getId)  
        graph.getOutcomingNeighbours(node).foreach{
          otherGuy =>
            if ((bidir && graph.getOutcomingNeighbours(otherGuy).contains(node)) || !bidir){
              connectedUserByNumberOfCommonCommunities(users.get(otherGuy.getId).
                  communities.intersect(user.communities).size) += graph.getEdgeWeight(node, otherGuy) / (2*maxWeight)
            }
        }
    }
    connectedUserByNumberOfCommonCommunities.foreach{
      case(key, value) => {
        //key: quantity of common communities
        //value: total sum of normalized weight with nodes that share @key communities
        if (directed) connectedUserByNumberOfCommonCommunities(key) = value
        else connectedUserByNumberOfCommonCommunities(key) = value / 2
      }
    }

    communities.foreach{
      community =>
        processOneCommunity(community, pairByNumberOfCommonCommunities, users)
        workDone += 100d * community.size * community.size / workSize
        if (workDone > 1) {print("="); workDone -= 1}
    }
    println()
    
    pairByNumberOfCommonCommunities.foreach{
      case(key, value) => {
        //key: quantity of common communities
        //value: number of pairs of nodes
        //
        // We pass through key communities and computes this value key times. 
        // That's why we need to divide by the quantity of common communities -- each edge is computed $key times.
        pairByNumberOfCommonCommunities(key) = value / 2 / key
      }
    }
    println(this.getClass.getName + "\t" + (System.currentTimeMillis() - start) * 0.001)
    pairByNumberOfCommonCommunities.map{
      case(key, value) => probabilityOfCommonEdge(key) = connectedUserByNumberOfCommonCommunities(key).toDouble / pairByNumberOfCommonCommunities(key)
      // compute the probability of edge between users with common membership (key:  quantity of common communities, value: average weight)
    }
    val s = mapAsJavaMap(probabilityOfCommonEdge)
    s
  }

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
}


