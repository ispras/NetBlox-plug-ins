package numericCommunitiesStats.computers;

import java.util.Map;

import numericCommunitiesStats.ParametersSetStats;
import numericCommunitiesStats.communitybased.NumberOfEdgesFromSize;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;

public class NumberOfEdgesFromSizeComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "NumberOfEdgesFromSize";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString,
			ParametersSetStats parameters) {
		boolean directed = graphOnDrive.isDirected();
		boolean weighted = graphOnDrive.isWeighted();
		Map<Object, Object> noe = NumberOfEdgesFromSize.apply(groupsOfNodesFilePathString,
				graphOnDrive.getGraphFilePathString(), directed, weighted, parameters);

        NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.FUNCTION);
        for (Map.Entry<Object, Object> entry : noe.entrySet())
        {
            result.putToFunction((Double) entry.getKey(), (Double) entry.getValue() );
        }
        return result;
	}
}

