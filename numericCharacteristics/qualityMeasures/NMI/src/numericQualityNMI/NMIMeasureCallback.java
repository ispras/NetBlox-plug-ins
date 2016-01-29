package numericQualityNMI;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.numericCharacteristics.GroupsOfNodesSetCharacteristicComputer;
import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;

public class NMIMeasureCallback extends GroupsOfNodesSetCharacteristicComputer {

	@Override
	public NumericCharacteristic run(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString, String referenceGroupsOfNodesFilePathString,
			MeasureParametersSet parameters) throws MeasureComputationException {
		Float nmiValue = NMIComputer.compute(referenceGroupsOfNodesFilePathString, groupsOfNodesFilePathString);

		NumericCharacteristic result = (nmiValue==null) ? null : new NumericCharacteristic(NumericCharacteristic.Type.SINGLE_VALUE, nmiValue);
		return result;
	}
}
