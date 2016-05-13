package minerLinkComm;

import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class ParametersSet_LinkComm extends GraphMiningParametersSet {
	private ValueFromRange<Float> threshold = null;

	public ParametersSet_LinkComm(String algorithmNameInScenario, String algorithmDescriptionID, ValueFromRange<Float> threshold) {
		super(MiningJobBase.JobBase.GRAPH, algorithmNameInScenario, algorithmDescriptionID);
		this.threshold = threshold;
	}


	public Float getThreshold()	{
		return threshold.getValue();
	}


	@Override
	public boolean hasParametersFromSomeRange() {
		return (threshold != null)  &&  (!threshold.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;

		if ((threshold != null)  &&  (id.equals(threshold.getRangeId())))	{
			result = threshold.getValue();
		}
		else	{
			result = super.getValueForVariationId(id);
		}

		return result;
	}



	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> list = appendNonNullParameter(null, threshold, "t");
		return list;
	}
}
