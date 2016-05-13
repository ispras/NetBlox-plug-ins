package minerLinkComm;

import ru.ispras.modis.NetBlox.exceptions.ScenarioException;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.MiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLFloatRangeStringProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class LinkCommParser extends MiningDescriptionParser {
	private static final String TAG_THRESHOLD = "threshold";

	private final XMLFloatRangeStringProcessor thresholdsProcessor;


	public LinkCommParser()	{
		super();
		addTaggedParser(TAG_THRESHOLD, thresholdsProcessor = new XMLFloatRangeStringProcessor());
	}


	@Override
	public void closeElement()	{
		super.closeElement();

		RangeOfValues<Float> thresholdValues = thresholdsProcessor.getValues();
		if (thresholdValues == null  ||  thresholdValues.isEmpty())	{
			throw new ScenarioException("'threshold' parameter is obligate for "+LinkCommunitiesMiner.PLUGIN_ID+" plug-in.");
		}
		((DescriptionLinkComm) getParsedDescription()).setThresholds(thresholdValues); 
	}


	@Override
	protected DescriptionGraphMiningAlgorithm createMinerDescription() {
		return new DescriptionLinkComm();
	}
}
