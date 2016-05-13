package minerCFinderAdapter;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.MiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLFloatRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

/**
 * Parses the description of CFinder Adapter launch.
 * 
 * @author ilya
 */
public class DescriptionParser extends MiningDescriptionParser {
	class MinWeightProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Float> values = getValues();
			if (values != null  &&  !values.isEmpty())	{
				((DescriptionCFinder) getParsedDescription()).setMinWeightTresholds(values);
			}
		}
	}

	class MaxWeightProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Float> values = getValues();
			if (values != null  &&  !values.isEmpty())	{
				((DescriptionCFinder) getParsedDescription()).setMaxWeightTresholds(values);
			}
		}
	}

	class MaxCliquesTimeProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Float> values = getValues();
			if (values != null  &&  !values.isEmpty())	{
				((DescriptionCFinder) getParsedDescription()).setMaxCliquesSearchTimes(values);
			}
		}
	}

	class WeightIntensityProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Float> values = getValues();
			if (values != null  &&  !values.isEmpty())	{
				((DescriptionCFinder) getParsedDescription()).setWeightIntensityTresholds(values);
			}
		}
	}

	class CliquesSizeProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> values = getValues();
			if (values != null  &&  !values.isEmpty())	{
				((DescriptionCFinder) getParsedDescription()).setCliquesSizes(values);
			}
		}
	}


	private static final String TAG_MIN_WEIGHT_THRESHOLD = "minWeightThreshold";
	private static final String TAG_MAX_WEIGHT_THRESHOLD = "maxWeightThreshold";
	private static final String TAG_MAX_CLIQUES_SEARCH_TIME = "maxCliquesSearchTime";
	private static final String TAG_WEIGHT_INTENSITY_THRESHOLD = "weightIntensityThreshold";
	private static final String TAG_K_CLIQUE_SIZE = "kCliqueSize";


	public DescriptionParser()	{
		super();

		addTaggedParser(TAG_MIN_WEIGHT_THRESHOLD, new MinWeightProcessor());
		addTaggedParser(TAG_MAX_WEIGHT_THRESHOLD, new MaxWeightProcessor());
		addTaggedParser(TAG_MAX_CLIQUES_SEARCH_TIME, new MaxCliquesTimeProcessor());
		addTaggedParser(TAG_WEIGHT_INTENSITY_THRESHOLD, new WeightIntensityProcessor());
		addTaggedParser(TAG_K_CLIQUE_SIZE, new CliquesSizeProcessor());
	}


	@Override
	protected DescriptionGraphMiningAlgorithm createMinerDescription() {
		return new DescriptionCFinder();
	}


	/*@Override
	public void closeElement()	{
		super.closeElement();
	}*/
}
