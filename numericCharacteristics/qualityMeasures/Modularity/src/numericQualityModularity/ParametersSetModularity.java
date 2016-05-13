package numericQualityModularity;

import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class ParametersSetModularity extends MeasureParametersSet {
	
	private static final String PARAMETERS_SEPARATOR = "; ";
	private ValueFromRange<String> formulaType = null;
	private ValueFromRange<String> belongingFunction = null;
	private ValueFromRange<String> belongingCoefficient= null;


	public ParametersSetModularity(String algorithmNameInScenario, String algorithmDescriptionID,
			MiningJobBase jobBase,
			ValueFromRange<String> formulaType,
			ValueFromRange<String> belongingFunction,
			ValueFromRange<String> belongingCoefficient)	{
		super(algorithmNameInScenario, jobBase);

		// We should set up defaults if some parameters are missing.
		if (formulaType == null) {
			formulaType = new ValueFromRange<String>(RangeOfValues.NO_RANGE_ID, "OURS");
		}
		this.formulaType = formulaType;
		
		if (belongingFunction == null) {
			String bf = "";
			switch (this.formulaType.getValue()) {
			case "OURS":
				bf = "INTERSECTION";
				break;
			case "LINK-RANK":
				bf = "INTERSECTION";
				break;
			case "DENSITY":
				bf = "PRODUCT";
				break;
			case "EDGE-BASED":
				bf = "EDGE";
				break;
			}
			belongingFunction = new ValueFromRange<String>(RangeOfValues.NO_RANGE_ID, bf);
		}
		this.belongingFunction = belongingFunction;
		
		if (belongingCoefficient == null) {
			belongingCoefficient = new ValueFromRange<String>(RangeOfValues.NO_RANGE_ID, "UNIFORM");
		}
		this.belongingCoefficient = belongingCoefficient;
	}

	public String getFormulaType() {
		return (formulaType == null) ? null : formulaType.getValue();
	}

	public String getBelongingFunction()	{
		return (belongingFunction==null) ? null : belongingFunction.getValue();
	}
	
	public String getbelongingCoefficient()	{
		return (belongingCoefficient==null) ? null : belongingCoefficient.getValue();
	}

	@Override
	public boolean hasParametersFromSomeRange() {
		return (formulaType != null)  &&  (!formulaType.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(belongingFunction != null)  &&  (!belongingFunction.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(belongingCoefficient != null)  &&  (!belongingCoefficient.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;
		if ((formulaType != null)  &&  (id.equals(formulaType.getRangeId())))	{
			result = getFormulaType();
		}
		else if ((belongingFunction != null)  &&  (id.equals(belongingFunction.getRangeId())))	{
			result = getbelongingCoefficient();
		}
		else if ((belongingCoefficient != null)  &&  (id.equals(belongingCoefficient.getRangeId())))	{
			result = getbelongingCoefficient();
		}
		else	{
			result = super.getValueForVariationId(id);
		}

		return result;
	}
	
	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> list = new LinkedList<Pair<String, String>>();
		
		list.add(new Pair<String, String>("formula=", formulaType.getValue().toString() + PARAMETERS_SEPARATOR));
		list.add(new Pair<String, String>("BF=", belongingFunction.getValue().toString() + PARAMETERS_SEPARATOR));
		list.add(new Pair<String, String>("BC=", belongingCoefficient.getValue().toString()));
		
		return list;
	}
}
