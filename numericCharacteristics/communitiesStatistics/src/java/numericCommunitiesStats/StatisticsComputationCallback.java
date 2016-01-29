package numericCommunitiesStats;

import numericCommunitiesStats.computers.DataInFilesStatisticComputer;
import numericCommunitiesStats.computers.DataInsideStatisticComputer;
import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.numericCharacteristics.GroupsOfNodesSetCharacteristicComputer;
import ru.ispras.modis.NetBlox.scenario.MeasureParametersSet;

public class StatisticsComputationCallback extends GroupsOfNodesSetCharacteristicComputer {
	@Override
	public NumericCharacteristic run(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes, ISetOfGroupsOfNodes referenceGroupsOfNodes,
			MeasureParametersSet parameters) throws MeasureComputationException	{
		String characteristicName = parameters.getCharacteristicNameInScenario();

		DataInsideStatisticComputer computer = ComputerFactory.getComputerForGroupsOfNodesInInternalRepresentation(characteristicName);
		if (computer == null)	{
			throw new MeasureComputationException("No computer for "+characteristicName+" in "+Activator.getContext().getBundle().getSymbolicName());
		}

		NumericCharacteristic characteristic = computer.compute(graph, groupsOfNodes);
		return characteristic;
	}


	@Override
	public NumericCharacteristic run(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString, String referenceGroupsOfNodesFilePathString,
			MeasureParametersSet parameters) throws MeasureComputationException	{
		String characteristicName = parameters.getCharacteristicNameInScenario();

		DataInFilesStatisticComputer computer = ComputerFactory.getComputerForGroupsOfNodesInFiles(characteristicName);
		if (computer == null)	{
			throw new MeasureComputationException("No computer for "+characteristicName+" in "+Activator.getContext().getBundle().getSymbolicName());
		}

		NumericCharacteristic characteristic = computer.compute(graphOnDrive, groupsOfNodesFilePathString);
		return characteristic;
	}
}
