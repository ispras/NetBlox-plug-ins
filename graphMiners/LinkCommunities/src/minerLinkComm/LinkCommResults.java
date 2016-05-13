package minerLinkComm;

import java.util.List;

import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class LinkCommResults extends MinerResults {
	private List<String> nodeClustersLines;

	public LinkCommResults(List<String> nodeClustersLines, GraphMiningParametersSet miningParameters)	{
		super(ResultsProvisionFormat.LIST_OF_STRINGS, MinedResultType.NODES_GROUPS, miningParameters);
		this.nodeClustersLines = nodeClustersLines;
	}

	@Override
	public List<String> getNodesGroupsStrings()	{
		return nodeClustersLines;
	}
}
