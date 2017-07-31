package numericCommunitiesStats.computers;

import numericCommunitiesStats.ParametersSetStats;
import numericCommunitiesStats.communitybased.ConnectedComponentsInCommunities;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.collection.immutable.List;

public class ConnectedComponentsInCommunitiesDistributionComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "ConnectedComponentsInCommunitiesDistribution";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString,
			ParametersSetStats parameters) {
		boolean directed = graphOnDrive.isDirected();
		boolean weighted = graphOnDrive.isWeighted();
		List<Object> ccic = ConnectedComponentsInCommunities.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString(),
				directed, weighted, parameters);

		if (ccic == null  ||  ccic.isEmpty())	{
			return null;
		}

		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.DISTRIBUTION);
		scala.collection.Iterator<Object> iterator = ccic.iterator();
		while (iterator.hasNext())	{
			Integer value = ((Double) iterator.next()).intValue();
			result.addToDistribution(value);
		}
		return result;
	}

}
