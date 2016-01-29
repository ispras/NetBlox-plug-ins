package numericGraphStats;

import numericGraphStats.computers.SNAPBasedComputer;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.AGraphMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class GraphStatsMiningCallback extends AGraphMiner {

	@Override
	public MinerResults mine(GraphOnDrive graphOnDrive, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters)
			throws GraphMiningException	{
		String characteristicName = miningParameters.getAlgorithmName();

		SNAPBasedComputer computer = ComputerFactory.getComputer(characteristicName);

		try {
			NumericCharacteristic characteristic = computer.compute(graphOnDrive);

			return new GraphStatsMiningResults(miningParameters, characteristic);
		} catch (MeasureComputationException e) {
			throw new GraphMiningException(e);
		}
	}
}
