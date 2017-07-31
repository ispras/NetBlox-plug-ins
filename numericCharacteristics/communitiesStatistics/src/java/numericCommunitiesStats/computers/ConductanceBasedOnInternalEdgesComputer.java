package numericCommunitiesStats.computers;

import numericCommunitiesStats.ParametersSetStats;
import numericCommunitiesStats.communitybased.ConductanceOnInternalEdges;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class ConductanceBasedOnInternalEdgesComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "commConductance";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString, 
			ParametersSetStats parameters) {
		boolean directed = graphOnDrive.isDirected();
		boolean weighted = graphOnDrive.isWeighted();
		List<Object> conductances = ConductanceOnInternalEdges.apply(groupsOfNodesFilePathString, 
				graphOnDrive.getGraphFilePathString(), directed, weighted, parameters);

		NumericCharacteristic result = makeCharacteristicOutOfDoubleValues(conductances);
		return result;
	}

}
