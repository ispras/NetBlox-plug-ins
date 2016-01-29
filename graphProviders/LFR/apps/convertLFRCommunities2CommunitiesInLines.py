# Converts the representation of LFR generated communities (from binary_networks etc.)
# to the same form as SLPA gives the results. In the latter case a line of the file
# gives you the nodes for one community.

import sys

if __name__ == "__main__":

    path = sys.argv[1]
    path_o = sys.argv[2]

    comms = {}

    for line in file(path):
        id  = int(line.split('\t')[0])
        com_ids = line.split('\t')[1].strip().split(" ")

        for c_id in com_ids:
            if not comms.has_key(int(c_id)):
                comms[int(c_id)] = set()
            comms[int(c_id)].add(id)

    out = open(path_o, "w")

    for c in comms.values():
        out.write(" ".join(map(str, c)) + "\n")
