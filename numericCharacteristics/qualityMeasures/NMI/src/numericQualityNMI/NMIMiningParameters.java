package numericQualityNMI;

import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class NMIMiningParameters extends GraphMiningParametersSet {

	public NMIMiningParameters(String algorithmNameInScenario, String algorithmDescriptionID) {
		super(MiningJobBase.JobBase.MULTIPLE_SETS_OF_GROUPS_OF_NODES, algorithmNameInScenario, algorithmDescriptionID);
	}


	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		return null;
	}

}
