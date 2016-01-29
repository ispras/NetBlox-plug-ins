package numericDistributionsOnGroupsOfNodes;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class DistributionResults extends MinerResults {
	private NumericCharacteristic resultValue = null;

	public DistributionResults(GraphMiningParametersSet miningParameters, NumericCharacteristic characteristic) {
		super(ResultsProvisionFormat.INTERNAL, MinedResultType.CHARACTERISTIC, miningParameters);
		resultValue = characteristic;
	}

	@Override
	public NumericCharacteristic getCharacteristic()	{
		return resultValue;
	}
}
