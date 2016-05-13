package numericQualityModularity;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.numericCharacteristics.GroupsOfNodesSetCharacteristicComputer;
import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;

public class ModularityMeasureCallback extends
		GroupsOfNodesSetCharacteristicComputer {

	@Override
	public NumericCharacteristic run(GraphOnDrive graphOnDrive,
			String groupsOfNodesFilePathString,
			String referenceGroupsOfNodesFilePathString,
			MeasureParametersSet parameters) throws MeasureComputationException {

		// Checking for validity of parameters.
		if (graphOnDrive.isDirected() && ((ParametersSetModularity)parameters).getbelongingCoefficient().equals("FRACTION")) {
			throw new MeasureComputationException("'FRACTION' belonging coefficient is not allowed for directed graphs.");
		}
		if (((ParametersSetModularity)parameters).getFormulaType().equals("DENSITY") &&
				((ParametersSetModularity)parameters).getBelongingFunction().equals("INTERSECTION")) {
			throw new MeasureComputationException("'INTERSECTION' belonging function is not allowed for 'DENSITY' formula.");
		}
		
		NumericCharacteristic result = new ModularityComputer().compute(
				graphOnDrive, groupsOfNodesFilePathString,
				(ParametersSetModularity) parameters);
		return result;
	}
}
