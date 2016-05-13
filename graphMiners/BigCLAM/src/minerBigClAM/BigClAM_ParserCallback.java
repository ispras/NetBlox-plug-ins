package minerBigClAM;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;

/**
 * The callback class for the scenario.parsers extension point for the BigClAM community detection
 * description parser.
 * 
 * @author ilya
 */
public class BigClAM_ParserCallback implements IParserSupplier {
	private static BigClAMDescriptionParser parser = null;

	@Override
	public XMLElementProcessor getXMLElementParser() {
		if (parser == null)	{
			parser = new BigClAMDescriptionParser();
		}
		return parser;
	}
}
