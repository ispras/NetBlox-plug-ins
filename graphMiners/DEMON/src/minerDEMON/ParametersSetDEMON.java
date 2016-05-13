package minerDEMON;


import java.util.ArrayList;
import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class ParametersSetDEMON extends GraphMiningParametersSet {
	
	private ValueFromRange<Float> epsilon = null;
	private ValueFromRange<Integer> minCommunitySize = null;

	private static final Float DEFAULT_EPSILON = 0.25f;
	private static final Integer DEFAULT_MIN_COMMUNITY_SIZE = 3;

	public ParametersSetDEMON(String algorithmNameInScenario,
			String algorithmDescriptionID, ValueFromRange<Float> epsilon,
			ValueFromRange<Integer> minimalCommunitySize) {
		super(MiningJobBase.JobBase.GRAPH, algorithmNameInScenario, algorithmDescriptionID, null);

		this.epsilon = epsilon;
		this.minCommunitySize = minimalCommunitySize;
	}


	public Float getEpsilon()	{
		return (epsilon==null) ? DEFAULT_EPSILON : epsilon.getValue();
	}
	public Integer getMinCommunitySize()	{
		return (minCommunitySize==null) ? DEFAULT_MIN_COMMUNITY_SIZE : minCommunitySize.getValue();
	}


	@Override
	public boolean hasParametersFromSomeRange() {
		return (epsilon != null)  &&  (!epsilon.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(minCommunitySize != null)  &&  (!minCommunitySize.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;
		if ((epsilon != null)  &&  (id.equals(epsilon.getRangeId()))) {
			result = getEpsilon();
		}
		else if ((minCommunitySize != null)  &&  (id.equals(minCommunitySize.getRangeId()))) {
			result = getMinCommunitySize();
		}
		else {
			result = super.getValueForVariationId(id);
		}

		return result;
	}

	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>(2);
		
		list.add(new Pair<String, String>("minC", getMinCommunitySize().toString()));
		list.add(new Pair<String, String>("r", String.format("%.2f", getEpsilon())));

		return list;
	}
}
