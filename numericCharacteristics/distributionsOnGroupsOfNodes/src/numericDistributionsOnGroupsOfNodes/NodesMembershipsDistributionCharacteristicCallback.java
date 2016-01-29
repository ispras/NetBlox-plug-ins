package numericDistributionsOnGroupsOfNodes;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.numericCharacteristics.GroupsOfNodesSetCharacteristicComputer;
import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;

public class NodesMembershipsDistributionCharacteristicCallback extends
		GroupsOfNodesSetCharacteristicComputer {

	@Override
	public NumericCharacteristic run(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes, ISetOfGroupsOfNodes referenceGroupsOfNodes,
			MeasureParametersSet parameters) throws MeasureComputationException	{
		return Computer.computeNodesInCommunitiesMembershipsDistribution(graph, groupsOfNodes);
	}
}
