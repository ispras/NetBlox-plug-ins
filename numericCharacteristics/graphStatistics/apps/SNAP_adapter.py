import snap

import math
import sys

TASK_DEGREE_DISTRIBUTION = 0
TASK_DIAMETER = 1
TASK_CLUSTERING_COEFFICIENT = 2



def printDiameter(graph, isGraphDirected):
	numberOfRandomStartingNodes = graph.GetNodes()
	if (numberOfRandomStartingNodes > 100):
		numberOfRandomStartingNodes = int(math.floor(0.1*numberOfRandomStartingNodes))
	diameter = snap.GetBfsFullDiam(graph, numberOfRandomStartingNodes, isGraphDirected)
	print diameter

def printDegreeDistribution(graph):
	degreesCountedArray = snap.TIntPrV()
	snap.GetOutDegCnt(graph, degreesCountedArray)
	for degreeCountPair in degreesCountedArray:
		degree = degreeCountPair.GetVal1()
		numberOfNodes = degreeCountPair.GetVal2()
		print "%d %d"  %  (degree, numberOfNodes)

def printClusteringCoefficient(graph):
	clusteringCoefficient = snap.GetClustCf (graph, -1)
	print clusteringCoefficient



pathToGraphFile = sys.argv[1]
isGraphDirected = sys.argv[2]
taskNumberCode = int(sys.argv[3])

graph = snap.LoadEdgeList(snap.PNGraph if isGraphDirected else snap.PUNGraph, pathToGraphFile, 0, 1)

if taskNumberCode == TASK_DIAMETER:
	printDiameter(graph, isGraphDirected)
elif taskNumberCode == TASK_DEGREE_DISTRIBUTION:
	printDegreeDistribution(graph)
elif taskNumberCode == TASK_CLUSTERING_COEFFICIENT:
	printClusteringCoefficient(graph)