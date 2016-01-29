package numericCommunitiesStats.computers;

import numericCommunitiesStats.communitybased.Cohesion;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.Tuple2;
import scala.collection.immutable.List;

public class CohesionPerCommunityViaTrianglesComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "cohesionViaTriangles";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString) {
		List<Tuple2<Object, Object>> sizesAndCohesions = Cohesion.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString());
		if (sizesAndCohesions == null  ||  sizesAndCohesions.isEmpty())	{
			return null;
		}

		NumericCharacteristic result = extractCharacteristicSignificant2ndValues(sizesAndCohesions);
		return result;
	}

}
