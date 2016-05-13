package minerDEMON;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;

/**
 * The callback class for the scenario.parsers extension point for the DEMON
 * description parser.
 * 
 * @author misha
 */
public class DEMONParserCallback implements IParserSupplier {
	private static DEMONDescriptionParser demonDescriptionParser = null;

	@Override
	public XMLElementProcessor getXMLElementParser() {
		if (demonDescriptionParser == null) {
			demonDescriptionParser = new DEMONDescriptionParser();
		}
		return demonDescriptionParser;
	}
}
