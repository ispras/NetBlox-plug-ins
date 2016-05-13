package numericQualityModularity;

import org.xml.sax.SAXException;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLStringValuesRangeProcessor;
import ru.ispras.modis.NetBlox.parser.xmlParser.CommonXMLParser;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class ModularityDescriptionParser extends CommonXMLParser {
	class FormulaTypeProcessor extends XMLStringValuesRangeProcessor {
		@Override
		public void addValue(String value) {
			super.addValue(value.toUpperCase());
		}

		@Override
		public void closeElement() {
			super.closeElement();
			RangeOfValues<String> formulaType = getValues();
			measureDescription.setFormulaType(formulaType);
		}
	}

	class BelongingFunctionProcessor extends XMLStringValuesRangeProcessor {
		@Override
		public void addValue(String value) {
			super.addValue(value.toUpperCase());
		}

		@Override
		public void closeElement() {
			super.closeElement();
			RangeOfValues<String> belongingFunction = getValues();
			measureDescription.setBelongingFunction(belongingFunction);
		}
	}

	class BelongingCoefficientProcessor extends XMLStringValuesRangeProcessor {
		@Override
		public void addValue(String value) {
			super.addValue(value.toUpperCase());
		}

		@Override
		public void closeElement() {
			super.closeElement();
			RangeOfValues<String> belongingCoefficients = getValues();
			measureDescription.setBelongingCoefficient(belongingCoefficients);
		}
	}

	private static final String TAG_FORMULA_TYPE = "formula";
	private static final String TAG_BELONGING_FUNCTION = "belongingFunction";
	private static final String TAG_BELONGING_COEFFICIENT = "belongingCoefficient";

	private ModularityMeasureDescription measureDescription;

	public ModularityDescriptionParser() {
		super();

		add(TAG_FORMULA_TYPE, new FormulaTypeProcessor());
		add(TAG_BELONGING_COEFFICIENT, new BelongingCoefficientProcessor());
		add(TAG_BELONGING_FUNCTION, new BelongingFunctionProcessor());
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

		measureDescription = new ModularityMeasureDescription();
	}

	public ModularityMeasureDescription getParsedDescription() {
		return measureDescription;
	}
}
