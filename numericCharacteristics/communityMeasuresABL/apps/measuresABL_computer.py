from math import log

import networkx as nx
import sys

__author__ = 'misha'


MIN_NONTRIVIAL_COMM_SIZE = 3


def community_quality(graph, comm_list, is_directed):
    # TODO define format: do we have coordinates of users (or towers?) in a separate file?
    return -1


def get_unique_nodes(comm_list):
    unique_nodes = set()
    for comm in comm_list:
        unique_nodes = unique_nodes.union(set(comm))
    return unique_nodes


def community_coverage(graph, comm_list, is_directed):
    """
    The fraction of nodes which are covered by communities.
    """
    inside_comm = get_unique_nodes(comm_list)

    return 1.0 * len(inside_comm) / graph.number_of_nodes()


def mutual_info(x_array, y_array):
    mi = 0.0
    N = len(x_array)
    pairs = zip(x_array, y_array)

    x_dict = dict.fromkeys(x_array, 0)
    y_dict = dict.fromkeys(y_array, 0)
    pair_dict = dict.fromkeys(pairs, 0)

    for x, y in pairs:
        x_dict[x] += 1
        y_dict[y] += 1
        pair_dict[(x, y)] += 1

    for (x, y), count in pair_dict.items():
        p_x = 1.0 * x_dict[x] / N
        p_y = 1.0 * y_dict[y] / N
        p_xy = 1.0 * count / N
        mi += 1.0 * p_xy * log(p_xy / p_x / p_y, 2)

    return mi


def overlap_quality(graph, comm_list, is_directed):
    """
    Mutual information of actual overlap (i.e. membership) and total number of calls (both incoming and outgoing). No
    normalization was used.
    """
    try:
        pathToNumCalls = str(sys.argv[6])
    except IndexError:
        raise IndexError("One more parameter (path to number of calls file) must be specified for overlap quality"
                         " measure.")

    unique_nodes = get_unique_nodes(comm_list)

    nums_of_calls = []
    with open(pathToNumCalls) as num_calls_file:
        for line in num_calls_file:
            id_and_num = [x for x in line.split()]
            node, num = int(id_and_num[0]), id_and_num[1]
            # We add only nodes, that are inside communities. Order doesn't matter for MI.
            if node in unique_nodes:
                nums_of_calls.append(int(num))
    if len(nums_of_calls) != len(unique_nodes):
        raise Exception("Only %d nodes have attributes in file '%s', when there are %d nodes inside communities." %
                        (len(nums_of_calls), pathToNumCalls, len(unique_nodes)))

    overlaps_dict = {}
    for node in unique_nodes:
        overlaps_dict[node] = 0

    for comm in comm_list:
        for node in comm:
            overlaps_dict[node] += 1

    res = mutual_info(overlaps_dict.values(), nums_of_calls)
    return res


def overlap_coverage(graph, comm_list, is_directed):
    """
    Sum of all communities contents divided by their unique content.
    """
    num_of_participants = len(get_unique_nodes(comm_list))
    if num_of_participants == 0:
        return "NaN"	#-1.0
    num_of_memberships = sum([len(c) for c in comm_list])
    return 1.0 * num_of_memberships / num_of_participants


# Map: measure_code -> function
measureCodes = {0: community_quality,
                1: community_coverage,
                2: overlap_quality,
                3: overlap_coverage}

if __name__ == "__main__":

    pathToGraphFile = sys.argv[1]
    isGraphDirected = sys.argv[2] == 'true'
    isGraphWeighted = sys.argv[3] == 'true'
    pathToCommunities = sys.argv[4]
    code = int(sys.argv[5])

    g = nx.Graph()
    if isGraphDirected:
        g = nx.DiGraph()

    with open(pathToGraphFile) as graph_file:
        weight = 1
        for line in graph_file:
            abw = [x for x in line.split()]
            if isGraphWeighted:
                try:
                    weight = float(abw[2])
                except IndexError as e:
                    raise IOError("Graph is specified like weighted, but file %s doesn't contain weights (third element"
                                  " in a line is treated like weight of an edge)." % pathToGraphFile)
            g.add_edge(int(abw[0]), int(abw[1]), w=weight)

    # Checking if graph contains nodes.
    if g.number_of_nodes() == 0:
        print "NaN"	#(-1.0)
        quit()

    community_list = []
    with open(pathToCommunities) as comm_file:
        for line in comm_file:
            new_comm = [int(x) for x in line.split()]
            if len(new_comm) >= MIN_NONTRIVIAL_COMM_SIZE:
                community_list.append(new_comm)

    print float(measureCodes[code](g, community_list, isGraphDirected))
