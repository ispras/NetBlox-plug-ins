package numericCommunitiesMeasuresABL;

import java.io.InputStream;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IMeasureDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;

public class CommunitiesMeasuresABLInScenarioParserCallback implements IMeasureDescriptionParser {

	@Override
	public DescriptionMeasure parseMeasureDescription(InputStream tagContent) {
		return new CommunitiesMeasuresABLMeasureDescription();
	}
}
