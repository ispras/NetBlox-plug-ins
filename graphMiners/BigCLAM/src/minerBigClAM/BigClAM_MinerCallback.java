package minerBigClAM;

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
public class BigClAM_MinerCallback extends AGraphMiner {
	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException	{
		if (!(miningParameters instanceof ParametersSetBigClAM))	{
			throw new PluginException("Mismatch of parameters type: "+miningParameters.getAlgorithmName()+" parameters in "+BigClAM_Miner.PLUGIN_ID);
		}

		BigClAM_Miner.mine(graphOnDrive, (ParametersSetBigClAM) miningParameters);

		return packDiscoveredCommunities(BigClAM_Miner.TEMP_OUTPUT_COMMUNITIES_PATH, miningParameters);
	}

	private MinerResults packDiscoveredCommunities(String pathToCommunities, GraphMiningParametersSet miningParameters)	{
		return new BigClAM_Results(pathToCommunities, miningParameters);
	}
}
