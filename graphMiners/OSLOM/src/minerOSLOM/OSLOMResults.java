package minerOSLOM;

import java.util.List;

import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class OSLOMResults extends MinerResults {
	private List<String> minedCommunitiesInLines;

	public OSLOMResults(List<String> minedCommunitiesInLines, GraphMiningParametersSet miningParameters)	{
		super(ResultsProvisionFormat.LIST_OF_STRINGS, MinedResultType.NODES_GROUPS, miningParameters);

		this.minedCommunitiesInLines = minedCommunitiesInLines;
	}

	@Override
	public List<String> getNodesGroupsStrings()	{
		return minedCommunitiesInLines;
	}
}
