package minerBigClAM;

import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class BigClAM_Results extends MinerResults {
	private String minedCommunitiesFilePathString;


	public BigClAM_Results(String minedCommunitiesFilePathString, GraphMiningParametersSet miningParameters)	{
		super(ResultsProvisionFormat.FILE_PATH_STRING, MinedResultType.NODES_GROUPS, miningParameters);

		this.minedCommunitiesFilePathString = minedCommunitiesFilePathString;
	}


	@Override
	public String getNodesGroupsFilePathString()	{
		return minedCommunitiesFilePathString;
	}
}
