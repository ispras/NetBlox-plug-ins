package minerSLPA_GANXiS;

import java.util.ArrayList;
import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class ParametersSetSLPA extends GraphMiningParametersSet {
	private ValueFromRange<Integer> maximalNumberOfIterations = null;
	private ValueFromRange<Float> threshold = null;
	private ValueFromRange<Integer> minimalCommunitySize = null;
	private ValueFromRange<Integer> maximalCommunitySize = null;

	private static final Float DEFAULT_THRESHOLD_VALUE_WHEN_UNSPECIFIED = 0.1f;


	public ParametersSetSLPA(String algorithmNameInScenario, String algorithmDescriptionID, ValueFromRange<Integer> numberOfIterations, ValueFromRange<Float> threshold,
			ValueFromRange<Integer> minimalCommunitySize, ValueFromRange<Integer> maximalCommunitySize)	{
		super(MiningJobBase.JobBase.GRAPH, algorithmNameInScenario, algorithmDescriptionID, null);

		this.maximalNumberOfIterations = numberOfIterations;
		this.threshold = threshold;
		this.minimalCommunitySize = minimalCommunitySize;
		this.maximalCommunitySize = maximalCommunitySize;
	}

	public ParametersSetSLPA(ParametersSetSLPA toClone)	{
		super(MiningJobBase.JobBase.GRAPH, toClone.getAlgorithmName(), toClone.getAlgorithmDescriptionId());

		this.maximalNumberOfIterations = clone(toClone.maximalNumberOfIterations);
		this.threshold = clone(toClone.threshold);
		this.minimalCommunitySize = clone(toClone.minimalCommunitySize);
		this.maximalCommunitySize = clone(toClone.maximalCommunitySize);
	}


	private <CT> ValueFromRange<CT> clone(ValueFromRange<CT> cloned)	{	//XXX Extract to upper classes?
		return (cloned==null) ? null : cloned.clone();
	}


	public Integer getIterationsNumber()	{
		return (maximalNumberOfIterations==null) ? null : maximalNumberOfIterations.getValue();
	}
	public Float getThreshold()	{
		return (threshold==null) ? null : threshold.getValue();
	}
	public Integer getMinCommunitySize()	{
		return (minimalCommunitySize==null) ? null : minimalCommunitySize.getValue();
	}
	public Integer getMaxCommunitySize()	{
		return (maximalCommunitySize==null) ? null : maximalCommunitySize.getValue();
	}

	public boolean isDefaultThreshold()	{
		if (threshold == null)	{
			return true;
		}

		Float threshold = getThreshold();
		if (threshold.equals(0.01) || threshold.equals(0.05) || threshold.equals(0.1) || threshold.equals(0.15) || threshold.equals(0.2) ||
				threshold.equals(0.25) || threshold.equals(0.3) || threshold.equals(0.35) || threshold.equals(0.4) || threshold.equals(0.45) ||
				threshold.equals(0.5))	{
			return true;
		}

		return false;
	}

	public void setThreshold(Float thresholdValue)	{
		if (threshold == null)	{
			threshold = new ValueFromRange<Float>(RangeOfValues.NO_RANGE_ID, thresholdValue);
		}
		else	{
			threshold.setValue(thresholdValue);
		}
	}


	@Override
	public boolean hasParametersFromSomeRange() {
		return (maximalNumberOfIterations != null)  &&  (!maximalNumberOfIterations.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(threshold != null)  &&  (!threshold.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(minimalCommunitySize != null)  &&  (!minimalCommunitySize.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(maximalCommunitySize != null)  &&  (!maximalCommunitySize.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;
		if ((maximalNumberOfIterations != null)  &&  (id.equals(maximalNumberOfIterations.getRangeId())))	{
			result = getIterationsNumber();
		}
		else if ((threshold != null)  &&  (id.equals(threshold.getRangeId())))	{
			result = getThreshold();
		}
		else if ((minimalCommunitySize != null)  &&  (id.equals(minimalCommunitySize.getRangeId())))	{
			result = getMinCommunitySize();
		}
		else if ((maximalCommunitySize != null)  &&  (id.equals(maximalCommunitySize.getRangeId())))	{
			result = getMaxCommunitySize();
		}
		else	{
			result = super.getValueForVariationId(id);
		}

		return result;
	}


	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> list = appendNonNullParameter(null, maximalNumberOfIterations, "i");
		list = appendNonNullParameter(list, minimalCommunitySize, "minC");
		list = appendNonNullParameter(list, maximalCommunitySize, "maxC");

		Float thresholdValue = (threshold!=null) ? threshold.getValue() : DEFAULT_THRESHOLD_VALUE_WHEN_UNSPECIFIED;
		Pair<String, String> thresholdKeyValuePair = new Pair<String, String>("r", String.format("%.2f", thresholdValue));
		if (list == null)	{
			list = new ArrayList<Pair<String, String>>(1);
		}
		list.add(thresholdKeyValuePair);

		return list;
	}


	@Override
	public ParametersSetSLPA clone()	{
		return new ParametersSetSLPA(this);
	}
}
