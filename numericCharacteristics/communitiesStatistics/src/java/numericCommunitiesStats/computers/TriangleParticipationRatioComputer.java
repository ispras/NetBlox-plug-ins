package numericCommunitiesStats.computers;

import java.util.Collection;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.dataStructures.IGraph.INode;

public class TriangleParticipationRatioComputer extends DataInsideStatisticComputer {
	public static final String NAME_IN_SCENARIO = "TPR";

	@Override
	public NumericCharacteristic compute(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes) {
		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.LIST_OF_VALUES);
		for (IGroupOfNodes groupOfNodes : groupsOfNodes)	{
			int nodesInTrianglesCounter = 0;
			for (IGraph.INode node : groupOfNodes)	{
				if (graphContainsTriangleWithNodeInGroup(graph, node, groupOfNodes))	{
					nodesInTrianglesCounter++;
				}
			}

			Double tpr = ((double)nodesInTrianglesCounter) / groupOfNodes.size();
			result.addValue(tpr);
		}

		return result;
	}


	private boolean graphContainsTriangleWithNodeInGroup(IGraph graph, IGraph.INode node, IGroupOfNodes group)	{
		if (!group.contains(node))	{
			return false;
		}

		Collection<INode> innerNeighbours = graph.getNeighboursInGroup(node, group);
		if (innerNeighbours.isEmpty())	{
			return false;
		}

		for (INode neighbour1 : innerNeighbours)	{
			for (INode neighbour2 : innerNeighbours)	{
				if (neighbour1.equals(neighbour2))	{
					continue;
				}

				if (graph.hasEdge(neighbour1, neighbour2))	{
					return true;
				}
			}
		}

		return false;
	}
}
