package minerDijkstra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ru.ispras.modis.NetBlox.dataStructures.Graph;
import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;

public class ShortestPathComputer {
	public static List<MinerResults> run(IGraph graph, DijkstraMinerParametersSet parameters)	{
		List<MinerResults> results = new ArrayList<MinerResults>();

		Integer sourceNodeID = parameters.getSourceNodeID();
		if (sourceNodeID == null)	{
			for (IGraph.INode sourceNode : graph.getNodes())	{
				Integer targetNodeId = parameters.getTargetNodeID();
				if (targetNodeId != null  &&  sourceNode.equals(graph.getNode(targetNodeId)))	{
					continue;
				}
				List<MinerResults> resultsForSource = runFromSource(graph, sourceNode, parameters);
				results.addAll(resultsForSource);
			}
		}
		else	{
			IGraph.INode sourceNode = graph.getNode(sourceNodeID);
			List<MinerResults> resultsForSource = runFromSource(graph, sourceNode, parameters);
			results.addAll(resultsForSource);
		}

		return results;
	}

	private static List<MinerResults> runFromSource(IGraph graph, IGraph.INode sourceNode, DijkstraMinerParametersSet parameters)	{
		List<MinerResults> results = null;

		Map<IGraph.INode, Collection<IGraph.INode>> previousNodesInPath = runDijkstra(graph, sourceNode, parameters);

		Integer targetNodeID = parameters.getTargetNodeID();
		if (targetNodeID == null)	{
			results = new ArrayList<MinerResults>(graph.size()-1);

			for (IGraph.INode targetNode : graph.getNodes())	{
				if (targetNode.equals(sourceNode))	{
					continue;
				}
				DijkstraResults pathResult = assemblePathsToTarget(parameters, sourceNode, targetNode, previousNodesInPath, graph);
				results.add(pathResult);
			}
		}
		else	{
			IGraph.INode targetNode = graph.getNode(targetNodeID);
			DijkstraResults pathResult = assemblePathsToTarget(parameters, sourceNode, targetNode, previousNodesInPath, graph);

			results = new ArrayList<MinerResults>(1);
			results.add(pathResult);
		}

		return results;
	}

	private static DijkstraResults assemblePathsToTarget(DijkstraMinerParametersSet parameters, IGraph.INode sourceNode, IGraph.INode targetNode,
			Map<IGraph.INode, Collection<IGraph.INode>> previousNodesInPath, IGraph graph)	{
		Stack<IGraph.INode> basicReversePath = new Stack<IGraph.INode>();
		Collection<Stack<IGraph.INode>> allReversePaths = new ArrayList<Stack<IGraph.INode>>();
		allReversePaths.add(basicReversePath);

		assemblePaths(targetNode, previousNodesInPath, basicReversePath, allReversePaths);

		boolean findSingle = parameters.findSingle();
		parameters = new DijkstraMinerParametersSet(parameters.getAlgorithmName(), parameters.getAlgorithmDescriptionId(),
				new ValueFromRange<Integer>(RangeOfValues.NO_RANGE_ID, sourceNode.getId()),
				new ValueFromRange<Integer>(RangeOfValues.NO_RANGE_ID, targetNode.getId()),
				findSingle);

		DijkstraResults miningResults = null;
		if (findSingle)	{
			IGraph pathAsGraph = formatAPath(allReversePaths.iterator().next(), graph);
			miningResults = new DijkstraResults(parameters, pathAsGraph);
		}
		else	{
			Collection<IGraph> pathsAsGraphs = formatPathsInGraph(allReversePaths, graph);
			miningResults = new DijkstraResults(parameters, pathsAsGraphs);
		}
		return miningResults;
	}


	private static Map<IGraph.INode, Collection<IGraph.INode>> runDijkstra(IGraph graph, IGraph.INode sourceNode,
			DijkstraMinerParametersSet parameters)	{
		Integer targetNodeID = parameters.getTargetNodeID();
		boolean findSingle = parameters.findSingle();

		Map<IGraph.INode, Float> distancesFromSource = new HashMap<IGraph.INode, Float>(graph.size());
		Map<IGraph.INode, Collection<IGraph.INode>> previousNodesInPath = new HashMap<IGraph.INode, Collection<IGraph.INode>>(graph.size());

		List<IGraph.INode> unvisitedNodes = new LinkedList<IGraph.INode>(graph.getNodes());

		IGraph.INode currentNode = sourceNode;
		distancesFromSource.put(currentNode, 0f);
		unvisitedNodes.remove(currentNode);

		while (!unvisitedNodes.isEmpty())	{
			for (IGraph.INode neighbour : graph.getNeighbours(currentNode))	{
				if (!unvisitedNodes.contains(neighbour))	{
					continue;
				}

				addNodeIfItShortensPathToNeighbour(graph, currentNode, neighbour, previousNodesInPath, distancesFromSource, findSingle);
			}

			unvisitedNodes.remove(currentNode);
			currentNode = getNodeWithMinimumDistanceFromSource(unvisitedNodes, distancesFromSource);
			if (findSingle  &&  targetNodeID != null  &&  targetNodeID.equals(currentNode.getId()))	{
				break;
			}
		}

		return previousNodesInPath;
	}

	private static void addNodeIfItShortensPathToNeighbour(IGraph graph, IGraph.INode currentNode, IGraph.INode neighbour,
			Map<IGraph.INode, Collection<IGraph.INode>> previousNodesInPath, Map<IGraph.INode, Float> distancesFromSource, boolean findSingle)	{
		Float newDistance = distancesFromSource.get(currentNode) + graph.getEdgeWeight(currentNode, neighbour);
		Float existingDistance = distancesFromSource.get(neighbour);
		if (existingDistance == null  ||  newDistance < existingDistance)	{
			Collection<IGraph.INode> collectionOfPrevious = new LinkedList<IGraph.INode>();
			collectionOfPrevious.add(currentNode);
			previousNodesInPath.put(neighbour, collectionOfPrevious);

			distancesFromSource.put(neighbour, newDistance);
		}
		else if (newDistance == existingDistance  &&  !findSingle)	{
			Collection<IGraph.INode> collectionOfPrevious = previousNodesInPath.get(neighbour);
			collectionOfPrevious.add(currentNode);
		}
	}

	private static IGraph.INode getNodeWithMinimumDistanceFromSource(List<IGraph.INode> nodes, Map<IGraph.INode, Float> distances)	{
		IGraph.INode result = null;
		Float minDistance = Float.MAX_VALUE;
		for (IGraph.INode node : nodes)	{
			Float distance = distances.get(node);
			if (distance != null  &&  distance < minDistance)	{
				result = node;
				minDistance = distance;
			}
		}
		return result;
	}


	private static void assemblePaths(IGraph.INode targetNode, Map<IGraph.INode, Collection<IGraph.INode>> previousNodesInPath,
			Stack<IGraph.INode> reversePath, Collection<Stack<IGraph.INode>> allReversePaths)	{
		reversePath.add(targetNode);

		Collection<IGraph.INode> pathAncestors = previousNodesInPath.get(targetNode);
		if (pathAncestors != null)	{
			Collection<Stack<IGraph.INode>> reversePathsForEachAncestor = multiplyPathStacks(reversePath, pathAncestors.size(), allReversePaths);
			Iterator<Stack<IGraph.INode>> pathsIterator = reversePathsForEachAncestor.iterator();
	
			for (IGraph.INode ancestor : pathAncestors)	{
				assemblePaths(ancestor, previousNodesInPath, pathsIterator.next(), allReversePaths);
			}
		}
	}

	private static Collection<Stack<IGraph.INode>> multiplyPathStacks(Stack<IGraph.INode> pathStack, int requiredNumber,
			Collection<Stack<IGraph.INode>> allReversePaths)	{
		Collection<Stack<IGraph.INode>> reversePathsForEachAncestor = new ArrayList<Stack<IGraph.INode>>(requiredNumber);
		for (int i=0 ; i<requiredNumber ; i++)	{
			if (i==0)	{
				reversePathsForEachAncestor.add(pathStack);
			}
			else	{
				@SuppressWarnings("unchecked")
				Stack<IGraph.INode> newPath = (Stack<IGraph.INode>)pathStack.clone();

				reversePathsForEachAncestor.add(newPath);
				allReversePaths.add(newPath);
			}
		}
		return reversePathsForEachAncestor;
	}


	private static Collection<IGraph> formatPathsInGraph(Collection<Stack<IGraph.INode>> reversePaths, IGraph originalGraph)	{
		Collection<IGraph> result = new ArrayList<IGraph>(reversePaths.size());
		for (Stack<IGraph.INode> reversePath : reversePaths)	{
			IGraph pathAsGraph = formatAPath(reversePath, originalGraph);
			if (pathAsGraph != null)	{
				result.add(pathAsGraph);
			}
		}
		return result;
	}

	private static IGraph formatAPath(Stack<IGraph.INode> reversePath, IGraph originalGraph)	{
		Graph pathAsGraph = new Graph(originalGraph.isDirected(), originalGraph.isWeighted());

		IGraph.INode firstNode = reversePath.pop();
		if (reversePath.isEmpty())	{
			if (originalGraph.hasEdge(firstNode, firstNode))	{
				pathAsGraph.addEdge(firstNode, firstNode, originalGraph.getEdgeWeight(firstNode, firstNode));
			}
			else	{	//XXX Should warn about an error?
				return null;
			}
		}
		else	{
			while (!reversePath.isEmpty())	{
				IGraph.INode secondNode = reversePath.pop();
				pathAsGraph.addEdge(firstNode, secondNode, originalGraph.getEdgeWeight(firstNode, secondNode));
				firstNode = secondNode;
			}
		}

		return pathAsGraph;
	}
}
