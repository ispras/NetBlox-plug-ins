package pipeMinedCharacteristic;

import java.io.InputStream;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IMeasureDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;

public class ScenarioParsingCallback implements IMeasureDescriptionParser {

	@Override
	public DescriptionMeasure parseMeasureDescription(InputStream tagContent) {
		return new MeasureDescription();
	}

}
