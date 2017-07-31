package numericCommunitiesStats;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLFloatRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLStringValuesRangeProcessor;
import ru.ispras.modis.NetBlox.parser.xmlParser.CommonXMLParser;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLStringValueProcessor;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class CommunitiesStatisticDescriptionParser extends CommonXMLParser {
	class MinWeightProcessor extends XMLFloatRangeStringProcessor{
		
		@Override
		public void closeElement() {
			super.closeElement();
			RangeOfValues<Float> minWeights = getValues();
			measureDescription.setMinWeight(minWeights);
		}
	}

	class ConnectednessTypeProcessor extends XMLStringValuesRangeProcessor {
		@Override
		public void addValue(String value) {
			super.addValue(value.toUpperCase());
		}

		@Override
		public void closeElement() {
			super.closeElement();
			RangeOfValues<String> connectednessTypes = getValues();
			measureDescription.setConnectednessType(connectednessTypes);
		}
	}

	class TrianglesTypeProcessor extends XMLStringValuesRangeProcessor {
		@Override
		public void addValue(String value) {
			super.addValue(value.toUpperCase());
		}

		@Override
		public void closeElement() {
			super.closeElement();
			RangeOfValues<String> trianglesTypes = getValues();
			measureDescription.setTrianglesType(trianglesTypes);
		}
	}

	class IcdfTypeProcessor extends XMLStringValuesRangeProcessor{
		@Override
		public void addValue(String value) {
			super.addValue(value.toUpperCase());
		}
		
		@Override
		public void closeElement() {
			super.closeElement();

			RangeOfValues<String> icdfType = getValues();
			if (icdfType != null && !icdfType.isEmpty()) {
				measureDescription.setIcdfType(icdfType);
			}
		}
	}
	
	private class BooleanProcessor extends XMLStringValueProcessor {

		@Override
		public void closeElement() {
			super.closeElement();
			String stringValue = getText();
			if (stringValue != null && !stringValue.isEmpty()) {
				boolean value = Boolean.parseBoolean(stringValue);
				measureDescription.setBidirectional(value);
			}
		}
	}

	private static final String TAG_MIN_WEIGHT = "minWeight";
	private static final String TAG_CONNECTEDNESS_TYPE = "connectednessType";
	private static final String TAG_TRIANGLES_TYPE = "trianglesType";
	private static final String TAG_ICDF_TYPE = "icdfType";
	private static final String TAG_BIDIRECTIONAL = "bidirectional";

	private CommunitiesStatisticDescription measureDescription;

	public CommunitiesStatisticDescriptionParser() {
		super();

		add(TAG_MIN_WEIGHT, new MinWeightProcessor());
		add(TAG_CONNECTEDNESS_TYPE, new ConnectednessTypeProcessor());
		add(TAG_TRIANGLES_TYPE, new TrianglesTypeProcessor());
		add(TAG_ICDF_TYPE, new IcdfTypeProcessor());
		add(TAG_BIDIRECTIONAL, new BooleanProcessor());
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

		measureDescription = new CommunitiesStatisticDescription();
	}

	public CommunitiesStatisticDescription getParsedDescription() {
		return measureDescription;
	}
}
