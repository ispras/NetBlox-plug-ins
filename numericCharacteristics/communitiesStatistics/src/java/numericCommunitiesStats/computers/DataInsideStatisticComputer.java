package numericCommunitiesStats.computers;

import numericCommunitiesStats.ParametersSetStats;
import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

public abstract class DataInsideStatisticComputer extends ExternalApplicationProvider {

	public abstract NumericCharacteristic compute(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes, 
			ParametersSetStats parameters);
}
