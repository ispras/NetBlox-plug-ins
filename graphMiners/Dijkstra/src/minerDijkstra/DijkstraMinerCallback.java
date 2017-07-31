package minerDijkstra;

import java.util.List;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.AGraphMiner;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.SupplementaryData;
import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;

public class DijkstraMinerCallback extends AGraphMiner {

	@Override
	public MinerResults mine(IGraph graph, SupplementaryData supplementaryData, GraphMiningParametersSet miningParameters) throws GraphMiningException	{
		DijkstraMinerParametersSet dijkstraParameters = (DijkstraMinerParametersSet) miningParameters;

		List<MinerResults> algorithmResults = ShortestPathComputer.run(graph, dijkstraParameters);

		if (algorithmResults.isEmpty())	{
			throw new GraphMiningException("No required paths could be found in graph.");
		}
		else if (algorithmResults.size() == 1)	{
			return algorithmResults.get(0);
		}
		else	{
			return new DijkstraResults(miningParameters, algorithmResults);
		}
	}
}
