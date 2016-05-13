package minerSLPA_GANXiS;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

public class GANXiS_Miner extends ExternalApplicationProvider {
	public static final String PLUGIN_ID = Activator.getContext().getBundle().getSymbolicName();

	private static final String APPLICATION_RUNNER = "java";
	private static final String APPLICATION;
	private static final String MINERS_ROOT;
	public static final String OUTPUT_DIRECTORY_PATHSTRING;
	static	{
		MINERS_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");

		APPLICATION = MINERS_ROOT + "GANXiSw.jar";

		OUTPUT_DIRECTORY_PATHSTRING = getTempFolderPathString() + FILES_SEPARATOR + PLUGIN_ID + FILES_SEPARATOR;
	}

	//private static final String GANXIS_RESULT_FILE_NAME_PATTERN_STRING = "SLPAw_\\w+_r(0\\.\\d+)_v3_\\w+\\.icpm";	//Leave for future use perhaps.
	
	/**
	 * Runs SLPA algorithm on the given graph with the specified parameters.
	 * 
	 * @param graphOnDrive
	 * @param parameters
	 * @return path to SLPA's output file
	 * @throws GraphMiningException
	 */
	public static String mine(GraphOnDrive graphOnDrive, ParametersSetSLPA parameters) throws GraphMiningException	{
		// Temporary dir with unique name to make sure that SLPA really outputs something.
		String outputDir = OUTPUT_DIRECTORY_PATHSTRING + String.valueOf(System.nanoTime()) + FILES_SEPARATOR;
		
		List<String> command = generateCommand(graphOnDrive, parameters, outputDir);

		try {
			Path outputPath = Paths.get(outputDir);
			if (!Files.exists(outputPath))	{
				Files.createDirectories(outputPath);
			}

			runExternal(command, MINERS_ROOT, null);
		} catch (ExternalException | IOException e) {
			throw new GraphMiningException(e);
		}
		
		String[] maybeOutputFile = new File(outputDir).list(new FilenameFilter() {			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".icpm");
			}
		});		
		if (maybeOutputFile.length > 0) {
			return outputDir + maybeOutputFile[0];
		}
		throw new GraphMiningException(PLUGIN_ID+" got no results.");
	}


	private static List<String> generateCommand(GraphOnDrive graphOnDrive, ParametersSetSLPA parameters, String outputDir)	{
		List<String> command = new LinkedList<String>();
		command.add(APPLICATION_RUNNER);
		command.add(XMS);
		command.add(XMX);
		command.add("-jar");
		command.add(APPLICATION);
		command.add("-i");
		command.add(graphOnDrive.getGraphFilePathString());
		command.add("-r");
		command.add(parameters.getThreshold().toString());

		addNotNullParameter(command, "-t", parameters.getIterationsNumber());
		addNotNullParameter(command, "-minC", parameters.getMinCommunitySize());
		addNotNullParameter(command, "-maxC", parameters.getMaxCommunitySize());

		if (!graphOnDrive.isDirected())	{
			command.add("-Sym");
			command.add("1");
		}
		if (!graphOnDrive.isWeighted())	{
			command.add("-W");
			command.add("0");
		}

		command.add("-d");
		command.add(outputDir);

		return command;
	}	
}
