package minerOSLOM;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.SystemUtils;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

public class OSLOMMiner extends ExternalApplicationProvider {
	private static final String OSLOM_DIRECTED;
	private static final String OSLOM_UNDIRECTED;
	private static final String MINERS_ROOT;
	static	{
		MINERS_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");

		if (SystemUtils.IS_OS_WINDOWS)	{
			OSLOM_DIRECTED = MINERS_ROOT + "oslom_dir.exe";
			OSLOM_UNDIRECTED = MINERS_ROOT + "oslom_undir.exe";
		}
		else	{
			OSLOM_DIRECTED = MINERS_ROOT + "oslom_dir";
			OSLOM_UNDIRECTED = MINERS_ROOT + "oslom_undir";
		}
	}

	private static final String OSLOM_MODULE_LINE_PATTERN_STRING = "#module.+";
	private static final Pattern OSLOM_MODULE_LINE_PATTERN = Pattern.compile(OSLOM_MODULE_LINE_PATTERN_STRING);


	public static List<String> mine(GraphOnDrive graphOnDrive, OSLOM_ParametersSet miningParameters) throws GraphMiningException	{
		List<String> command = generateCommand(graphOnDrive, miningParameters);

		try {
			runExternal(command, MINERS_ROOT, null);
		} catch (ExternalException e) {
			throw new GraphMiningException(e);
		}

		List<String> minedCommunitiesInLines = extractResults(graphOnDrive);
		return minedCommunitiesInLines;
	}


	private static List<String> generateCommand(GraphOnDrive graphOnDrive, OSLOM_ParametersSet parameters)	{
		List<String> command = new LinkedList<String>();
		if (graphOnDrive.isDirected())	{
			command.add(OSLOM_DIRECTED);
		}
		else	{
			command.add(OSLOM_UNDIRECTED);
		}

		command.add("-f");
		command.add(graphOnDrive.getGraphFilePathString());

		if (graphOnDrive.isWeighted())	{
			command.add("-w");
		}
		else	{
			command.add("-uw");
		}

		command.add("-hr");
		command.add(String.valueOf(parameters.getNumberOfRunsForHigherHierarchicalLevel()));

		return command;
	}


	private static List<String> extractResults(GraphOnDrive graphOnDrive) throws GraphMiningException	{
		String oslomResultFolderPathString = graphOnDrive.getGraphFilePathString() + "_oslo_files";

		try {
			List<String> oslomResultLines = Files.readAllLines(Paths.get(oslomResultFolderPathString+FILES_SEPARATOR+"tp"), Charset.defaultCharset());
			List<String> minedCommunitiesInLines = new LinkedList<String>();

			for (String line : oslomResultLines)	{
				line.trim();
				Matcher matcher = OSLOM_MODULE_LINE_PATTERN.matcher(line);
				if (matcher.matches())	{
					continue;
				}

				minedCommunitiesInLines.add(line);
			}

			deleteTemporaryFiles(oslomResultFolderPathString);

			return minedCommunitiesInLines;
		} catch (IOException e) {
			String message = "Could not access OSLOM mining results: "+e.getMessage();
			throw new GraphMiningException(message);
		}
	}

	private static void deleteTemporaryFiles(String oslomResultFolderPathString) throws IOException	{
		deleteRecursively(oslomResultFolderPathString);
		Files.deleteIfExists(Paths.get(MINERS_ROOT+"time_seed.dat"));
		Files.deleteIfExists(Paths.get(MINERS_ROOT+"tp"));
	}
}
