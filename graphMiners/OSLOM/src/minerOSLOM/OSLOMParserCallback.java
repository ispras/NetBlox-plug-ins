package minerOSLOM;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.MiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;

public class OSLOMParserCallback implements IParserSupplier {
	private OSLOMDescriptionParser parser = null;

	@Override
	public XMLElementProcessor getXMLElementParser() {
		if (parser == null)	{
			parser = new OSLOMDescriptionParser();
		}
		return parser;
	}


	private class OSLOMDescriptionParser extends MiningDescriptionParser	{
		@Override
		protected DescriptionGraphMiningAlgorithm createMinerDescription() {
			return new DescriptionGCD_OSLOM();
		}
	}
}
