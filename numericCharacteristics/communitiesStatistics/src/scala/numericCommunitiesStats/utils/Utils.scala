package numericCommunitiesStats.utils

import scala.collection.mutable
import scala.io.Source
import gnu.trove.map.hash.TIntObjectHashMap
import scala.collection.mutable.ArrayBuffer
import ru.ispras.modis.NetBlox.dataStructures.Graph


/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 25.02.14
 * Time: 20:31
 */
object Utils {
    def toNodeMap(edgeList: Array[(Int, Int)]): TIntObjectHashMap[Array[Int]] = {
        println("get edge list")
        val start = 0.001 * System.currentTimeMillis()
        val graph =  mutable.Map[Int, ArrayBuffer[Int]]().withDefaultValue(ArrayBuffer[Int]())

        edgeList.foreach{
            case(a, b) => graph(a) = b +: graph(a)
                graph(b) = a +: graph(b)
        }
        val result =  new TIntObjectHashMap[Array[Int]]()
        graph.foreach{ case(key, value) => result.put(key, graph(key).toArray.distinct)}
        println("time to read graph " + (0.001 * System.currentTimeMillis() - start))
        result
    }


    def toUserRepresentation(graph: TIntObjectHashMap[Array[Int]], communities: Array[Set[Int]]) = {

        val communitiesUser = new TIntObjectHashMap[ArrayBuffer[Int]]()
        graph.keys.foreach(communitiesUser.put(_, ArrayBuffer[Int]()))

        communities.zipWithIndex.foreach{
            case(community, number) => community.foreach(user => communitiesUser.put(user, number +: communitiesUser.get(user)))
        }

        val result = new TIntObjectHashMap[User]()
        graph.keys().foreach(id => result.put(id, new User(id, communitiesUser.get(id).toArray, graph.get(id).toSet ) ))
        result
    }

    def readEdgeList(path: String, separator: String = "\t") = {
        Source.fromFile(path).getLines().map{
            line => val edge = line.replace(" ", separator).split(separator).map(_.toDouble)
                (edge(0).toInt, edge(1).toInt)
        }.toArray.distinct
    }

    def binomialCoefficient(n: Int, k: Int) = {
        require(n >= 0 && k >= 0 && n >= k)
        if (n > k) {
            0.until(k).foldLeft(1d)((res, i) => res * (n - i) / (i + 1)).toInt
        }
        else 1
    }

    //def readCommunities(path: String, graph: TIntObjectHashMap[Array[Int]], separator: String = "\t") = {\

    def readCommunities(path: String, graph: Graph, separator: String = "\t") = {
        
        //List nodes = toList
        Source.fromFile(path).getLines().map{
            line => line.replace(" ", separator).split(separator).map(_.toInt).filter(x => graph.getNode(x) != null)
            .toSet
        }.filter(_.nonEmpty).toArray
    }
}

