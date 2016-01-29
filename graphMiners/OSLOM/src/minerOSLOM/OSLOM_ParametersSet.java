package minerOSLOM;

import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class OSLOM_ParametersSet extends GraphMiningParametersSet {
	private int numberOfRunsForHigherHierarchicalLevel = 0;

	public OSLOM_ParametersSet(String algorithmNameInScenario, String algorithmDescriptionID) {
		super(MiningJobBase.JobBase.GRAPH, algorithmNameInScenario, algorithmDescriptionID);
	}

	public int getNumberOfRunsForHigherHierarchicalLevel()	{
		return numberOfRunsForHigherHierarchicalLevel;
	}


	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		return null;
	}
}
