package numericQualityNMI;

import java.io.InputStream;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IGraphMiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IMeasureDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;

public class NMIInScenarioParserCallback implements IMeasureDescriptionParser, IGraphMiningDescriptionParser {

	@Override
	public DescriptionMeasure parseMeasureDescription(InputStream tagContent) {
		return new NMIMeasureDescription();
	}

	@Override
	public DescriptionGraphMiningAlgorithm parseMiningDescription(InputStream tagContent) {
		return new NMIAsGraphMiningDescription();
	}
}
