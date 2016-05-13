package minerSLPA_GANXiS;

import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.exceptions.PluginException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.AGraphMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class GANXiS_MinerCallback extends AGraphMiner {
	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException	{
		if (!(miningParameters instanceof ParametersSetSLPA))	{
			throw new PluginException("Mismatch of parameters type: "+miningParameters.getAlgorithmName()+" parameters in "+GANXiS_Miner.PLUGIN_ID);
		}

		String path = GANXiS_Miner.mine(graphOnDrive, (ParametersSetSLPA) miningParameters); 

		return new SLPAResults(miningParameters, path);
	}

}
