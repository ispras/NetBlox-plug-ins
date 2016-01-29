package minerDijkstra;

import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class DijkstraMinerParametersSet extends GraphMiningParametersSet {
	private ValueFromRange<Integer> sourceNodeID = null;
	private ValueFromRange<Integer> targetNodeID = null;
	private boolean findSingle = true;

	public DijkstraMinerParametersSet(String algorithmNameInScenario, String algorithmDescriptionID,
			ValueFromRange<Integer> sourceNodeID, ValueFromRange<Integer> targetNodeID, boolean findSingle) {
		super(MiningJobBase.JobBase.GRAPH, algorithmNameInScenario, algorithmDescriptionID);

		this.sourceNodeID = sourceNodeID;
		this.targetNodeID = targetNodeID;
		//this.findForAll = findForAll;
		this.findSingle = findSingle;
	}


	public Integer getSourceNodeID()	{
		return (sourceNodeID==null) ? null : sourceNodeID.getValue();
	}
	public Integer getTargetNodeID()	{
		return (targetNodeID==null) ? null : targetNodeID.getValue();
	}
	public boolean findSingle()	{
		return findSingle;
	}


	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> list = appendNonNullParameter(null, sourceNodeID, "s");
		list = appendNonNullParameter(list, targetNodeID, "t");
		return list;
	}


	@Override
	public boolean hasParametersFromSomeRange() {
		return (sourceNodeID != null)  &&  (!sourceNodeID.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				(targetNodeID != null)  &&  (!targetNodeID.getRangeId().equals(RangeOfValues.NO_RANGE_ID))   ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;
		if ((sourceNodeID != null)  &&  (id.equals(sourceNodeID.getRangeId())))	{
			result = getSourceNodeID();
		}
		else if ((targetNodeID != null)  &&  (id.equals(targetNodeID.getRangeId())))	{
			result = getTargetNodeID();
		}
		else	{
			result = super.getValueForVariationId(id);
		}

		return result;
	}
}
