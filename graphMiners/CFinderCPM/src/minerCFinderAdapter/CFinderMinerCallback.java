package minerCFinderAdapter;

import java.util.List;

import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.exceptions.PluginException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.AGraphMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

/**
 * The callback class for the graph.miners extension point. Calls the class that will
 * perform the mining process and packs and returns the results to host.
 * 
 * @author ilya
 */
public class CFinderMinerCallback extends AGraphMiner {
	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException	{
		if (!(miningParameters instanceof ParametersSetCFinder))	{
			throw new PluginException("Mismatch of parameters type: "+miningParameters.getAlgorithmName()+" parameters in "+CFinderCPMMiner.PLUGIN_ID);
		}

		List<MinerResults> minedCommunities = CFinderCPMMiner.mine(graphOnDrive, (ParametersSetCFinder) miningParameters);

		return new CFinderResults(miningParameters, minedCommunities);
	}

}
