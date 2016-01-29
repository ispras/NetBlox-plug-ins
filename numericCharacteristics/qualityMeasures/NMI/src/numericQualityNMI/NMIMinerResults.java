package numericQualityNMI;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class NMIMinerResults extends MinerResults {
	private NumericCharacteristic resultValue = null;

	public NMIMinerResults(GraphMiningParametersSet miningParameters, NumericCharacteristic characteristic) {
		super(ResultsProvisionFormat.INTERNAL, MinedResultType.CHARACTERISTIC, miningParameters);
		resultValue = characteristic;
	}

	@Override
	public NumericCharacteristic getCharacteristic()	{
		return resultValue;
	}
}
