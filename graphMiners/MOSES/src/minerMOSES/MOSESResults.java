package minerMOSES;

import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class MOSESResults extends MinerResults {
	private String minedCommunitiesFilePathString;

	public MOSESResults(String minedCommunitiesFilePathString, GraphMiningParametersSet miningParameters) {
		super(ResultsProvisionFormat.FILE_PATH_STRING, MinedResultType.NODES_GROUPS, miningParameters);

		this.minedCommunitiesFilePathString = minedCommunitiesFilePathString;
	}

	@Override
	public String getNodesGroupsFilePathString()	{
		return minedCommunitiesFilePathString;
	}
}
