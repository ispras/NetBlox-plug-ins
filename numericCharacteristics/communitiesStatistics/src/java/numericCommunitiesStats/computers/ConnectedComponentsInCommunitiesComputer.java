package numericCommunitiesStats.computers;

import numericCommunitiesStats.communitybased.ConnectedComponentsInCommunities;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class ConnectedComponentsInCommunitiesComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "ConnectedComponentsInCommunities";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString) {
		List<Object> ccic = ConnectedComponentsInCommunities.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString());

        NumericCharacteristic result = makeCharacteristicOutOfDoubleValues(ccic);
		return result;
	}

}
