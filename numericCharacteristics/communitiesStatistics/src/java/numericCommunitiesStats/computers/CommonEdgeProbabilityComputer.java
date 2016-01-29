package numericCommunitiesStats.computers;

import java.util.Map;

import numericCommunitiesStats.communitybased.CommonEdgeProbability;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;

/**
 * Computes the probability of existence of an edge between two nodes that share <i>n</i> 
 * common communities (the dependence of probability from the number of communities shared).
 * 
 * @author Kyrylo Chykhradze
 */
public class CommonEdgeProbabilityComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "CommonEdgeProbability";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString) {
		Map<Object, Object> cep = CommonEdgeProbability.apply(groupsOfNodesFilePathString, graphOnDrive.getGraphFilePathString());
        NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.FUNCTION);

        for (Map.Entry<Object, Object> entry : cep.entrySet())
        {
            result.putToFunction((Double) entry.getKey(), (Double) entry.getValue() );
        }
        return result;
	}

}
