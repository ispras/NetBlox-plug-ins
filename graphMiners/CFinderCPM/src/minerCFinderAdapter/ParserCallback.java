package minerCFinderAdapter;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;

/**
 * The callback class for the scenario.parsers extension point for the CFinder adapter
 * launch description parser.
 * 
 * @author ilya
 */
public class ParserCallback implements IParserSupplier {
	private static DescriptionParser parser = null;

	@Override
	public XMLElementProcessor getXMLElementParser() {
		if (parser == null)	{
			parser = new DescriptionParser();
		}
		return parser;
	}

}
