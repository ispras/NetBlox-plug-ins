package minerDijkstra;

import java.util.Collection;
import java.util.List;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class DijkstraResults extends MinerResults {
	private Collection<IGraph> resultingPaths = null;
	private IGraph resultingPath = null;

	public DijkstraResults(GraphMiningParametersSet miningParameters, List<MinerResults> multipleResult) {
		super(miningParameters, multipleResult);
	}

	public DijkstraResults(DijkstraMinerParametersSet parameters, Collection<IGraph> resultingPaths)	{
		super(ResultsProvisionFormat.INTERNAL, MinedResultType.SETS_OF_GRAPH_EDGES, parameters);
		this.resultingPaths = resultingPaths;
	}

	public DijkstraResults(DijkstraMinerParametersSet parameters, IGraph resultingPath)	{
		super(ResultsProvisionFormat.INTERNAL, MinedResultType.GRAPH_EDGES, parameters);
		this.resultingPath = resultingPath;
	}


	@Override
	public Collection<IGraph> getMultipleGraphStructures()	{
		return resultingPaths;
	}

	@Override
	public IGraph getMinedGraphStructure()	{
		return resultingPath;
	}
}
