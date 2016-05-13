package numericCommunitiesMeasuresABL;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.numericCharacteristics.GroupsOfNodesSetCharacteristicComputer;
import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;

public class CommunitiesMeasuresABLCallback extends GroupsOfNodesSetCharacteristicComputer {
	
	private static final int COMMUNITY_QUALITY_CODE = 0;
	private static final int COMMUNITY_COVERAGE_CODE = 1;
	private static final int OVERLAP_QUALITY_CODE = 2;
	private static final int OVERLAP_COVERAGE_CODE = 3;
	
	public static final String NUM_OF_CALLS_ATTRIBUTE = "USER-CALLS-NUMBER";

	@Override
	public NumericCharacteristic run(GraphOnDrive graphOnDrive,
			String groupsOfNodesFilePathString,
			String referenceGroupsOfNodesFilePathString,
			MeasureParametersSet parameters) throws MeasureComputationException	{
		
		String characteristicName = parameters.getCharacteristicNameInScenario();
		
		int measureCode = -1;
		switch(characteristicName) {
			case "community_quality":
				measureCode = COMMUNITY_QUALITY_CODE;
				throw new MeasureComputationException("Community_quality is not implemented yet in "+Activator.getContext().getBundle().getSymbolicName());
			case "community_coverage":
				measureCode = COMMUNITY_COVERAGE_CODE;
				break;			
			case "overlap_coverage":
				measureCode = OVERLAP_COVERAGE_CODE;
				break;
			default:
				throw new MeasureComputationException("No computer for "+characteristicName+" in "+Activator.getContext().getBundle().getSymbolicName());
		}

		NumericCharacteristic result = new CommunitiesMeasuresABLComputer()
				.compute(graphOnDrive, groupsOfNodesFilePathString, measureCode);
		return result;
	}
	
	@Override
	public NumericCharacteristic run(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes, ISetOfGroupsOfNodes referenceGroupsOfNodes,
			MeasureParametersSet parameters) throws MeasureComputationException {
		
		String characteristicName = parameters.getCharacteristicNameInScenario();
		
		if(!characteristicName.equals("overlap_quality")) {
			throw new MeasureComputationException("Expected 'overlap_quality' instead of "+characteristicName+" in "+Activator.getContext().getBundle().getSymbolicName());
		}
		
		String attributeName = NUM_OF_CALLS_ATTRIBUTE;

		NumericCharacteristic result = new CommunitiesMeasuresABLComputer()
				.compute(graph, groupsOfNodes, OVERLAP_QUALITY_CODE, attributeName);
		return result;
	}
}
