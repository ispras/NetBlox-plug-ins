# NetBlox plug-ins readme

This repository contains the sources for a number of plug-ins for [NetBlox](https://github.com/ispras/NetBlox) system. See the information about NetBlox as a whole on its main page and in its wiki. Here you will find the information only about provided plug-ins.

The repository stores the source code for plug-ins (mainly Java, but not only) along with their configuration and resources files.  Some of the plug-ins use external implementations of the algorithms they provide (binaries, libraries). Those implementations are not stored in the repository in most cases, but can be downloaded from the Internet as they are freely provided by their authors. More information will be given in the descriptions of specific plug-ins.

## Using plug-ins

To use a plug-in you need to have the [NetBlox](https://github.com/ispras/NetBlox) system ready (see the link). As for the plug-in itself, you need to download its code from here and compile it on your machine (a few words about the structure of repository will be said later). The plug-ins system of NetBlox is based on Eclipse RCP, so we recommend you to build the plug-ins from sources in Eclipse IDE along with NetBlox itself which will help you to use the libraries and packages exported by NetBlox, as well as in other aspects (we plan to add an IDE-independent building script later). If a plug-in uses external binaries or libraries, you will need to download them from sites provided by their authors (the information will be given in the descriptions of specific plug-ins). After you have compiled the plug-ins you want, you need to register them in NetBlox; you can do it either in IDE (if you're going to launch NetBlox from IDE) or manually (if you choose to export NetBlox and use it as a standalone application). For the instructions concerning the launch see NetBlox User's guide (in its wiki).

See more information about plug-ins in the [provided plug-ins user's guide](https://github.com/ispras/NetBlox-plug-ins/wiki/User's-guide-for-provided-plug-ins).

## Structure of repository

The files in this repository are organised around the plug-ins which are grouped in three major categories (that correspond to three different extension points of the system):
* graphProviders folder contains two plug-ins that provide the graphs that are used in further computations:
 1. LFR — it is an adaptor for LFR bechmark graphs generation. The binaries that are used by plug-in can be downloaded from the author's [page](https://sites.google.com/site/andrealancichinetti/files). The description of the algorithm can be found in his [paper](https://sites.google.com/site/andrealancichinetti/benchmark2.pdf?attredirects=0).
 2. external — this plug-in allows a user to upload an externally provided graph to the system for computations.
* graphMiners folder contains several graph mining plug-ins:
 1. Dijkstra — here resides the source code for a plug-in that implements the Dijkstra's shortest path algorithm. See the [user's guide for plug-ins](https://github.com/ispras/NetBlox-plug-ins/wiki/User's-guide-for-provided-plug-ins) for more information.
 2. GCE — this plug-in is an adapter for the author's implementation of Greedy Clique Expansion community detection algorithm. The implementation itself should be downloaded from [here](https://sites.google.com/site/greedycliqueexpansion/).
 3. MOSES — an adapter for another community detection algorithm. The required binaries are available from [here](https://sites.google.com/site/aaronmcdaid/moses). Please note that they don't run under Windows OS (the provided binaries for other plug-ins either run under Windows from the box, or their source code can be relatively easily upgraded for that).
 4. OSLOM — an adapter for one of the best quality community detection algorithms (as for now). The source code for the author's implementation of the algorithm (that is required by this plug-in) can be downloaded from the special [site](http://www.oslom.org/software.htm).
 5. SLPA_GANXiS — here you will find an adapter for SLPA community detection algorithm, which is based on the Label Propagation idea. Its current implementation by its authors is called GANXiS and should be downloaded from [here](https://sites.google.com/site/communitydetectionslpa/ ).
* numericCharacteristics — this directory keeps one plug-in for community detection quality measurement and several plug-ins that compute characteristic statistics for graphs and sets of groups of their nodes (communities, clusters, etc.).
 1. qualityMeasures/NMI — this path will bring you to the mentioned GCD quality measure. This plug-in serves as an adaptor for the authors' implementation of this measure. The sources for the actual implementation (that will be called by plug-in) and the description of the algorithm can be found on Lancichinetti's [page](https://sites.google.com/site/andrealancichinetti/mutual).
 2. pipeMinedCharacteristic — this plug-in doesn't actually compute anything. Instead it pipes the results of work of graph mining plug-ins that have output some numeric characteristics themselves to the later, plotting, stage (see the [provided plug-ins user's guide](https://github.com/ispras/NetBlox-plug-ins/wiki/User's-guide-for-provided-plug-ins) for information about those).
 3. communitiesStatistics — the plug-in in here computes a number of characteristic statistics for sets of groups of nodes. See the full list in the plug-ins user's guide in wiki.
 4. distributionsOnGroupsOfNodes — computes two distributions: Communities Sizes distribution and Nodes Memberships distribution. More details in user's guide.
 5. graphStatistics — computes several characteristic statistics for graphs (both original and discovered at mining stage). Computations are run using the [snap](http://snap.stanford.edu/snappy/index.html) python library (which must be installed on the user's machine). More details in user's guide.

## License

The NetBlox plug-ins are released under Apache 2.0 license, as the system itself. The file with license text is added to the repository.

## Questions

If you have some questions considering NetBlox or the provided plug-ins, please feel free to ask us!
