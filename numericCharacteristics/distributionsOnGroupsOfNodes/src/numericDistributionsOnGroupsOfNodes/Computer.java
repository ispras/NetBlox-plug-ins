package numericDistributionsOnGroupsOfNodes;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;

public class Computer {

	public static NumericCharacteristic computeCommunitiesSizesDistribution(ISetOfGroupsOfNodes groupsOfNodes)	{
		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.DISTRIBUTION);

		for (IGroupOfNodes community : groupsOfNodes)	{
			result.addToDistribution(community.size());
		}

		return result;
	}


	public static NumericCharacteristic computeNodesInCommunitiesMembershipsDistribution(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes)	{
		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.DISTRIBUTION);

		for (IGraph.INode node : graph.getNodes())	{
			int numberOfCommunitiesMemberships = 0;
			for (IGroupOfNodes community : groupsOfNodes)	{
				if (community.contains(node))	{
					numberOfCommunitiesMemberships++;
				}
			}

			result.addToDistribution(numberOfCommunitiesMemberships);
		}

		return result;
	}
}
