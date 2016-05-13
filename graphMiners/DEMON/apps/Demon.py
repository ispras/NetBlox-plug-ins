import networkx as nx
import random
import sys, os


class Demon(object):
    """
    Based on
    Michele Coscia, Giulio Rossetti, Fosca Giannotti, Dino Pedreschi:
    DEMON: a local-first discovery method for overlapping communities.
    KDD 2012:615-623
    """

    def __init__(self):
        pass

    def execute(self, G, epsilon, weighted, min_community_size, path_to_output):
        """
        Execute Demon algorithm

        :param G: the networkx graph on which perform Demon
        :param epsilon: the tolerance required in order to merge communities (default 0.25)
        :param weighted: Whether the graph is weighted or not
        :param min_community_size: min nodes needed to form a community
        """

        #######
        self.G = G
        self.epsilon = epsilon
        self.min_community_size = min_community_size
        self.weighted = weighted
        for n in self.G.nodes():
            G.node[n]['communities'] = [n]
        #######

        all_communities = {}

        # total_nodes = len(nx.nodes(self.G))
        # actual = 0

        for ego in nx.nodes(self.G):
            # percentage = float(actual * 100) / total_nodes

            # if (int(percentage) % 10) == 0:
            #     print 'Ego-network analyzed: %d/100 (communities identified: %d)' % (
            #         percentage, len(all_communities))
            # actual += 1

            # ego_minus_ego
            ego_minus_ego = nx.ego_graph(self.G, ego, 1, False)
            community_to_nodes = self.__overlapping_label_propagation(ego_minus_ego, ego)

            # merging phase
            for c in community_to_nodes.keys():
                if len(community_to_nodes[c]) > self.min_community_size:
                    actual_community = community_to_nodes[c]
                    all_communities = self.__merge_communities(all_communities, actual_community)

        # print communities
        if not os.path.exists(os.path.dirname(path_to_output)):
            os.makedirs(os.path.dirname(path_to_output))
        with open(path_to_output, "w+") as out_file_com:
            for c in all_communities.keys():
                out_file_com.write("%s\n" % (' '.join([str(x) for x in sorted(c)])))
        # out_file_com.flush()
        # out_file_com.close()

        return

    def __overlapping_label_propagation(self, ego_minus_ego, ego, max_iteration=100):
        """
        :param max_iteration: number of desired iteration for the label propagation
        :param ego_minus_ego: ego network minus its center
        :param ego: ego network center
        """
        t = 0

        old_node_to_coms = {}

        while t < max_iteration:
            t += 1

            label_freq = {}
            node_to_coms = {}

            nodes = nx.nodes(ego_minus_ego)
            random.shuffle(nodes)

            count = -len(nodes)

            for n in nodes:
                n_neighbors = nx.neighbors(ego_minus_ego, n)

                if count == 0:
                    t += 1

                # compute the frequency of the labels
                for nn in n_neighbors:

                    communities_nn = [nn]

                    if nn in old_node_to_coms:
                        communities_nn = old_node_to_coms[nn]

                    for nn_c in communities_nn:
                        if nn_c in label_freq:
                            v = label_freq.get(nn_c)
                            # case of weighted graph
                            if self.weighted:
                                label_freq[nn_c] = v + ego_minus_ego.edge[nn][n]['w']
                            else:
                                label_freq[nn_c] = v + 1
                        else:
                            # case of weighted graph
                            if self.weighted:
                                label_freq[nn_c] = ego_minus_ego.edge[nn][n]['w']
                            else:
                                label_freq[nn_c] = 1

                # first run, random choosing of the communities among the neighbors labels
                if t == 1:
                    if not len(n_neighbors) == 0:
                        r_label = random.sample(label_freq.keys(), 1)
                        ego_minus_ego.node[n]['communities'] = r_label
                        old_node_to_coms[n] = r_label
                    count += 1
                    continue

                # choose the majority
                else:
                    labels = []
                    max_freq = -1

                    for l, c in label_freq.items():
                        if c > max_freq:
                            max_freq = c
                            labels = [l]
                        elif c == max_freq:
                            labels.append(l)

                    node_to_coms[n] = labels

                    if not n in old_node_to_coms or not set(node_to_coms[n]) == set(old_node_to_coms[n]):
                        old_node_to_coms[n] = node_to_coms[n]
                        ego_minus_ego.node[n]['communities'] = labels

            t += 1

        # build the communities reintroducing the ego
        community_to_nodes = {}
        for n in nx.nodes(ego_minus_ego):
            if len(nx.neighbors(ego_minus_ego, n)) == 0:
                ego_minus_ego.node[n]['communities'] = [n]

            c_n = ego_minus_ego.node[n]['communities']

            for c in c_n:

                if c in community_to_nodes:
                    com = community_to_nodes.get(c)
                    com.append(n)
                else:
                    nodes = [n, ego]
                    community_to_nodes[c] = nodes

        return community_to_nodes

    def __merge_communities(self, communities, actual_community):
        """
        :param communities: dictionary of communities
        :param actual_community: a community
        """

        # if the community is already present return
        if tuple(actual_community) in communities:
            return communities

        else:
            # search a community to merge with
            inserted = False
            # print len(communities)

            for test_community in communities.items():
                # if len(c) <= len(a_c):

                union = self.__generalized_inclusion(actual_community, test_community[0])
                # print union
                # communty to merge with found!
                if union is not None:
                    # print "%s %s %s\n" %(actual_community, test_community[0], union)
                    communities.pop(test_community[0])
                    # if not inserted:
                    communities[tuple(sorted(union))] = 0
                    inserted = True
                    break

            # not merged: insert the original community
            if not inserted:
                communities[tuple(sorted(actual_community))] = 0

        return communities

    def __generalized_inclusion(self, c1, c2):
        """
        :param c1: community
        :param c2: community
        """
        intersection = set(c2) & set(c1)
        # print "%s %s %s\n" %(set(c2[0]), set(c1), intersection)
        smaller_set = min(len(c1), len(c2))

        if len(intersection) == 0:
            return None

        if not smaller_set == 0:
            res = float(len(intersection)) / float(smaller_set)

        # founded!
        if res >= self.epsilon:
            union = set(c2) | set(c1)
            # print "%s %s %s %s\n" %(set(c2), set(c1), intersection, union)
            return union


if __name__ == "__main__":

    pathToGraphFile = sys.argv[1]
    pathToOutputFile = sys.argv[2]
    isGraphWeighted = sys.argv[3] == 'true'
    epsilon = float(sys.argv[4])
    minCommunitySize = int(sys.argv[5])

    g = nx.Graph()
    # if isGraphDirected:
    #     g = nx.DiGraph()

    with open(pathToGraphFile, "r") as graph_file:
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

    Demon().execute(g, epsilon, isGraphWeighted, minCommunitySize, pathToOutputFile)
