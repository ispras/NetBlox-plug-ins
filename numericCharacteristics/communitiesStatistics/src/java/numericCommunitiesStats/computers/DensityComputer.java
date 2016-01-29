package numericCommunitiesStats.computers;

import numericCommunitiesStats.communitybased.Density;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class DensityComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "commDensity";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString) {
		List<Object> densities = Density.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString());

		NumericCharacteristic result = makeCharacteristicOutOfDoubleValues(densities);
		return result;
	}

}
