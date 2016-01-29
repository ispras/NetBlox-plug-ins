package minerMOSES;

import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.AGraphMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class MOSESMinerCallback extends AGraphMiner {

	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException {
		MOSESAdapter.mine(graphOnDrive.getGraphFilePathString());

		return packMinedData(miningParameters);
	}


	private MinerResults packMinedData(GraphMiningParametersSet miningParameters)	{
		return new MOSESResults(MOSESAdapter.DISCOVERED_COMMUNITIES_PATH_STRING, miningParameters);
	}
}
