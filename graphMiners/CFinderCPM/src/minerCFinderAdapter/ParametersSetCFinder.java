package minerCFinderAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class ParametersSetCFinder extends GraphMiningParametersSet {
	private ValueFromRange<Float> minWeightThreshold = null;
	private ValueFromRange<Float> maxWeightThreshold = null;
	private ValueFromRange<Float> maxCliquesSearchTime = null;
	private ValueFromRange<Float> weightIntensityThreshold = null;
	private ValueFromRange<Integer> kCliqueSize = null;
	
	private static final Integer DEFAULT_K_CLIQUE_SIZE = 3;

	public ParametersSetCFinder(String algorithmNameInScenario, String algorithmDescriptionID,
			ValueFromRange<Float> minWeightThreshold, ValueFromRange<Float> maxWeightThreshold,
			ValueFromRange<Float> maxCliquesSearchTime, ValueFromRange<Float> weightIntensityThreshold,
			ValueFromRange<Integer> kCliqueSize) {
		super(MiningJobBase.JobBase.GRAPH, algorithmNameInScenario, algorithmDescriptionID);

		this.minWeightThreshold = minWeightThreshold;
		this.maxWeightThreshold = maxWeightThreshold;
		this.maxCliquesSearchTime = maxCliquesSearchTime;
		this.weightIntensityThreshold = weightIntensityThreshold;
		this.kCliqueSize = kCliqueSize;
	}

	public ParametersSetCFinder(ParametersSetCFinder toClone)	{
		super(toClone.getJobBase(), toClone.getAlgorithmName(), toClone.getAlgorithmDescriptionId());

		this.minWeightThreshold = clone(toClone.minWeightThreshold);
		this.maxWeightThreshold = clone(toClone.maxWeightThreshold);
		this.maxCliquesSearchTime = clone(toClone.maxCliquesSearchTime);
		this.weightIntensityThreshold = clone(toClone.weightIntensityThreshold);
		this.kCliqueSize = clone(toClone.kCliqueSize);

		this.launchNumber = clone(toClone.launchNumber);
	}


	public Float getMinWeightThreshold()	{
		return (minWeightThreshold==null) ? null : minWeightThreshold.getValue();
	}

	public Float getMaxWeightThreshold()	{
		return (maxWeightThreshold==null) ? null : maxWeightThreshold.getValue();
	}

	public Float getMaxSearchTimeForCliques()	{
		return (maxCliquesSearchTime==null) ? null : maxCliquesSearchTime.getValue();
	}

	public Float getWeightIntensityThreshold()	{
		return (weightIntensityThreshold==null) ? null : weightIntensityThreshold.getValue();
	}

	public Integer getCliqueSize()	{
		return (kCliqueSize==null) ? DEFAULT_K_CLIQUE_SIZE : kCliqueSize.getValue();
	}


	public void setCliquesSize(Integer cliquesSize)	{
		if (kCliqueSize == null)	{
			kCliqueSize = new ValueFromRange<Integer>(RangeOfValues.NO_RANGE_ID, cliquesSize);
		}
		else	{
			kCliqueSize.setValue(cliquesSize);
		}
	}


	@Override
	public boolean hasParametersFromSomeRange() {
		return (minWeightThreshold != null)  &&  (!minWeightThreshold.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(maxWeightThreshold != null)  &&  (!maxWeightThreshold.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(maxCliquesSearchTime != null)  &&  (!maxCliquesSearchTime.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(weightIntensityThreshold != null)  &&  (!weightIntensityThreshold.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(kCliqueSize != null)  &&  (!kCliqueSize.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;

		if ((minWeightThreshold != null)  &&  (id.equals(minWeightThreshold.getRangeId())))	{
			result = minWeightThreshold.getValue();
		}
		else if ((maxWeightThreshold != null)  &&  (id.equals(maxWeightThreshold.getRangeId())))	{
			result = maxWeightThreshold.getValue();
		}
		else if ((maxCliquesSearchTime != null)  &&  (id.equals(maxCliquesSearchTime.getRangeId())))	{
			result = maxCliquesSearchTime.getValue();
		}
		else if ((weightIntensityThreshold != null)  &&  (id.equals(weightIntensityThreshold.getRangeId())))	{
			result = weightIntensityThreshold.getValue();
		}
		else if ((kCliqueSize != null)  &&  (id.equals(kCliqueSize.getRangeId())))	{
			result = kCliqueSize.getValue();
		}
		else	{
			result = super.getValueForVariationId(id);
		}

		return result;
	}


	/**
	 * See the description in interface.
	 * Use as keys the CFinder CLI keys.
	 */
	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> list = appendNonNullParameter(null, minWeightThreshold, "w");
		list = appendNonNullParameter(list, maxWeightThreshold, "W");

		list = appendNonNullParameter(list, maxCliquesSearchTime, "t");
		list = appendNonNullParameter(list, weightIntensityThreshold, "I");
		
		if (list == null)	{
			list = new ArrayList<Pair<String, String>>(1);
		}
		list.add(new Pair<String, String>("r", getCliqueSize().toString()));

		return list;
	}


	@Override
	public ParametersSetCFinder clone()	{
		return new ParametersSetCFinder(this);
	}
}
