package numericCommunitiesStats.computers;

import numericCommunitiesStats.ParametersSetStats;
import numericCommunitiesStats.communitybased.Cohesion;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.Tuple2;
import scala.collection.immutable.List;

public class AverageCohesionViaTrianglesComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "averageCohesionViaTriangles";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString, 
			ParametersSetStats parameters) {
		boolean directed = graphOnDrive.isDirected();
		boolean weighted = graphOnDrive.isWeighted();
		List<Tuple2<Object, Object>> sizesAndCohesions = 
				Cohesion.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString(), 
						directed, weighted, parameters);
		if (sizesAndCohesions == null  ||  sizesAndCohesions.isEmpty())	{
			return null;
		}

		float averageCohesion = (float) 0.0;
		int numberOfCommunities = 0;

		scala.collection.Iterator<Tuple2<Object, Object>> iterator = sizesAndCohesions.iterator();
		while (iterator.hasNext())	{
			Tuple2<Object, Object> sizeCohesionPair = iterator.next();
			Double cohesion = (Double) sizeCohesionPair._2();//sizeCohesionPair._2
			averageCohesion += cohesion;
			numberOfCommunities++;
		}

		averageCohesion /= numberOfCommunities;

		return new NumericCharacteristic(NumericCharacteristic.Type.SINGLE_VALUE, averageCohesion);
	}

}
