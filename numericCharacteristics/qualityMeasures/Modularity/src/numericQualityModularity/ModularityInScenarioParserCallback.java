package numericQualityModularity;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.exceptions.ScenarioException;
import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IMeasureDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;

public class ModularityInScenarioParserCallback implements IMeasureDescriptionParser {

	private final static ModularityDescriptionParser parser = new ModularityDescriptionParser();
	
	@Override
	public DescriptionMeasure parseMeasureDescription(InputStream tagContent) {
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
