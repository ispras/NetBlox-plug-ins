package numericCommunitiesStats.communitybased

import gnu.trove.map.hash.TIntObjectHashMap
import numericCommunitiesStats.utils.Utils
import ru.ispras.modis.NetBlox.dataStructures.Graph
import ru.ispras.modis.NetBlox.dataStructures.IGraph.INode
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes
import scala.collection.JavaConversions._
import collection.mutable.WrappedArray
/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo
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
    protected def processOneCommunity(community: IGroupOfNodes, graph: Graph,
                  directed: Boolean, weighted: Boolean, maxWeight: Float, minWeight: Float, connectednessType: String,
                  trianglesType: String, icdfType: String)= {
        var internalTriangles = 0d
        var externalTriangles = 0d
        externalTriangles = computeTriangles(community, graph, trianglesType, maxWeight, minWeight, false)
        internalTriangles = computeTriangles(community, graph, trianglesType, maxWeight, minWeight, true)
        val size = community.size
        (size,
            if ((internalTriangles + externalTriangles > 0) && size > 2) 
                  internalTriangles / Utils.binomialCoefficient(size, 3) * 
                    internalTriangles / (internalTriangles + externalTriangles)
            else Double.NaN)
    }
    
    
    protected def computeTriangles(community: IGroupOfNodes, graph: Graph, trianglesType: String, 
                            maxWeight: Float, minWeight: Float, internal: Boolean)={
      var numberOfTriangles = 0
      if (trianglesType.equals("CLIQUE")){
        numberOfTriangles = cliqueNumberComputer(community, graph, minWeight, internal)
      }
      else if (trianglesType.equals("WCC")){
        numberOfTriangles = wccNumberComputer(community, graph, minWeight, internal)
      }
      else if (trianglesType.equals("SCC")){
        numberOfTriangles = sccNumberComputer(community, graph, minWeight, internal)
      }
      numberOfTriangles
    }
    
    protected def cliqueNumberComputer(community: IGroupOfNodes, graph: Graph, 
                              minWeight: Float, internal: Boolean): Integer = {
      var numberOfTriangles = 0
      numberOfTriangles = community.foldLeft(0){
          (count,node) => {
            val outCollection = graph.getNeighboursInGroup(node, community)
            for (outNode <- outCollection if (outNode.getId>node.getId)){
              var weight01 = graph.getEdgeWeight(node, outNode)
              var weight10 = graph.getEdgeWeight(outNode, node)
              if (weight01 != null && weight10!=null && weight01>minWeight && weight10>minWeight){
                var secondOutCollection = graph.getNeighbours(outNode)
                if (internal) secondOutCollection = secondOutCollection.filter { x => community.contains(x) }
                else secondOutCollection = secondOutCollection.filter { x => !community.contains(x) }
                for (secondOutNode <- secondOutCollection  if (secondOutNode.getId > outNode.getId)){
                  var weight02 = graph.getEdgeWeight(node, secondOutNode)
                  var weight20 = graph.getEdgeWeight(secondOutNode, node)
                  var weight12 = graph.getEdgeWeight(outNode, secondOutNode)
                  var weight21 = graph.getEdgeWeight(secondOutNode, outNode)
                  if (weight02 != null && weight20 != null && weight21 != null && weight12 != null && weight02>minWeight 
                      && weight20 > minWeight && weight12 > minWeight && weight21 > minWeight){
                    numberOfTriangles+=1
                  }
                }
              }
            }
          }
          numberOfTriangles
        }
    numberOfTriangles
    }
    
    
    protected def wccNumberComputer(community: IGroupOfNodes, graph: Graph, minWeight: Float, internal: Boolean): Integer = {
      var numberOfTriangles = 0
      numberOfTriangles = community.foldLeft(0){
            (count,node) => {
               val outCollection = graph.getNeighbours(node)
               for (outNode <- outCollection if (outNode.getId>node.getId && community.contains(outNode))){
                 for (secondOutNode <- outCollection if ((internal && community.contains(secondOutNode) &&
                                               secondOutNode.getId > outNode.getId) || 
                                               (!internal && !community.contains(secondOutNode)))){
                   var indicator = 0
                   var weight01 = graph.getEdgeWeight(node, outNode)
                   var weight10 = graph.getEdgeWeight(outNode, node)
                   var weight02 = graph.getEdgeWeight(node, secondOutNode)
                   var weight20 = graph.getEdgeWeight(secondOutNode, node)
                   var weight12 = graph.getEdgeWeight(outNode, secondOutNode)
                   var weight21 = graph.getEdgeWeight(secondOutNode, outNode)
                   if ((weight01 != null && weight01 > minWeight) || (weight10 != null && weight10 > minWeight)) indicator+=1
  		             if ((weight02 != null && weight02 > minWeight) || (weight20 != null && weight20 > minWeight)) indicator+=1
        		 			 if ((weight12 != null && weight12 > minWeight) || (weight21 != null && weight21 > minWeight)) indicator+=1
        		 			 if (indicator==3) {
        		 			   numberOfTriangles+=1
        		 			 }
                 }
               }
            }
            numberOfTriangles
        }
    numberOfTriangles
    }
    
    
    protected def sccNumberComputer(community: IGroupOfNodes, graph: Graph, minWeight: Float, internal: Boolean): Integer = {
      var numberOfTriangles = 0
      numberOfTriangles = community.foldLeft(0){
            (count,node) => {
               val outCollection = graph.getNeighbours(node)
               for (outNode <- outCollection if (outNode.getId>node.getId && community.contains(outNode))){
                 for (secondOutNode <- outCollection if ((internal && community.contains(secondOutNode) &&
                                               secondOutNode.getId > outNode.getId) || 
                                               (!internal && !community.contains(secondOutNode)))){
                   var indicator = 0
                   var weight01 = graph.getEdgeWeight(node, outNode)
                   var weight10 = graph.getEdgeWeight(outNode, node)
                   var weight02 = graph.getEdgeWeight(node, secondOutNode)
                   var weight20 = graph.getEdgeWeight(secondOutNode, node)
                   var weight12 = graph.getEdgeWeight(outNode, secondOutNode)
                   var weight21 = graph.getEdgeWeight(secondOutNode, outNode)
                   if ((weight01 != null && weight01 > minWeight) || (weight10 != null && weight10 > minWeight)) indicator+=1
  		             if ((weight02 != null && weight02 > minWeight) || (weight20 != null && weight20 > minWeight)) indicator+=1
        		 			 if ((weight12 != null && weight12 > minWeight) || (weight21 != null && weight21 > minWeight)) indicator+=1
        		 			 if (indicator==3){
                     if ((weight01 != null && weight01 > minWeight) || (weight02 != null && weight02 > minWeight)) {
            						indicator+=1;
          					 }
          					 if ((weight10 != null && weight10 > minWeight) || (weight20 != null && weight20 > minWeight)) {
          					 	 indicator+=1;
           					 }
          					 if ((weight12 != null && weight12 > minWeight) || (weight10 != null && weight10 > minWeight)) {
          						 indicator+=1;
          					 }
          					 if ((weight21 != null && weight21 > minWeight) || (weight01 != null && weight01 > minWeight)) {
          						 indicator+=1;
          					 }
          					 if ((weight20 != null && weight20 > minWeight) || (weight21 != null && weight21 > minWeight)) {
          						 indicator+=1;
          					 }
          					 if ((weight02 != null && weight02 > minWeight) || (weight12 != null && weight12 > minWeight)) {
          						 indicator+=1;
          					 }
          		 			 if (indicator>=6) numberOfTriangles+=1
        		 			 }
                 }
               }
            }
            numberOfTriangles
        }
    numberOfTriangles
    }
}