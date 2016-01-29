package numericQualityNMI;

import java.util.List;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphWithSetsOfGroupsOfNodesMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class NMIAsGraphMiningCallback extends GraphWithSetsOfGroupsOfNodesMiner {

	@Override
	public boolean canGetSetsOfGroupsToMineFromPreliminaryComputations() {
		//TODO Extend this to include such an option.
		return false;
	}


	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, List<String> groupsOfNodesSetsFilesPathStrings, SupplementaryData supplementaryData,
			GraphMiningParametersSet miningParameters) throws GraphMiningException	{
		if (groupsOfNodesSetsFilesPathStrings.size() != 2)	{
			String error = "NMI graph miner deals with 2 set of groups of nodes, while there have been passed "+groupsOfNodesSetsFilesPathStrings.size();
			throw new GraphMiningException(error);
		}

		try {
			Float nmiValue = NMIComputer.compute(groupsOfNodesSetsFilesPathStrings.get(0), groupsOfNodesSetsFilesPathStrings.get(1));

			NumericCharacteristic result = (nmiValue==null) ? null : new NumericCharacteristic(NumericCharacteristic.Type.SINGLE_VALUE, nmiValue);

			return new NMIMinerResults(miningParameters, result);
		} catch (MeasureComputationException e) {
			throw new GraphMiningException(e.getMessage());
		}
	}
}
