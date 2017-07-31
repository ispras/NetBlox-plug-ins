package graphProviderExternal;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.ExternalDataForGraphParser;
import ru.ispras.modis.NetBlox.parser.xmlParser.CommonXMLParser;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLStringValueProcessor;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class GraphDescriptionContentParser extends CommonXMLParser {
	private static final String TAG_DIRECTORY_PATH = "directoryPath";
	private static final String TAG_GRAPH_FILE_NAME = "graphFileName";
	private static final String TAG_REFERENCE = "reference";
	private static final String TAG_EXTERNAL_FOR_MINING = "externalForMining";
	private static final String TAG_EXTERNAL_FOR_MEASURES = "external";
	private static final String TAG_ATTRIBUTES = "attributes";

	private final XMLStringValueProcessor directoryPathProcessor;
	private final XMLStringValueProcessor graphFileNameProcessor;
	private final XMLStringValueProcessor referenceCoverFileNameProcessor;
	private final ExternalDataForGraphParser externalForMiningProcessor;
	private final ExternalDataForGraphParser externalForMeasuresProcessor;
	private final XMLStringValueProcessor attributesFileNameProcessor;

	private DescriptionGraphUncategorised graphDescription;


	public GraphDescriptionContentParser() {
		super();

		add(TAG_DIRECTORY_PATH, directoryPathProcessor = new XMLStringValueProcessor());
		add(TAG_GRAPH_FILE_NAME, graphFileNameProcessor = new XMLStringValueProcessor());
		add(TAG_REFERENCE, referenceCoverFileNameProcessor = new XMLStringValueProcessor());
		add(TAG_EXTERNAL_FOR_MINING, externalForMiningProcessor = new ExternalDataForGraphParser());
		add(TAG_EXTERNAL_FOR_MEASURES, externalForMeasuresProcessor = new ExternalDataForGraphParser());
		add(TAG_ATTRIBUTES, attributesFileNameProcessor = new XMLStringValueProcessor());
	}


	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

		graphDescription = new DescriptionGraphUncategorised();
	}


	@Override
	public void endDocument() throws SAXException	{
		super.endDocument();

		graphDescription.setDirectoryPathname(directoryPathProcessor.getText());
		graphDescription.setGraphFileName(graphFileNameProcessor.getText());

		String text = referenceCoverFileNameProcessor.getText();
		if (text != null  &&  !text.isEmpty())	{
			graphDescription.setReferenceCommunitiesRelativeFileName(text);
		}

		text = attributesFileNameProcessor.getText();
		if (text != null  &&  !text.isEmpty())	{
			graphDescription.setAttributesFileName(text);
		}

		RangeOfValues<String> setsOfGroupsFilenames = externalForMiningProcessor.getExternalFilenames();
		if (setsOfGroupsFilenames != null)	{
			graphDescription.setExternalForMiningFiles(setsOfGroupsFilenames, externalForMiningProcessor.getExternalDataType());
		}
		setsOfGroupsFilenames = externalForMeasuresProcessor.getExternalFilenames();
		if (setsOfGroupsFilenames != null)	{
			graphDescription.setExternalForCharacterizationFiles(setsOfGroupsFilenames, externalForMiningProcessor.getExternalDataType());
		}
	}


	public DescriptionGraphUncategorised getParsedDescription()	{
		return graphDescription;
	}
}
