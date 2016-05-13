import sys
from math import exp
from itertools import product, combinations, combinations_with_replacement
import networkx as nx
import time

__author__ = 'misha'


class ModularityCalculator(object):
    def __init__(self, graph, is_directed):
        # Always weighted, may be directed or undirected.
        self.graph = graph

        self.edge_based = self.edge_based_weighted_overlapping
        self.our_version = self.weighted_overlapping
        # self.density = self.density_directed_overlapping  # Both for directed and not

        if is_directed:
            self.edge_based = self.edge_based_weighted_directed_overlapping
            self.our_version = self.weighted_directed_overlapping
            # self.density = self.density_directed_overlapping

    def edge_based_weighted_overlapping(self, community_list, bf, bc):
        """ Edge-based formula was designed for unweighted only. I extended it like: A_ik -> w_ik, also adapted to
        undirected case.
        """
        # Total weight
        m = 1.0 * sum([x[2] for x in list(self.graph.edges_iter(data='w'))])
        V = self.graph.nodes()
        Q = 0.0
        # Iterating over communities
        for c in community_list:
            # start = time.time()
            # print "comm %s" % str(c)

            # List of weighted degrees and bf coefficients for each node in community (for faster computation)
            wdegree = {}
            bf_in = {}
            bf_out = {}
            for node in c:
                wdegree[node] = 1.0 * sum([x[2] for x in self.graph.edges([node], data='w')])
                bf_in[node] = 1.0 * sum([bf(self.graph, k, node, c, c, community_list, bc) for k in V])
                bf_out[node] = 1.0 * sum([bf(self.graph, node, k, c, c, community_list, bc) for k in V])

            # Iterating over all edges
            for i in c:
                for j in c:
                    w_ij = 0.0
                    if self.graph.has_edge(i, j):
                        w_ij = 1.0 * self.graph[i][j]['w']
                    r_ijc = 1.0 * bf(self.graph, i, j, c, c, community_list, bc)
                    # s_from = 1.0 * sum([bf(self.graph, i, k, c, c, community_list, bc) for k in V])
                    # s_to = 1.0 * sum([bf(self.graph, k, j, c, c, community_list, bc) for k in V])
                    s_ijc = 1.0 * bf_out[i] * bf_in[j] / len(V) / len(V)
                    Q += r_ijc * w_ij - s_ijc * wdegree[i] * wdegree[j] / 2.0 / m

            # print "time = %.1f sec, size = %d" % (time.time() - start, len(c))

        Q /= 2.0 * m
        return Q

    def edge_based_weighted_directed_overlapping(self, community_list, bf, bc):
        """ Edge-based formula was designed for unweighted only. I extended it like: A_ik -> w_ik.
        """
        # Total weight
        m = 1.0 * sum([x[2] for x in list(self.graph.edges_iter(data='w'))])
        V = self.graph.nodes()
        Q = 0.0
        # Iterating over communities
        for c in community_list:
            # List of weighted degrees for each node in community (for faster computation)
            wdegree_in = {}
            wdegree_out = {}
            bf_in = {}
            bf_out = {}
            for node in c:
                bf_in[node] = 1.0 * sum([bf(self.graph, k, node, c, c, community_list, bc) for k in V])
                bf_out[node] = 1.0 * sum([bf(self.graph, node, k, c, c, community_list, bc) for k in V])
                wdegree_out[node] = 1.0 * sum([x[2] for x in self.graph.edges([node], data='w')])
                wdegree_in[node] = 1.0 * sum([x[2]['w'] for x in list(self.graph.in_edges_iter([node], data='w'))])

            # Iterating over nodes in community c
            for i in c:
                for j in c:
                    w_ij = 0.0
                    if self.graph.has_edge(i, j):
                        w_ij = 1.0 * self.graph[i][j]['w']
                    r_ijc = 1.0 * bf(self.graph, i, j, c, c, community_list, bc)
                    # s_from = 1.0 * sum([bf(self.graph, i, k, c, c, community_list, bc) for k in V])
                    # s_to = 1.0 * sum([bf(self.graph, k, j, c, c, community_list, bc) for k in V])
                    s_ijc = 1.0 * bf_out[i] * bf_in[j] / len(V) / len(V)
                    Q += r_ijc * w_ij - s_ijc * wdegree_out[i] * wdegree_in[j] / m
        Q /= m
        return Q

    def weighted_overlapping(self, community_list, bf, bc):
        """ Our version (2.2) for undirected.
        """
        # Total weight
        m = sum([x[2] for x in list(self.graph.edges_iter(data='w'))])
        Q = 0.0
        # Iterating over communities
        for c in community_list:
            # List of weighted degrees for each node in community (for faster computation)
            wdegree = {}
            for n in c:
                wdegree[n] = 1.0 * sum([x[2] for x in self.graph.edges([n], data='w')])

            # Iterating over ordered pairs of nodes in community c (we count them twice)
            for i, j in combinations(c, 2):
                w_ij = 0.0
                if self.graph.has_edge(i, j):
                    w_ij = 1.0 * self.graph[i][j]['w']
                f_ijc = 1.0 * bf(self.graph, i, j, c, c, community_list, bc)
                Q += 2.0 * (w_ij - wdegree[i] * wdegree[j] / 2.0 / m) * f_ijc

            # Iterating over self-loops in community c
            for i in c:
                w_ii = 0.0
                if self.graph.has_edge(i, i):
                    w_ii = 1.0 * self.graph[i][i]['w']
                f_iic = 1.0 * bf(self.graph, i, i, c, c, community_list, bc)
                Q += (w_ii - wdegree[i] * wdegree[i] / 2.0 / m) * f_iic
        Q /= 2.0 * m
        return Q

    def weighted_directed_overlapping(self, community_list, bf, bc):
        """ Our version (2.2) for directed.
        """
        # Total weight
        m = sum([x[2] for x in list(self.graph.edges_iter(data='w'))])
        Q = 0.0
        # Iterating over communities
        for c in community_list:
            # List of weighted degrees for each node in community (for faster computation)
            wdegree_in = {}
            wdegree_out = {}
            for n in c:
                wdegree_out[n] = 1.0 * sum([x[2] for x in self.graph.edges([n], data='w')])
                wdegree_in[n] = 1.0 * sum([x[2]['w'] for x in list(self.graph.in_edges_iter([n], data='w'))])

            # Iterating over pairs of nodes in community c
            for i in c:
                for j in c:
                    w_ij = 0.0
                    if self.graph.has_edge(i, j):
                        w_ij = 1.0 * self.graph[i][j]['w']
                    f_ijc = 1.0 * bf(self.graph, i, j, c, c, community_list, bc)
                    Q += (w_ij - wdegree_out[i] * wdegree_in[j] / m) * f_ijc
        Q /= m
        return Q

    def density(self, community_list, bf, bc):
        """ Density modularity for both directed and undirected graphs. This is extension to weighted graphs:
        A_ij -> w_ij
        """
        # Total weight
        m = 0.5 * sum([x[2] for x in list(self.graph.edges_iter(data='w'))])
        Q = 0.0
        # Iterating over communities
        for c in community_list:
            # print "comm %s" % str(c)
            start = time.time()

            m_in = 0.0
            m_out = 0.0
            for i in c:
                for j in c:
                    if self.graph.has_edge(i, j):
                        m_in += 0.5 * bf(self.graph, i, j, c, c, community_list, bc) * self.graph[i][j]['w']
                for z in community_list:
                    if z == c: continue
                    for j in z:
                        if self.graph.has_edge(i, j):
                            m_out += 0.5 * bf(self.graph, i, j, c, z, community_list, bc) * self.graph[i][j]['w']

            denom = 0.0
            for i in c:
                for j in c:
                    if j == i: continue
                    denom += 1.0 * bf(self.graph, i, j, c, c, community_list, bc)
            d_c = 2.0 * m_in / denom

            last_sum = 0.0
            for z in community_list:
                if z == c:
                    continue
                m_cz = 0.0
                denom_sum = 0.0
                for i in c:
                    for j in z:
                        bfij = 1.0 * bf(self.graph, i, j, c, z, community_list, bc)
                        if self.graph.has_edge(i, j):
                            m_cz += bfij * self.graph[i][j]['w']
                        denom_sum += bfij
                # d_cz = 1.0 * m_cz / denom_sum
                last_sum += 1.0 * m_cz * m_cz / denom_sum / (2.0 * m)
            # print "time = %.1f sec, size = %d" % (time.time() - start, len(c))

            Q += 1.0 * m_in * d_c / m
            Q -= 1.0 * (2.0*m_in + m_out) * d_c / (2.0 * m) * (2.0*m_in + m_out) * d_c / (2.0 * m)
            Q -= 1.0 * last_sum

        return Q

    # def link_rank(self, community_list, bf=None, bc=None, alpha=0.85, personalization=None,
    #               max_iter=100, tol=1.0e-6, weight='w', dangling=None):
    #     """ Modularity based on google page rank, introduced by Kim et al in http://arxiv.org/pdf/0902.3728v3.pdf.
    #     Originally for non-overlapping clusters, we extended it.
    #     """
    #     Q = 0.0
    #     page_rank = nx.pagerank_scipy(self.graph, alpha, personalization, max_iter, tol, weight, dangling)
    #     # page_rank = nx.pagerank_numpy(self.graph, alpha, personalization, weight, dangling)
    #
    #     N = self.graph.number_of_nodes()
    #     # Iterating over communities
    #     for c in community_list:
    #         # Out degrees for each node in the community (for faster calculations)
    #         wdegree_out = {}
    #         for n in c:
    #             wdegree_out[n] = 1.0 * sum([x[2] for x in self.graph.edges([n], data='w')])
    #
    #         for i, j in product(c, c):
    #             p_i = page_rank[i]
    #             p_j = page_rank[j]
    #             w_ij = 0.0
    #             if self.graph.has_edge(i, j):
    #                 w_ij = 1.0 * self.graph[i][j]['w']
    #             if wdegree_out[i] == 0:  # dangling node
    #                 g_ij = 1.0 / N
    #             else:
    #                 g_ij = 1.0 * alpha * w_ij / wdegree_out[i] + (1.0 - alpha) / N
    #             Q += 1.0 * p_i * (g_ij - p_j)
    #     return Q

    def link_rank_overlapping(self, community_list, bf, bc, alpha=0.85, personalization=None,
                  max_iter=100, tol=1.0e-6, weight='w', dangling=None):
        """ Modularity based on google page rank, introduced by Kim et al in http://arxiv.org/pdf/0902.3728v3.pdf.
        Our extension for overlapping
        """
        Q = 0.0
        page_rank = nx.pagerank_scipy(self.graph, alpha, personalization, max_iter, tol, weight, dangling)
        # page_rank = nx.pagerank_numpy(self.graph, alpha, personalization, weight, dangling)

        N = self.graph.number_of_nodes()
        # Iterating over communities
        for c in community_list:
            # Out degrees for each node in the community (for faster calculations)
            wdegree_out = {}
            for n in c:
                wdegree_out[n] = 1.0 * sum([x[2] for x in self.graph.edges([n], data='w')])

            for i, j in product(c, c):
                p_i = page_rank[i]
                p_j = page_rank[j]
                w_ij = 0.0
                if self.graph.has_edge(i, j):
                    w_ij = 1.0 * self.graph[i][j]['w']
                if wdegree_out[i] == 0:  # dangling node
                    g_ij = 1.0 / N
                else:
                    g_ij = 1.0 * alpha * w_ij / wdegree_out[i] + (1.0 - alpha) / N
                f_ijc = 1.0 * bf(self.graph, i, j, c, c, community_list, bc)
                Q += 1.0 * p_i * (g_ij - p_j) * f_ijc
        return Q

# --- Belonging functions.


def bf_as_sum(g, a, b, community_a, community_b, community_list, bc):
    return 0.5 * (bc(g, a, community_a, community_list) + bc(g, b, community_b, community_list))


def bf_as_product(g, a, b, community_a, community_b, community_list, bc):
    return bc(g, a, community_a, community_list) * bc(g, b, community_b, community_list)


def bf_as_intersection(g, a, b, community_a, community_b, community_list, bc=None):
    num_of_common = 0
    for c in community_list:
        if a in c and b in c:
            num_of_common += 1

    if num_of_common == 0:
        return 0
    return 1.0 / num_of_common


def bf_edge_based(g, a, b, community_a, community_b, community_list, bc):
    # Magic parameters are taken from paper http://arxiv.org/pdf/0801.1647v4.pdf.
    p = 30.0

    def f(x):
        return 2 * p * x - p

    def F(a, b):
        return 1.0 / (1 + exp(-2*p*a + p)) / (1 + exp(-2*p*b + p))

    return F(bc(g, a, community_a, community_list), bc(g, b, community_b, community_list))

# --- Belonging coefficients.


def bc_as_uniform(g, node, community, community_list):
    num_of_communities = 0
    for c in community_list:
        if node in c:
            num_of_communities += 1

    if num_of_communities == 0:
        return 0
    return 1.0 / num_of_communities


def bc_as_fraction(g, node, community, community_list):
    """ Formula was designed for undirected unweighted only. I extended it like: A_ik -> w_ik
    """
    numerator = 0.0
    denominator = 0.0

    for k in community:
        if g.has_edge(node, k):
            numerator += g[node][k]['w']

    for c in community_list:
        if node in c:
            for k in c:
                if g.has_edge(node, k):
                    denominator += g[node][k]['w']

    if denominator == 0:
        raise RuntimeError("Seems node %s belongs to 0 communities or has no output links in its communities."
                           " Don't use this belonging function with directed graphs." % str(node))
        # print "Seems node %s belongs to 0 communities or has no output links in its communities." % str(node)
        # return 0.0
    return 1.0 * numerator / denominator

bf_dict = {"sum": bf_as_sum,
           "product": bf_as_product,
           "intersection": bf_as_intersection,
           "edge": bf_edge_based}

bc_dict = {"uniform": bc_as_uniform,
           "fraction": bc_as_fraction}

if __name__ == "__main__":

    # Assuming all parameters are provided and take valid values.
    pathToGraphFile = sys.argv[1]
    isGraphDirected = sys.argv[2] == 'true'
    isGraphWeighted = sys.argv[3] == 'true'
    pathToCommunities = sys.argv[4]
    formula_type = sys.argv[5].lower()
    belonging_func = bf_dict[sys.argv[6].lower()]
    belonging_coef = bc_dict[sys.argv[7].lower()]

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
        print (-1.0)
        quit()

    community_list = []
    with open(pathToCommunities) as comm_file:
        for line in comm_file:
            community_list.append([int(x) for x in line.split()])

    # Computing modularity
    mc = ModularityCalculator(g, isGraphDirected)
    if formula_type == "ours":
        our_version_mod = mc.our_version(community_list, belonging_func, belonging_coef)
    elif formula_type == "edge-based":
        our_version_mod = mc.edge_based(community_list, belonging_func, belonging_coef)
    elif formula_type == "density":
        our_version_mod = mc.density(community_list, belonging_func, belonging_coef)
    elif formula_type == "link-rank":
        our_version_mod = mc.link_rank_overlapping(community_list, belonging_func, belonging_coef)
    else:  # Unknown formula name
        raise NameError("Unknown formula name: %s" % formula_type)
    print float(our_version_mod)

