package minerLinkComm;

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
public class LinkCommunitiesMinerCallback extends AGraphMiner {

	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException	{
		if (!(miningParameters instanceof ParametersSet_LinkComm))	{
			throw new PluginException("Mismatch of parameters type: "+miningParameters.getAlgorithmName()+
					" parameters in "+LinkCommunitiesMiner.PLUGIN_ID);
		}

		return LinkCommunitiesMiner.mine(graphOnDrive, (ParametersSet_LinkComm) miningParameters);
	}

}
