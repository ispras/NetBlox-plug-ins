package minerMOSES;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.exceptions.ScenarioException;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.Utils;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IGraphMiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.xmlParser.CommonXMLParser;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLStringValueProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class ParserCallback implements IGraphMiningDescriptionParser {
	private static final DescriptionParser parser = new DescriptionParser();

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



class DescriptionParser extends CommonXMLParser	{
	class SupplementaryAlgosIdsProcessor extends XMLStringValueProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			String stringOfIds = getText();
			String[] ids = stringOfIds.split(Utils.DELIMITER);
			for (String stringId : ids)	{
				Utils.checkWhetherIsWordInScenario(stringId, TAG_SUPPLEMENTARY_ALGOS_IDS, "algorithm");

				minerDescription.addSupplementaryAlgorithmId(stringId);
			}
		}
	}

	class LaunchesProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> launchNumbers = getValues();
			if (launchNumbers != null  &&  !launchNumbers.isEmpty())	{
				minerDescription.setLaunchNumbers(launchNumbers);
			}
		}
	}


	private static final String TAG_SUPPLEMENTARY_ALGOS_IDS = "supplementaryAlgosIds";
	private static final String TAG_LAUNCH_NUMBERS = "launchNumbers";

	private DescriptionGCD_MOSES minerDescription;


	public DescriptionParser()	{
		super();

		add(TAG_SUPPLEMENTARY_ALGOS_IDS, new SupplementaryAlgosIdsProcessor());
		add(TAG_LAUNCH_NUMBERS, new LaunchesProcessor());
	}


	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

		minerDescription = new DescriptionGCD_MOSES();
	}


	public DescriptionGCD_MOSES getParsedDescription()	{
		return minerDescription;
	}
}