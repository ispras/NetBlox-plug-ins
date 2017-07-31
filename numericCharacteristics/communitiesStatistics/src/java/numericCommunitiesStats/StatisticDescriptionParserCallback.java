package numericCommunitiesStats;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.exceptions.ScenarioException;
import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IMeasureDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;

public class StatisticDescriptionParserCallback implements IMeasureDescriptionParser {

	private final static CommunitiesStatisticDescriptionParser parser = new CommunitiesStatisticDescriptionParser();
	
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
