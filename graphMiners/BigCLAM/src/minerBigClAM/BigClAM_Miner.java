package minerBigClAM;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;

import ru.ispras.modis.NetBlox.dataStructures.Graph;
import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;
import ru.ispras.modis.NetBlox.utils.Pair;

public class BigClAM_Miner extends ExternalApplicationProvider {
	public static final String PLUGIN_ID = Activator.getContext().getBundle().getSymbolicName();

	private static final String MINER_DIRECTORY;
	private static final String MINER_APP;
	private static final String TEMP_OUTPUT_FOLDER;
	public static final String TEMP_OUTPUT_COMMUNITIES_PATH;
	static	{
		MINER_DIRECTORY = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps") + FILES_SEPARATOR;
		if (SystemUtils.IS_OS_WINDOWS)	{
			MINER_APP = MINER_DIRECTORY + "bigclam.exe";
		}
		else	{
			MINER_APP = MINER_DIRECTORY + "bigclam";
		}

		TEMP_OUTPUT_FOLDER = getTempFolderPathString() + FILES_SEPARATOR + "bigclam" + FILES_SEPARATOR;
		TEMP_OUTPUT_COMMUNITIES_PATH = TEMP_OUTPUT_FOLDER + "cmtyvv.txt";
	}


	public static void mine(GraphOnDrive graphOnDrive, ParametersSetBigClAM parameters) throws GraphMiningException	{
		if (SystemUtils.IS_OS_WINDOWS)	{	//XXX Make this support.
			String notThisOS = "The current version of BigClAM plug-in doesn't support Windows OS.";
			throw new GraphMiningException(notThisOS);
		}
		
		List<String> command = generateCommand(graphOnDrive, parameters);
		try {
			Path outputPath = Paths.get(TEMP_OUTPUT_FOLDER);
			if (!Files.exists(outputPath))	{
				Files.createDirectories(outputPath);
			}

			runExternal(command, MINER_DIRECTORY, null);
		} catch (ExternalException | IOException e) {
			throw new GraphMiningException(e);
		}
	}


	private static List<String> generateCommand(GraphOnDrive graphOnDrive, ParametersSetBigClAM parameters)	{
		List<String> command = new LinkedList<String>();
		command.add(MINER_APP);

		command.add("-i:"+graphOnDrive.getGraphFilePathString());
		command.add("-o:"+TEMP_OUTPUT_FOLDER);

		List<Pair<String, String>> specifiedParametersList = parameters.getSpecifiedParametersAsPairsOfUniqueKeysAndValues();
		
		if (specifiedParametersList != null) {
			for (Pair<String, String> keyValue : specifiedParametersList) {
				String key = keyValue.getKey();
				String value = keyValue.getValue();
				if (keyValue.getKey().equals("xc") && Integer.valueOf(value) == -1) {
					// Setting max number of communities to try parameter equal to number of nodes.
					IGraph g = new Graph(graphOnDrive.getGraphFilePathString(), graphOnDrive.isDirected(), graphOnDrive.isWeighted());
					value = String.valueOf(g.size());
				}
				command.add("-" + key + ":" + value);
			}
		}

		//TODO How about regulating parallelism?
		command.add("-nt:4");	//Now it's just number of threads = 4.

		return command;
	}
}
