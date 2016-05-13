package minerBigClAM;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.MiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLFloatRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

/**
 * Parses the description of BigClAM algorithm.
 * 
 * @author ilya
 */
public class BigClAMDescriptionParser extends MiningDescriptionParser {
	class NumbersOfCommunitiesProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> numbers = getValues();
			if (numbers != null  &&  !numbers.isEmpty())	{
				((DescriptionBigClAM) getParsedDescription()).setNumbersOfCommunities(numbers);
			}
		}
	}

	class MinNumbersOfCommunitiesProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> numbers = getValues();
			if (numbers != null  &&  !numbers.isEmpty())	{
				((DescriptionBigClAM) getParsedDescription()).setMinNumbersOfCommunities(numbers);
			}
		}
	}

	class MaxNumbersOfCommunitiesProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> numbers = getValues();
			if (numbers != null  &&  !numbers.isEmpty())	{
				((DescriptionBigClAM) getParsedDescription()).setMaxNumbersOfCommunities(numbers);
			}
		}
	}

	class NumbersOfTrialsProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> numbers = getValues();
			if (numbers != null  &&  !numbers.isEmpty())	{
				((DescriptionBigClAM) getParsedDescription()).setNumbersOfTrialsOfCommunitiesNumbers(numbers);
			}
		}
	}

	class AlphaForBactrackingLSProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Float> values = getValues();
			if (values != null  &&  !values.isEmpty())	{
				((DescriptionBigClAM) getParsedDescription()).setAlphaBacktrackingValues(values);
			}
		}
	}

	class BetaForBactrackingLSProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Float> values = getValues();
			if (values != null  &&  !values.isEmpty())	{
				((DescriptionBigClAM) getParsedDescription()).setBetaBacktrackingValues(values);
			}
		}
	}


	private static final String TAG_NUMBER_OF_COMMUNITIES = "numberOfCommunities";
	private static final String TAG_MIN_COMMUNITIES_NUMBER = "minCommunitiesNumber";
	private static final String TAG_MAX_COMMUNITIES_NUMBER = "maxCommunitiesNumber";
	private static final String TAG_NUMBER_OF_TRIES_OF_COMMUNITIES_NUMBERS = "numTrialsOfCommNumbers";

	//private static final String TAG_NUMBER_OF_THREADS = "numberOfThreads";

	private static final String TAG_ALPHA_FOR_BACKTRACKING_LINE_SEARCH = "alphaBacktracking";
	private static final String TAG_BETA_FOR_BACKTRACKING_LINE_SEARCH = "betaBacktracking";


	public BigClAMDescriptionParser()	{
		super();

		addTaggedParser(TAG_NUMBER_OF_COMMUNITIES, new NumbersOfCommunitiesProcessor());
		addTaggedParser(TAG_MIN_COMMUNITIES_NUMBER, new MinNumbersOfCommunitiesProcessor());
		addTaggedParser(TAG_MAX_COMMUNITIES_NUMBER, new MaxNumbersOfCommunitiesProcessor());
		addTaggedParser(TAG_NUMBER_OF_TRIES_OF_COMMUNITIES_NUMBERS, new NumbersOfTrialsProcessor());
		addTaggedParser(TAG_ALPHA_FOR_BACKTRACKING_LINE_SEARCH, new AlphaForBactrackingLSProcessor());
		addTaggedParser(TAG_BETA_FOR_BACKTRACKING_LINE_SEARCH, new BetaForBactrackingLSProcessor());
	}


	@Override
	protected DescriptionGraphMiningAlgorithm createMinerDescription() {
		return new DescriptionBigClAM();
	}
}
