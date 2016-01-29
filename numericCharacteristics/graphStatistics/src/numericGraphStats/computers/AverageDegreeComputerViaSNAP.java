package numericGraphStats.computers;

import java.io.InputStream;
import java.util.List;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.utils.Pair;

public class AverageDegreeComputerViaSNAP extends DegreesDistributionComputerViaSNAP {
	public static final String NAME_IN_SCENARIO = "averageDegree";

	@Override
	protected NumericCharacteristic extractResult(InputStream stream) throws MeasureComputationException	{
		List<Pair<Integer, Integer>> degreeCountPairs = extractPairsOfValues(stream);

		int cumulativeDegree = 0;
		int numberOfNodes = 0;
		for (Pair<Integer, Integer> degreeAndCount : degreeCountPairs)	{
			int value = degreeAndCount.get1st();
			int numberOfOccurences = degreeAndCount.get2nd();

			cumulativeDegree += value*numberOfOccurences;
			numberOfNodes += numberOfOccurences;
		}

		float averageDegree = cumulativeDegree / numberOfNodes;
		NumericCharacteristic result =  new NumericCharacteristic(NumericCharacteristic.Type.SINGLE_VALUE, averageDegree);

		return result;
	}
}
