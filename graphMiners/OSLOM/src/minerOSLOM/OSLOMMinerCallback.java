package minerOSLOM;

import java.util.List;

import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.exceptions.PluginException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.AGraphMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class OSLOMMinerCallback extends AGraphMiner {

	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException {
		if (!(miningParameters instanceof OSLOM_ParametersSet))	{
			throw new PluginException("Mismatch of parameters type: "+miningParameters.getAlgorithmName()+" parameters in OSLOM plug-in.");
		}

		List<String> minedCommunitiesInLines = OSLOMMiner.mine(graphOnDrive, (OSLOM_ParametersSet) miningParameters);

		return new OSLOMResults(minedCommunitiesInLines, miningParameters);
	}
}
