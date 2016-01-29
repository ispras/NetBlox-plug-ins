package numericGraphStats.computers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.utils.Pair;

/**
 * Computes the distribution of nodes with different degrees in the graph
 * using external SNAP algorithm.
 * 
 * @author ilya
 */
public class DegreesDistributionComputerViaSNAP extends SNAPBasedComputer {
	public static final String NAME_IN_SCENARIO = "degreesDistribution";

	private static final int DEGREES_DISTRIBUTION_TASK_NUMBER_CODE = 0;
	private static final String WHITESPACE_CHARACTER_REGEX = "\\s";


	public DegreesDistributionComputerViaSNAP() {
		snapBasedTaskNumberCode = DEGREES_DISTRIBUTION_TASK_NUMBER_CODE;
	}


	@Override
	protected NumericCharacteristic extractResult(InputStream stream) throws MeasureComputationException	{
		List<Pair<Integer, Integer>> degreeCountPairs = extractPairsOfValues(stream);

		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.DISTRIBUTION);
		for (Pair<Integer, Integer> degreeAndCount : degreeCountPairs)	{
			int value = degreeAndCount.get1st();
			int numberOfOccurences = degreeAndCount.get2nd();
			result.addToDistribution(value, numberOfOccurences);
		}

		return result;
	}


	protected List<Pair<Integer, Integer>> extractPairsOfValues(InputStream stream) throws MeasureComputationException	{
		List<Pair<Integer, Integer>> listOfPairs = new LinkedList<Pair<Integer, Integer>>();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
		String line;
		try {
			while ((line = bufferedReader.readLine())  !=  null)	{
				String[] valueAndCountAsStrings = line.split(WHITESPACE_CHARACTER_REGEX);

				Pair<Integer, Integer> pair = new Pair<Integer, Integer>(
						Integer.parseInt(valueAndCountAsStrings[0]), Integer.parseInt(valueAndCountAsStrings[1]));

				listOfPairs.add(pair);
			}
		} catch (IOException e) {
			String errorMessage = "Couldn't get degrees distribution computed via SNAP: "+e.getMessage();
			throw new MeasureComputationException(errorMessage);
		}

		return listOfPairs;
	}
}
