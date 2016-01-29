package minerGCE;

import java.io.InputStream;

import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.exceptions.PluginException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.AGraphMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class GCEMinerCallback extends AGraphMiner {
	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException	{
		if (!(miningParameters instanceof GCE_ParametersSet))	{
			throw new PluginException("Mismatch of parameters type: "+miningParameters.getAlgorithmName()+" parameters in "+
					Activator.getContext().getBundle().getSymbolicName());
		}

		InputStream gceOutput = GCEMiner.mine(graphOnDrive, (GCE_ParametersSet) miningParameters);

		return new GCEResults(gceOutput, miningParameters);
	}

}
