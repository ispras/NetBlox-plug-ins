package pipeMinedCharacteristic;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.numericCharacteristics.CharacteristicEvaluator;
import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;

/**
 * Some numeric characteristic has been computed during graph mining. Pipe this characteristic
 * as a characteristics (measures) stage result.
 * 
 * @author ilya
 */
public class PipeMinedCallback extends CharacteristicEvaluator {

	@Override
	public NumericCharacteristic run(NumericCharacteristic characteristic, MeasureParametersSet parameters) throws MeasureComputationException {
		return characteristic;
	}

}
