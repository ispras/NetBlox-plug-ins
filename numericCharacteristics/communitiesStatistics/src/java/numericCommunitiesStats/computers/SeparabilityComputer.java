package numericCommunitiesStats.computers;

import numericCommunitiesStats.communitybased.Separability;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class SeparabilityComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "commSeparability";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString) {
		List<Object> separabilities = Separability.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString());

		NumericCharacteristic result = makeCharacteristicOutOfDoubleValues(separabilities);
		return result;
	}

}
