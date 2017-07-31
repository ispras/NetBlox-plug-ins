package numericCommunitiesStats.computers;

import java.util.Map;

import numericCommunitiesStats.ParametersSetStats;
import numericCommunitiesStats.communitybased.CommonEdgeFrequency;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;

/**
 * Computes the frequency of existence of an edge between two nodes that share <i>n</i> 
 * common communities (the dependence of probability from the number of communities shared).
 * 
 * @author Kyrylo Chykhradze
 */
public class CommonEdgeFrequencyComputer extends DataInFilesStatisticComputer {
	public static final String NAME_IN_SCENARIO = "CommonEdgeFrequency";

	@Override
	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString,
			ParametersSetStats parameters) {
		
		boolean directed = graphOnDrive.isDirected();
		boolean weighted = graphOnDrive.isWeighted();
		Map<Object, Object> cep = CommonEdgeFrequency.apply(groupsOfNodesFilePathString, 
				graphOnDrive.getGraphFilePathString(), directed, weighted, parameters);
        NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.FUNCTION);

        for (Map.Entry<Object, Object> entry : cep.entrySet())
        {
            result.putToFunction((Double) entry.getKey(), (Double) entry.getValue() );
        }
        return result;
	}

}
