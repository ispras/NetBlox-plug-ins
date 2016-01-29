package numericGraphStats;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class GraphStatsMiningResults extends MinerResults {
	private NumericCharacteristic characteristicValue;

	public GraphStatsMiningResults(GraphMiningParametersSet miningParameters, NumericCharacteristic characteristicValue) {
		super(ResultsProvisionFormat.INTERNAL, MinedResultType.CHARACTERISTIC, miningParameters);
		this.characteristicValue = characteristicValue;
	}

	@Override
	public NumericCharacteristic getCharacteristic()	{
		return characteristicValue;
	}
}
