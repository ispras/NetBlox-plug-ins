package numericCommunitiesStats.computers;

import numericCommunitiesStats.ParametersSetStats;
import numericCommunitiesStats.communitybased.Separability;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class SeparabilityComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "commSeparability";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString, 
			ParametersSetStats parameters) {
		boolean directed = graphOnDrive.isDirected();
		boolean weighted= graphOnDrive.isWeighted();
		List<Object> separabilities = Separability.apply(groupsOfNodesFilePathString, 
				graphOnDrive.getGraphFilePathString(), directed, weighted, parameters);

		NumericCharacteristic result = makeCharacteristicOutOfDoubleValues(separabilities);
		
		return result;
	}

}
