package numericCommunitiesStats.computers;

import numericCommunitiesStats.ParametersSetStats;
import numericCommunitiesStats.communitybased.ConnectedComponentsInCommunities;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class ConnectedComponentsInCommunitiesComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "ConnectedComponentsInCommunities";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString,
			ParametersSetStats parameters) {
		boolean directed = graphOnDrive.isDirected();
		boolean weighted = graphOnDrive.isWeighted();
		List<Object> ccic = ConnectedComponentsInCommunities.apply(groupsOfNodesFilePathString, 
				graphOnDrive.getGraphFilePathString(), directed, weighted, parameters);

        NumericCharacteristic result = makeCharacteristicOutOfDoubleValues(ccic);
		return result;
	}

}
