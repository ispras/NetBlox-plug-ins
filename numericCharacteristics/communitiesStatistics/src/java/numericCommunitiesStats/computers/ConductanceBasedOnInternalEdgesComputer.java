package numericCommunitiesStats.computers;

import numericCommunitiesStats.communitybased.ConductanceOnInternalEdges;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class ConductanceBasedOnInternalEdgesComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "commConductance";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString) {
		List<Object> conductances = ConductanceOnInternalEdges.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString());

		NumericCharacteristic result = makeCharacteristicOutOfDoubleValues(conductances);
		return result;
	}

}
