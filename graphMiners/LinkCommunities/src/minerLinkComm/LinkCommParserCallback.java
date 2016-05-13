package minerLinkComm;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;

/**
 * The callback class for the scenario.parsers extension point for the Link Communities detection
 * description parser.
 * 
 * @author ilya
 */
public class LinkCommParserCallback implements IParserSupplier {
	private static LinkCommParser parser = null;

	@Override
	public XMLElementProcessor getXMLElementParser() {
		if (parser == null)	{
			parser = new LinkCommParser();
		}
		return parser;
	}

}
