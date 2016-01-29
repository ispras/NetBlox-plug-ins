package minerDijkstra;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IParserSupplier;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;

public class MinerDescriptionParserCallback implements IParserSupplier {
	private static DijkstraMinerDescriptionParser minerDescriptionParser = null;

	@Override
	public XMLElementProcessor getXMLElementParser() {
		if (minerDescriptionParser == null)	{
			minerDescriptionParser = new DijkstraMinerDescriptionParser();
		}
		return minerDescriptionParser;
	}

}
