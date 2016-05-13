package minerCFinderAdapter;

import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.exceptions.PluginException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class CFinderResults extends MinerResults {
	private List<String> communitiesLines = null;


	public CFinderResults(GraphMiningParametersSet miningParameters)	{
		super(ResultsProvisionFormat.LIST_OF_STRINGS, MinedResultType.NODES_GROUPS, miningParameters);
		communitiesLines = new LinkedList<String>();
	}

	public CFinderResults(GraphMiningParametersSet miningParameters, List<MinerResults> multipleResults)	{
		super(miningParameters, multipleResults);
	}


	public void addCommunityLine(String line)	{
		if (getProvisionFormat() != ResultsProvisionFormat.LIST_OF_STRINGS  ||  getResultType() != MinedResultType.NODES_GROUPS)	{
			throw new PluginException("Mismatch of MinerResults container and the data passed to it in "+CFinderCPMMiner.PLUGIN_ID);
		}
		communitiesLines.add(line);
	}

	public boolean isEmpty()	{
		return communitiesLines.isEmpty();
	}


	@Override
	public List<String> getNodesGroupsStrings()	{
		return communitiesLines;
	}
}
