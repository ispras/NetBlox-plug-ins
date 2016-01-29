package minerSLPA_GANXiS;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;

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
