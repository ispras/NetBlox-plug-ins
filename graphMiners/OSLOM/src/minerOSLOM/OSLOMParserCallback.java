package minerOSLOM;

import java.io.InputStream;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IGraphMiningDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;

public class OSLOMParserCallback implements IGraphMiningDescriptionParser {
	@Override
	public DescriptionGraphMiningAlgorithm parseMiningDescription(InputStream tagContent) {
		return new DescriptionGCD_OSLOM();
	}

}
