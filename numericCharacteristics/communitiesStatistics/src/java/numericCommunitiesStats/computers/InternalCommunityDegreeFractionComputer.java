package numericCommunitiesStats.computers;

import numericCommunitiesStats.communitybased.ICDF;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class InternalCommunityDegreeFractionComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "ICDF";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString) {
		List<Object> icdf = ICDF.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString());

        NumericCharacteristic result = makeCharacteristicOutOfDoubleValues(icdf);
		return result;
	}

}
