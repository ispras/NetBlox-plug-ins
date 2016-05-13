package minerSLPA_GANXiS;

import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class SLPAResults extends MinerResults {
	private String minedCommunitiesFilePathString;


	public SLPAResults(GraphMiningParametersSet miningParameters, String minedCommunitiesFilePathString)	{
		super(ResultsProvisionFormat.FILE_PATH_STRING, MinedResultType.NODES_GROUPS, miningParameters);

		this.minedCommunitiesFilePathString = minedCommunitiesFilePathString;
	}


	@Override
	public String getNodesGroupsFilePathString()	{
		return minedCommunitiesFilePathString;
	}
}
