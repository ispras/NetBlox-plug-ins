package minerSLPA_GANXiS;

import java.util.List;

import ru.ispras.modis.NetBlox.exceptions.PluginException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class SLPAResults extends MinerResults {
	private String coverFilePathstring = null;

	public SLPAResults(GraphMiningParametersSet miningParameters, String resultFilePathstring) {
		super(ResultsProvisionFormat.FILE_PATH_STRING, MinedResultType.NODES_GROUPS, miningParameters);

		this.coverFilePathstring = resultFilePathstring;
	}

	public SLPAResults(GraphMiningParametersSet miningParameters, List<MinerResults> multipleResults)	{
		super(miningParameters, multipleResults);
	}


	@Override
	public String getNodesGroupsFilePathString()	{
		if (getResultType() != MinedResultType.NODES_GROUPS)	{
			throw new PluginException("Required "+MinedResultType.NODES_GROUPS+" result while this is a container for "+getResultType()+".");
		}
		return coverFilePathstring;
	}
}
