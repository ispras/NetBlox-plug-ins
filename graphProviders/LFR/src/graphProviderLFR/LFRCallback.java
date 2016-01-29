package graphProviderLFR;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;

public class LFRCallback implements IParserSupplier {
	private static LFRGraphParser lfrDescriptionParser = null;

	@Override
	public XMLElementProcessor getXMLElementParser() {
		if (lfrDescriptionParser == null)	{
			lfrDescriptionParser = new LFRGraphParser();
		}
		return lfrDescriptionParser;
	}

}
