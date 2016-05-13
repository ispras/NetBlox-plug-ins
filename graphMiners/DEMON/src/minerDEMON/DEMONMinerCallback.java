package minerDEMON;


import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.exceptions.PluginException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.AGraphMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class DEMONMinerCallback extends AGraphMiner {
	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException	{
		if (!(miningParameters instanceof ParametersSetDEMON)) {
			throw new PluginException("Mismatch of parameters type: "
					+ miningParameters.getAlgorithmName() + " parameters in "
					+ DEMON_Miner.PLUGIN_ID);
		}

		new DEMON_Miner().mine(graphOnDrive, (ParametersSetDEMON) miningParameters);
		
		return new DEMONResults(DEMON_Miner.OUTPUT_PATHSTRING, miningParameters);
	}

}
