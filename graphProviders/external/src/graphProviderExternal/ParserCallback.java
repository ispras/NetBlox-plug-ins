package graphProviderExternal;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.exceptions.ScenarioException;
import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IGraphDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphsOneType;

public class ParserCallback implements IGraphDescriptionParser {
	private final static GraphDescriptionContentParser parser = new GraphDescriptionContentParser();

	@Override
	public DescriptionGraphsOneType parse(InputStream graphTagContent) {
		SAXParserFactory saxfactory = SAXParserFactory.newInstance();
        saxfactory.setNamespaceAware(true);

        try {
			saxfactory.newSAXParser().parse(graphTagContent, parser);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new ScenarioException(e);
		}

		return parser.getParsedDescription();
	}

}
