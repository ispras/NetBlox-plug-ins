package numericCommunitiesStats;

import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class ParametersSetStats extends MeasureParametersSet {
	
	private static final String PARAMETERS_SEPARATOR = "; ";
	
	private static final Float DEFAULT_MIN_WEIGHT = 0f;
	private static final String DEFAULT_TYPE_OF_CONNECTEDNESS = "WCC";
	private static final String DEFAULT_TYPE_OF_TRIANGLES = "WCC";
	private static final String DEFAULT_TYPE_OF_ICDF = "ALL";
	
	
	private ValueFromRange<Float> minWeight = null;

	private ValueFromRange<String> connectednessType = null;
	private ValueFromRange<String> trianglesType  = null;
	private ValueFromRange<String> icdfType = null;
	private boolean bidirectional;


	public ParametersSetStats(String algorithmNameInScenario, String algorithmDescriptionID,
			MiningJobBase jobBase,
			ValueFromRange<Float> minWeight,
			ValueFromRange<String> connectednessType,
			ValueFromRange<String> trianglesType,
			ValueFromRange<String> icdfType, boolean bidirectional)	{
		super(algorithmNameInScenario, jobBase);
		// We should set up defaults if some parameters are missing.
		if (minWeight == null) {
			minWeight = new ValueFromRange<Float>(RangeOfValues.NO_RANGE_ID, DEFAULT_MIN_WEIGHT);
		}
		this.minWeight = minWeight;
		
		if (connectednessType == null) {
			connectednessType = new ValueFromRange<String>(RangeOfValues.NO_RANGE_ID, DEFAULT_TYPE_OF_CONNECTEDNESS);
		}
		this.connectednessType = connectednessType;
		
		if (trianglesType == null) {
			trianglesType = new ValueFromRange<String>(RangeOfValues.NO_RANGE_ID, DEFAULT_TYPE_OF_TRIANGLES);
		}
		this.trianglesType = trianglesType;
		if (icdfType == null) {
			icdfType = new ValueFromRange<String>(RangeOfValues.NO_RANGE_ID, DEFAULT_TYPE_OF_ICDF);
		}
		this.icdfType = icdfType;

		this.bidirectional = bidirectional;
		
		
	}

	public Float getMinWeight() {
		return  minWeight.getValue();
	}

	public String getConnectednessType()	{
		return  connectednessType.getValue();
	}
	
	public String getTrianglesType()	{
		return trianglesType.getValue();
	}

	public String getIcdfType() {
		return icdfType.getValue();
	}
	
	public Boolean getBidirectional() {
		return  bidirectional;
	}

	@Override
	public boolean hasParametersFromSomeRange() {
		return (minWeight != null)  &&  (!minWeight.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(connectednessType != null)  &&  (!connectednessType.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(trianglesType != null)  &&  (!trianglesType.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(icdfType != null)  &&  (!icdfType.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;
		if ((minWeight != null)  &&  (id.equals(minWeight.getRangeId())))	{
			result = getMinWeight();
		}
		else if ((connectednessType != null)  &&  (id.equals(connectednessType.getRangeId())))	{
			result = getConnectednessType();
		}
		else if ((trianglesType != null)  &&  (id.equals(trianglesType.getRangeId())))	{
			result = getTrianglesType();
		}
		else if ((icdfType != null)  &&  (id.equals(icdfType.getRangeId())))	{
			result = getIcdfType();
		}
		else	{
			result = super.getValueForVariationId(id);
		}

		return result;
	}
	
	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> list = new LinkedList<Pair<String, String>>();
		
		list.add(new Pair<String, String>("minWeight=", minWeight.getValue().toString() + PARAMETERS_SEPARATOR));
		list.add(new Pair<String, String>("connectedness=", connectednessType.getValue().toString() + PARAMETERS_SEPARATOR));
		list.add(new Pair<String, String>("triangles=", trianglesType.getValue().toString() + PARAMETERS_SEPARATOR));
		list.add(new Pair<String, String>("icdf=", icdfType.getValue().toString() + PARAMETERS_SEPARATOR));
		list.add(new Pair<String, String>("bidirectional=", bidirectional+ PARAMETERS_SEPARATOR));
		
		return list;
	}
}
