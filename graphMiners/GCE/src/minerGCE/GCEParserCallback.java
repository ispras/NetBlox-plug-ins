package minerGCE;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.exceptions.ScenarioException;
import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IGraphMiningDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;

public class GCEParserCallback implements IGraphMiningDescriptionParser {
	private final static GCEDescriptionParser parser = new GCEDescriptionParser();

	@Override
	public DescriptionGraphMiningAlgorithm parseMiningDescription(InputStream tagContent) {
		SAXParserFactory saxfactory = SAXParserFactory.newInstance();
        saxfactory.setNamespaceAware(true);

        try {
			saxfactory.newSAXParser().parse(tagContent, parser);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new ScenarioException(e);
		}

		return parser.getParsedDescription();
	}
}
