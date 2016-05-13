package minerLinkComm;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.SystemUtils;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

public class LinkCommunitiesMiner extends ExternalApplicationProvider {
	public static final String PLUGIN_ID = Activator.getContext().getBundle().getSymbolicName();

	private static final String MINER_DIRECTORY;
	private static final String APP_CALCULATE_JACCARDS;
	private static final String APP_CLUSTERS;

	private static final String TEMP_FOLDER;
	private static final String TEMP_JACCARDS_PATHSTRING;
	private static final String TEMP_LINK_CLUSTERS_PATHSTRING;
	private static final String TEMP_CLUSTERS_STATS;
	static	{
		MINER_DIRECTORY = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps") + FILES_SEPARATOR;
		if (SystemUtils.IS_OS_WINDOWS)	{
			APP_CALCULATE_JACCARDS = MINER_DIRECTORY + "calcJaccards.exe";
			APP_CLUSTERS = MINER_DIRECTORY + "clusterJaccards.exe";
		}
		else	{
			APP_CALCULATE_JACCARDS = MINER_DIRECTORY + "calcJaccards";
			APP_CLUSTERS = MINER_DIRECTORY + "clusterJaccards";
		}

		TEMP_FOLDER = getTempFolderPathString() + FILES_SEPARATOR + "linkcomm" + FILES_SEPARATOR;
		TEMP_JACCARDS_PATHSTRING = TEMP_FOLDER + "network.jaccs";
		TEMP_LINK_CLUSTERS_PATHSTRING = TEMP_FOLDER + "clusters.dat";
		TEMP_CLUSTERS_STATS = TEMP_FOLDER + "clusters.stats";
	}

	private static final String WHITESPACE_CHARACTER_REGEX = "\\s";


	public static LinkCommResults mine(GraphOnDrive graphOnDrive, ParametersSet_LinkComm parameters) throws GraphMiningException	{
		if (SystemUtils.IS_OS_WINDOWS)	{	//XXX Make this support.
			String notThisOS = "The current version of Link Communities detection plug-in doesn't support Windows OS.";
			throw new GraphMiningException(notThisOS);
		}

		try {
			Path outputPath = Paths.get(TEMP_FOLDER);
			if (!Files.exists(outputPath))	{
				Files.createDirectories(outputPath);
			}

			List<String> command = generateJaccardsCalculationCommand(graphOnDrive);
			runExternal(command, MINER_DIRECTORY, null);

			command = generateClustersComputationCommand(graphOnDrive, parameters);
			runExternal(command, MINER_DIRECTORY, null);

			List<String> nodeClustersLines = extractNodeClusters();
			LinkCommResults result = new LinkCommResults(nodeClustersLines, parameters);

			return result;
		} catch (ExternalException | IOException e) {
			throw new GraphMiningException(e);
		}
	}


	private static List<String> generateJaccardsCalculationCommand(GraphOnDrive graphOnDrive)	{
		List<String> command = new LinkedList<String>();
		command.add(APP_CALCULATE_JACCARDS);

		command.add(graphOnDrive.getGraphFilePathString());
		command.add(TEMP_JACCARDS_PATHSTRING);

		return command;
	}

	private static List<String> generateClustersComputationCommand(GraphOnDrive graphOnDrive, ParametersSet_LinkComm parameters)	{
		List<String> command = new LinkedList<String>();
		command.add(APP_CLUSTERS);

		command.add(graphOnDrive.getGraphFilePathString());
		command.add(TEMP_JACCARDS_PATHSTRING);

		command.add(TEMP_LINK_CLUSTERS_PATHSTRING);

		command.add(TEMP_CLUSTERS_STATS);

		command.add(parameters.getThreshold().toString());

		return command;
	}


	private static List<String> extractNodeClusters() throws GraphMiningException	{
		try {
			List<String> linkClustersLines = Files.readAllLines(Paths.get(TEMP_LINK_CLUSTERS_PATHSTRING), Charset.defaultCharset());
			List<String> nodeClustersLines = new LinkedList<String>();

			for (String line : linkClustersLines)	{
				String[] edgeLines = line.split(WHITESPACE_CHARACTER_REGEX);
				if (edgeLines.length <= 1)	{
					continue;
				}

				String communityOfNodesLine = transformIntoCommunityOfNodesLine(edgeLines);
				nodeClustersLines.add(communityOfNodesLine);
			}

			return nodeClustersLines;
		} catch (IOException e) {
			String message = "Could not access Link Communities detection results: "+e.getMessage();
			throw new GraphMiningException(message);
		}
	}

	private static String transformIntoCommunityOfNodesLine(String[] edgeLines)	{
		Set<String> uniqueCommunityNodesIDs = new HashSet<String>();
		for (String edgeAsLine : edgeLines)	{
			String[] nodesIDs = edgeAsLine.split(",");
			for (String id : nodesIDs)	{
				uniqueCommunityNodesIDs.add(id);
			}
		}

		Iterator<String> uniqueIDsIterator = uniqueCommunityNodesIDs.iterator();
		StringBuilder communityNodesStringBuilder = new StringBuilder(uniqueIDsIterator.next());
		while (uniqueIDsIterator.hasNext())	{
			communityNodesStringBuilder.append(' ').append(uniqueIDsIterator.next());
		}
		return communityNodesStringBuilder.toString();
	}
}
