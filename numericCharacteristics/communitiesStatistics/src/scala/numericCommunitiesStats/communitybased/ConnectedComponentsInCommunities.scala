package numericCommunitiesStats.communitybased

import numericCommunitiesStats.utils.User

import scala.collection.mutable
import gnu.trove.map.hash.TIntObjectHashMap
import gnu.trove.map.hash
import gnu.trove.set.hash.TIntHashSet
import collection.mutable.ArrayBuffer
import ru.ispras.modis.NetBlox.dataStructures.Graph
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes
import ru.ispras.modis.NetBlox.dataStructures.IGraph.INode
import scala.collection.JavaConversions._
import ru.ispras.modis.NetBlox.scenario.ParametersSet


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
  protected def processOneCommunity(community: IGroupOfNodes, graph: Graph, 
      directed: Boolean, weighted: Boolean, maxWeight: Float, minWeight: Float, connectednessType: String,
                  trianglesType: String, icdfType: String) = {
    // WCC -- weakly connected components; SCC -- strongly connected components
    var result: Int = if(connectednessType.equals("WCC")) wccComputer(community, graph, minWeight)
                      else sccComputer(community,graph,minWeight)
    result
  }
  
  protected def sccComputer(community: IGroupOfNodes, graph: Graph, minWeight: Float) = {
      val components: ArrayBuffer[Vector[Int]] = ArrayBuffer()
      val dfs_visited = mutable.HashMap[Integer, Int]()
      val stack_for_second_dfs = mutable.Stack[INode]() 
      
      community.foreach(node => dfs_visited.put(node.getId, 0))
      
      /*
       * first step: dfs in the original graph
       * result is the stack of vertex 
       * vertex with the greatest time of an output is above
       */
      
      def dfs(node: INode):Unit={
          dfs_visited.update(node.getId, 1)
          val neighbors = graph.getOutcomingNeighbours(node)
          for (neighbor <- neighbors if community.contains(neighbor)){
              if (graph.getEdgeWeight(node, neighbor)>=minWeight && dfs_visited(neighbor.getId)==0){
                  dfs(neighbor)
              }
          }
          stack_for_second_dfs.push(node)
      }
      
      for (node <- community){
          if (dfs_visited(node.getId)==0){ //dfs starts from this vertex
             dfs(node)
          }
      }
      
      /*
       * second step: dfs in the transposed graph
       * the result of each dfs is the strongly connected component
       */
      
      while (!stack_for_second_dfs.isEmpty){
          val node = stack_for_second_dfs.pop()
          
          if (dfs_visited(node.getId)!=2){ //dfs starts from this vertex
              val temp_stack = mutable.Stack[INode](node)
              val component = Vector.newBuilder[Int]
              
              while (!temp_stack.isEmpty){
                  val vertex = temp_stack.pop()
                  val uid = vertex.getId()
                  if (dfs_visited(uid)!=2){
                      dfs_visited.update(uid, 2)
                      component += uid
                      graph.getIncomingNeighbours(vertex).foreach { 
                          x => if (community.contains(x) && graph.getEdgeWeight(x, vertex)>=minWeight 
                                                                                              && dfs_visited(x.getId)!=2) 
                              temp_stack.push(x) 
                      }
                  }
              }
              components+=component.result()
          }
      }
      components.size
  }
  
  
   protected def wccComputer(community: IGroupOfNodes, graph: Graph, minWeight: Float) = {
      val components: ArrayBuffer[Vector[Int]] = ArrayBuffer()

      val bfs_labels = mutable.HashMap[Int, Int]() // Breadth-first search labels
      community.foreach(node => bfs_labels.put(node.getId, 0))
      for (member <- community if (bfs_labels.get(member.getId).get == 0)) {
        val queue = mutable.Queue[Int](member.getId)
        bfs_labels.put(member.getId, 1)
        val component = Vector.newBuilder[Int]
        while (!queue.isEmpty) {
          val uid = queue.dequeue()
          bfs_labels.put(uid, 2)
          component += uid
          val neighboursIterator = graph.getNeighboursInGroup(graph.getNode(uid), community).iterator()
          while (neighboursIterator.hasNext) {
            val nextNeighbour = neighboursIterator.next()
            val currentWeight1 = graph.getEdgeWeight(graph.getNode(uid), nextNeighbour)
            val currentWeight2 = graph.getEdgeWeight(nextNeighbour, graph.getNode(uid) )
            if (((currentWeight1 != null) && (currentWeight1 >= minWeight)) ||
                (((currentWeight2 != null) && (currentWeight2 >= minWeight)))){
              val nextNeighbourId = nextNeighbour.getId.toInt
              if (bfs_labels.get(nextNeighbourId).get == 0) {
                queue += nextNeighbourId
                bfs_labels.put(nextNeighbourId, 1)
              }
            }
          }
        }
        components += component.result()
      }
      components.size
  }
}

