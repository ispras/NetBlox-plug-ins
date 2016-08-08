package minerBigClAM;

import java.util.ArrayList;
import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class ParametersSetBigClAM extends GraphMiningParametersSet {
	private ValueFromRange<Integer> numberOfCommunitiesToDetect = null;
	private ValueFromRange<Integer> minCommunitiesNumber = null;
	private ValueFromRange<Integer> maxCommunitiesNumber = null;
	private ValueFromRange<Integer> numberOfTrialsOfCommunitiesNumbers = null;
	//numberOfThreads?
	private ValueFromRange<Float> alphaForBacktrackingLineSearch = null;
	private ValueFromRange<Float> betaForBacktrackingLineSearc = null;

	private static final Integer  DEFAULT_NUMBER_OF_COMMUNITIES_TO_DETECT = -1;

	public ParametersSetBigClAM(String algorithmNameInScenario, String algorithmDescriptionID, ValueFromRange<Integer> numberOfCommunitiesToDetect,
			ValueFromRange<Integer> minCommunitiesNumber, ValueFromRange<Integer> maxCommunitiesNumber, ValueFromRange<Integer> numberOfTrialsOfCommNum,
			ValueFromRange<Float> alphaForBacktracking, ValueFromRange<Float> betaForBacktracking) {
		super(MiningJobBase.JobBase.GRAPH, algorithmNameInScenario, algorithmDescriptionID);

		this.numberOfCommunitiesToDetect = numberOfCommunitiesToDetect;
		this.minCommunitiesNumber = minCommunitiesNumber;
		this.maxCommunitiesNumber = maxCommunitiesNumber;
		this.numberOfTrialsOfCommunitiesNumbers = numberOfTrialsOfCommNum;

		this.alphaForBacktrackingLineSearch = alphaForBacktracking;
		this.betaForBacktrackingLineSearc = betaForBacktracking;
	}


	@Override
	public boolean hasParametersFromSomeRange() {
		return (numberOfCommunitiesToDetect != null)  &&  (!numberOfCommunitiesToDetect.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(minCommunitiesNumber != null)  &&  (!minCommunitiesNumber.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(maxCommunitiesNumber != null)  &&  (!maxCommunitiesNumber.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(numberOfTrialsOfCommunitiesNumbers != null)  &&  (!numberOfTrialsOfCommunitiesNumbers.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(alphaForBacktrackingLineSearch != null)  &&  (!alphaForBacktrackingLineSearch.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(betaForBacktrackingLineSearc != null)  &&  (!betaForBacktrackingLineSearc.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;

		if ((numberOfCommunitiesToDetect != null)  &&  (id.equals(numberOfCommunitiesToDetect.getRangeId())))	{
			result = numberOfCommunitiesToDetect.getValue();
		}
		else if ((minCommunitiesNumber != null)  &&  (id.equals(minCommunitiesNumber.getRangeId())))	{
			result = minCommunitiesNumber.getValue();
		}
		else if ((maxCommunitiesNumber != null)  &&  (id.equals(maxCommunitiesNumber.getRangeId())))	{
			result = maxCommunitiesNumber.getValue();
		}
		else if ((numberOfTrialsOfCommunitiesNumbers != null)  &&  (id.equals(numberOfTrialsOfCommunitiesNumbers.getRangeId())))	{
			result = numberOfTrialsOfCommunitiesNumbers.getValue();
		}
		else if ((alphaForBacktrackingLineSearch != null)  &&  (id.equals(alphaForBacktrackingLineSearch.getRangeId())))	{
			result = alphaForBacktrackingLineSearch.getValue();
		}
		else if ((betaForBacktrackingLineSearc != null)  &&  (id.equals(betaForBacktrackingLineSearc.getRangeId())))	{
			result = betaForBacktrackingLineSearc.getValue();
		}
		else	{
			result = super.getValueForVariationId(id);
		}

		return result;
	}



	/**
	 * See the description in interface.
	 * Use as keys the CLI keys of the external binary app (the authors' implementation of the algorithms).
	 */
	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
		
		Integer numberOfCommunitiesToDetectValue = (numberOfCommunitiesToDetect!=null) ? numberOfCommunitiesToDetect.getValue() : DEFAULT_NUMBER_OF_COMMUNITIES_TO_DETECT;
		Pair<String, String> numberOfCommunitiesToDetectKeyValuePair = new Pair<String, String>("c", numberOfCommunitiesToDetectValue.toString());
		list.add(numberOfCommunitiesToDetectKeyValuePair);

		list = appendNonNullParameter(list, minCommunitiesNumber, "mc");
		list = appendNonNullParameter(list, maxCommunitiesNumber, "xc");
		list = appendNonNullParameter(list, numberOfTrialsOfCommunitiesNumbers, "nc");
		list = appendNonNullParameter(list, alphaForBacktrackingLineSearch, "sa");
		list = appendNonNullParameter(list, betaForBacktrackingLineSearc, "sb");

		return list;
	}
}
