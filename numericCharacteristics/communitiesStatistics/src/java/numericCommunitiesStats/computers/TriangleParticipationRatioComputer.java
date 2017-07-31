package numericCommunitiesStats.computers;

import java.util.Collection;

import numericCommunitiesStats.ParametersSetStats;
import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.dataStructures.IGraph.INode;


/**
 * Created with IntelliJ IDEA.
 * User: Kyrylo Chykhradze
 */

public class TriangleParticipationRatioComputer extends DataInsideStatisticComputer {
	public static final String NAME_IN_SCENARIO = "TPR";

	@Override
	public NumericCharacteristic compute(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes, 
			ParametersSetStats parameters) {
		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.LIST_OF_VALUES);
		Float minWeight = parameters.getMinWeight();
		boolean directed = graph.isDirected();
		boolean weighted = graph.isWeighted();
		if (!weighted) minWeight = 0f; 
		String trianglesType = parameters.getTrianglesType();
		for (IGroupOfNodes groupOfNodes : groupsOfNodes)	{
			int nodesInTrianglesCounter = 0;
			for (IGraph.INode node : groupOfNodes)	{
				if (graphContainsTriangleWithNodeInGroup(graph, node, groupOfNodes, minWeight, trianglesType, directed))	{
					nodesInTrianglesCounter++;
				}
			}
			Double tpr = ((double)nodesInTrianglesCounter) / groupOfNodes.size();
			result.addValue(tpr);
		}
		return result;
	}
	
	private static boolean graphContainsTriangleWithNodeInGroup(IGraph graph, IGraph.INode node, 
			IGroupOfNodes group, Float minWeight, String trianglesType, Boolean directed)	{
		
		Collection<INode> innerNeighbours = graph.getNeighboursInGroup(node, group);
		if (innerNeighbours.isEmpty())	{
			return false;
		}
		if (directed){
			if (trianglesType.equals("CLIQUE")){
				return cliqueExists(graph, node,innerNeighbours, minWeight);
			}
			
			else if (trianglesType.equals("WCC")){
				return wccExists(graph, node,innerNeighbours, minWeight);
			}
			
			else if (trianglesType.equals("SCC")){
				return sccExists(graph, node,innerNeighbours, minWeight);
			}
			return false;
		}
		else{
			for (INode neighbour1 : innerNeighbours)	{
				for (INode neighbour2 : innerNeighbours)	{
					if (neighbour1.equals(neighbour2))	{
						continue;
					}
					if ((graph.hasEdge(neighbour1, neighbour2) && graph.getEdgeWeight(neighbour1, neighbour2)>minWeight &&
							graph.getEdgeWeight(node, neighbour1)>minWeight && graph.getEdgeWeight(node, neighbour2)>minWeight)){
						return true;
					}
				}
			}
			return false;
		}
	}
	
	private static boolean cliqueExists(IGraph graph, INode node, Collection<INode> innerNeighbours, Float minWeight){
		for (INode neighbour1 : innerNeighbours)	{
			for (INode neighbour2 : innerNeighbours)	{
				if (neighbour1.equals(neighbour2))	{
					continue;
				}
				if (!(graph.hasEdge(neighbour1, neighbour2) && graph.hasEdge(neighbour2, neighbour1)
						&& graph.hasEdge(node, neighbour2) && graph.hasEdge(node, neighbour1) 
						&& graph.hasEdge(neighbour2, node) && graph.hasEdge(neighbour1, node))){
					continue;
				}
				if (graph.getEdgeWeight(neighbour2, neighbour1)>minWeight && 
					graph.getEdgeWeight(neighbour1, neighbour2)>minWeight &&
					graph.getEdgeWeight(node, neighbour1)>minWeight && 
					graph.getEdgeWeight(neighbour1, node)>minWeight && 
					graph.getEdgeWeight(node, neighbour2)>minWeight && 
					graph.getEdgeWeight(neighbour2, node)>minWeight){
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean wccExists(IGraph graph, INode node, Collection<INode> innerNeighbours, Float minWeight){
		for (INode neighbour1 : innerNeighbours)	{
			for (INode neighbour2 : innerNeighbours)	{
				if (neighbour1.equals(neighbour2))	{
					continue;
				}
				Float weight01 = graph.getEdgeWeight(node, neighbour1);
				Float weight02 = graph.getEdgeWeight(node, neighbour2);
				Float weight10 = graph.getEdgeWeight(neighbour1, node);
				Float weight20 = graph.getEdgeWeight(neighbour2, node);
				Float weight12 = graph.getEdgeWeight(neighbour1, neighbour2);
				Float weight21 = graph.getEdgeWeight(neighbour2, neighbour1);
				Integer indicator = 0;
				if ((weight01 != null && weight01 > minWeight) || (weight10 != null && weight10 > minWeight)) {
					indicator+=1;
				}
				if ((weight02 != null && weight02 > minWeight) || (weight20 != null && weight20 > minWeight)) indicator+=1;
				if ((weight12 != null && weight12 > minWeight) || (weight21 != null && weight21 > minWeight)) indicator+=1;
				if (indicator == 3 ){
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean sccExists(IGraph graph, INode node, Collection<INode> innerNeighbours, Float minWeight){
		for (INode neighbour1 : innerNeighbours)	{
			for (INode neighbour2 : innerNeighbours)	{
				if (neighbour1.equals(neighbour2))	{
					continue;
				}
				Float weight01 = graph.getEdgeWeight(node, neighbour1);
				Float weight02 = graph.getEdgeWeight(node, neighbour2);
				Float weight10 = graph.getEdgeWeight(neighbour1, node);
				Float weight20 = graph.getEdgeWeight(neighbour2, node);
				Float weight12 = graph.getEdgeWeight(neighbour1, neighbour2);
				Float weight21 = graph.getEdgeWeight(neighbour2, neighbour1);
				Integer indicator = 0;
				if ((weight01 != null && weight01 > minWeight) || (weight10 != null && weight10 > minWeight)) {
					indicator+=1;
				}
				if ((weight02 != null && weight02 > minWeight) || (weight20 != null && weight20 > minWeight)) indicator+=1;
				if ((weight12 != null && weight12 > minWeight) || (weight21 != null && weight21 > minWeight)) indicator+=1;
				if (indicator == 3 ){
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
					if (indicator==9){
						return true;
					}
				}
			}
		}
		return false;
	}
}
