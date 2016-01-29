package numericGraphStats;

import numericGraphStats.computers.SNAPBasedComputer;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.numericCharacteristics.GraphCharacteristicComputer;
import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;

public class GraphStatisticCallback extends GraphCharacteristicComputer {

	@Override
	public NumericCharacteristic run(GraphOnDrive graphOnDrive, MeasureParametersSet parameters) throws MeasureComputationException	{
		String characteristicName = parameters.getCharacteristicNameInScenario();

		SNAPBasedComputer computer = ComputerFactory.getComputer(characteristicName);
		if (computer == null)	{
			throw new MeasureComputationException("No computer for "+characteristicName+" in "+Activator.getContext().getBundle().getSymbolicName());
		}

		NumericCharacteristic characteristic = computer.compute(graphOnDrive);
		return characteristic;
	}
}
