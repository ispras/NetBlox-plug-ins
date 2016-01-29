package minerGCE;

import java.io.InputStream;

import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class GCEResults extends MinerResults {
	private InputStream inputStream;

	public GCEResults(InputStream gceOutput, GraphMiningParametersSet miningParameters) {
		super(ResultsProvisionFormat.STREAM, MinedResultType.NODES_GROUPS, miningParameters);

		inputStream = gceOutput;
	}

	@Override
	public InputStream getNodesGroupsStream()	{
		return inputStream;
	}
}
