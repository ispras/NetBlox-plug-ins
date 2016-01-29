package numericDistributionsOnGroupsOfNodes;

import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class DistributionsMiningParameters extends GraphMiningParametersSet {

	public DistributionsMiningParameters(String algorithmNameInScenario, String algorithmDescriptionID) {
		super(MiningJobBase.JobBase.NODES_GROUPS_SET, algorithmNameInScenario, algorithmDescriptionID);
	}


	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		return null;
	}

}
