package numericDistributionsOnGroupsOfNodes;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphWithSingleGroupsOfNodesSetMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class CommunitiesSizesDistributionAsGraphMining extends GraphWithSingleGroupsOfNodesSetMiner {

	@Override
	public boolean canGetGroupsOfNodesToMineFromPreliminaryComputations() {
		//TODO Extend this to include such an option.
		return false;
	}


	@Override
	public MinerResults mine(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes,
			SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters) throws GraphMiningException	{
		NumericCharacteristic distribution = Computer.computeCommunitiesSizesDistribution(groupsOfNodes);

		return new DistributionResults(miningParameters, distribution);
	}
}
