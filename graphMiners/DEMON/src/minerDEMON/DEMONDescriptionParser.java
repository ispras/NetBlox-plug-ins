package minerDEMON;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.MiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLFloatRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

/**
 * Parses the description of DEMON algorithm.
 * 
 * @author misha
 */
public class DEMONDescriptionParser extends MiningDescriptionParser {

	class EpsilonProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Float> epsilons = getValues();
			if (epsilons != null  &&  !epsilons.isEmpty())	{
				((DescriptionDEMON) getParsedDescription()).setEpsilons(epsilons);
			}
		}
	}

	class MinCommunitySizeProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> communitiesSizes = getValues();
			if (communitiesSizes != null  &&  !communitiesSizes.isEmpty())	{
				((DescriptionDEMON) getParsedDescription()).setMinCommunitiesSizes(communitiesSizes);
			}
		}
	}


	private static final String TAG_EPSILON = "epsilon";
	private static final String TAG_MIN_COMMUNITY_SIZE = "minCommunitySize";


	public DEMONDescriptionParser()	{
		super();

		addTaggedParser(TAG_EPSILON, new EpsilonProcessor());
		addTaggedParser(TAG_MIN_COMMUNITY_SIZE, new MinCommunitySizeProcessor());
	}


	@Override
	protected DescriptionGraphMiningAlgorithm createMinerDescription() {
		return new DescriptionDEMON();
	}
}
