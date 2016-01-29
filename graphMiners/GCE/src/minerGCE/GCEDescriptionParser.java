package minerGCE;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.Utils;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.xmlParser.CommonXMLParser;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLStringValueProcessor;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class GCEDescriptionParser extends CommonXMLParser {
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
	private static final String TAG_MINIMAL_CLIQUE_SIZE = "minimalCliqueSize";

	private final XMLStringValueProcessor minimalCliqueSizeParser;

	private DescriptionGCD_GCE minerDescription;


	public GCEDescriptionParser()	{
		super();

		add(TAG_SUPPLEMENTARY_ALGOS_IDS, new SupplementaryAlgosIdsProcessor());
		add(TAG_LAUNCH_NUMBERS, new LaunchesProcessor());
		add(TAG_MINIMAL_CLIQUE_SIZE, minimalCliqueSizeParser = new XMLStringValueProcessor());
	}


	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

		minerDescription = new DescriptionGCD_GCE();
	}


	@Override
	public void endDocument() throws SAXException	{
		super.endDocument();

		String text = minimalCliqueSizeParser.getText();
		if (text != null  &&  !text.isEmpty())	{
			minerDescription.setMinimalCliqueSize(Integer.parseInt(text));
		}
	}


	public DescriptionGCD_GCE getParsedDescription()	{
		return minerDescription;
	}
}
