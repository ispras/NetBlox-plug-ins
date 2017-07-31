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
	public NumericCharacteristic run(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes, 
			ISetOfGroupsOfNodes referenceGroupsOfNodes,
			MeasureParametersSet parameters) throws MeasureComputationException	{
		String characteristicName = parameters.getCharacteristicNameInScenario();

		
		DataInsideStatisticComputer computer = ComputerFactory.getComputerForGroupsOfNodesInInternalRepresentation(characteristicName);
		if (computer == null)	{
			throw new MeasureComputationException("No computer for "+characteristicName+" in "+Activator.getContext().getBundle().getSymbolicName());
		}
		parametersCorrectnessChecker(graph.isDirected(), graph.isWeighted(), (ParametersSetStats)  parameters);
		NumericCharacteristic characteristic = computer.compute(graph, groupsOfNodes, (ParametersSetStats) parameters);
		return characteristic;
	}


	@Override
	public NumericCharacteristic run(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString, 
			String referenceGroupsOfNodesFilePathString,
			MeasureParametersSet parameters) throws MeasureComputationException	{
		
		String characteristicName = parameters.getCharacteristicNameInScenario();

		DataInFilesStatisticComputer computer = ComputerFactory.getComputerForGroupsOfNodesInFiles(characteristicName);
		if (computer == null)	{
			throw new MeasureComputationException("No computer for "+characteristicName+" in "
													+Activator.getContext().getBundle().getSymbolicName());
		}
		parametersCorrectnessChecker(graphOnDrive.isDirected(), graphOnDrive.isWeighted(), (ParametersSetStats) parameters);
		NumericCharacteristic characteristic = computer.compute(graphOnDrive, groupsOfNodesFilePathString, 
				(ParametersSetStats) parameters);
		return characteristic;
	}
	
	private void parametersCorrectnessChecker(boolean directed, boolean weighted, ParametersSetStats parameters)  
			throws MeasureComputationException	{
		String connectednessType = parameters.getConnectednessType();
		if (!(connectednessType.equals("SCC") || connectednessType.equals("WCC"))){
			throw new MeasureComputationException("Incorrect connectedness type");
		}
		if (!directed && connectednessType!=null) {
			System.out.println("WARNING:\t Type of connectedness is ignored for undirected graphs");
		}
		if (!directed && parameters.getTrianglesType()!=null) {
			System.out.println("WARNING:\t Type of triangles is ignored for undirected graphs");
		}
		if (!weighted && parameters.getMinWeight() != null) {
			System.out.println("WARNING:\t Minimal weight is ignored for unweighted graphs.");
		}
		String icdfType = parameters.getIcdfType();
		if (!directed && (icdfType != null)) {
			System.out.println("WARNING:\t icdfType is ignored for undirected graphs.");
		}
		if (!((icdfType.equals("ALL") || icdfType.equals("IN") || icdfType.equals("OUT")))) {
			throw new MeasureComputationException("icdfType could be 'ALL', 'IN' or 'OUT'");	
		}
		if (!directed && parameters.getBidirectional()) {
			System.out.println("WARNING:\t Bidirectional parameter is ignored for undirected graphs");
		}
		
	}
}
