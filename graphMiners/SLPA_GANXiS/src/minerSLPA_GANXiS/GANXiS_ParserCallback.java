package minerSLPA_GANXiS;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;

/**
 * The callback class for the scenario.parsers extension point for the GANXiS description parser.
 * 
 * @author ilya
 */
public class GANXiS_ParserCallback implements IParserSupplier {
	private static SLPADescriptionParser slpaDescriptionParser = null;

	@Override
	public XMLElementProcessor getXMLElementParser() {
		if (slpaDescriptionParser == null)	{
			slpaDescriptionParser = new SLPADescriptionParser();
		}
		return slpaDescriptionParser;
	}
}
