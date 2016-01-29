package minerSLPA_GANXiS;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.MiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLFloatRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

/**
 * Parses the description of SLPA algorithm (GANXiS variant).
 * 
 * @author ilya
 */
public class SLPADescriptionParser extends MiningDescriptionParser {
	class IterationsProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> iterations = getValues();
			if (iterations != null  &&  !iterations.isEmpty())	{
				((DescriptionSLPA) getParsedDescription()).setMaximalNumbersOfIterations(iterations);
			}
		}
	}

	class ThresholdsProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Float> thresholds = getValues();
			if (thresholds != null  &&  !thresholds.isEmpty())	{
				((DescriptionSLPA) getParsedDescription()).setThresholds(thresholds);
			}
		}
	}

	class MinimalCommunitySizeProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> communitiesSizes = getValues();
			if (communitiesSizes != null  &&  !communitiesSizes.isEmpty())	{
				((DescriptionSLPA) getParsedDescription()).setMinimalCommunitiesSizes(communitiesSizes);
			}
		}
	}

	class MaximalCommunitySizeProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> communitiesSizes = getValues();
			if (communitiesSizes != null  &&  !communitiesSizes.isEmpty())	{
				((DescriptionSLPA) getParsedDescription()).setMaximalCommunitiesSizes(communitiesSizes);
			}
		}
	}


	private static final String TAG_ITERATIONS = "iterations";
	private static final String TAG_THRESHOLD = "threshold";
	private static final String TAG_MIN_C = "minC";
	private static final String TAG_MAX_C = "maxC";


	public SLPADescriptionParser()	{
		super();

		addTaggedParser(TAG_ITERATIONS, new IterationsProcessor());
		addTaggedParser(TAG_THRESHOLD, new ThresholdsProcessor());
		addTaggedParser(TAG_MIN_C, new MinimalCommunitySizeProcessor());
		addTaggedParser(TAG_MAX_C, new MaximalCommunitySizeProcessor());
	}


	@Override
	protected DescriptionGraphMiningAlgorithm createMinerDescription() {
		return new DescriptionSLPA();
	}
}
