package minerSLPA_GANXiS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
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

	private static final String GANXIS_RESULT_FILE_NAME_PATTERN_STRING = "SLPAw_\\w+_r(0\\.\\d+)_v3_\\w+\\.icpm";
	private static final int THRESHOLD_GROUP = 1;
	private static final Pattern GANXIS_RESULT_FILE_NAME_PATTERN = Pattern.compile(GANXIS_RESULT_FILE_NAME_PATTERN_STRING);


	public static MinerResults mine(GraphOnDrive graphOnDrive, ParametersSetSLPA parameters) throws GraphMiningException	{
		List<String> command = generateCommand(graphOnDrive, parameters);

		try {
			Path outputPath = Paths.get(OUTPUT_DIRECTORY_PATHSTRING);
			if (!Files.exists(outputPath))	{
				Files.createDirectories(outputPath);
			}

			runExternal(command, MINERS_ROOT, null);
		} catch (ExternalException | IOException e) {
			throw new GraphMiningException(e);
		}

		List<MinerResults> listOfMinedResults = extractMinedCommunities(parameters);

		return new SLPAResults(parameters, listOfMinedResults);
	}


	private static List<String> generateCommand(GraphOnDrive graphOnDrive, ParametersSetSLPA parameters)	{
		List<String> command = new LinkedList<String>();
		command.add(APPLICATION_RUNNER);
		command.add(XMS);
		command.add(XMX);
		command.add("-jar");
		command.add(APPLICATION);
		command.add("-i");
		command.add(graphOnDrive.getGraphFilePathString());

		addNotNullParameter(command, "-t", parameters.getIterationsNumber());
		addNotNullParameter(command, "-minC", parameters.getMinCommunitySize());
		addNotNullParameter(command, "-maxC", parameters.getMaxCommunitySize());

		if (!parameters.isDefaultThreshold())	{
			addNotNullParameter(command, "-r", parameters.getThreshold());
		}

		if (!graphOnDrive.isDirected())	{
			command.add("-Sym");
			command.add("1");
		}
		if (!graphOnDrive.isWeighted())	{
			command.add("-W");
			command.add("0");
		}

		command.add("-d");
		command.add(OUTPUT_DIRECTORY_PATHSTRING);

		return command;
	}


	private static List<MinerResults> extractMinedCommunities(ParametersSetSLPA algorithmLaunchParameters) throws GraphMiningException	{
		List<MinerResults> listOfMinedResults = new LinkedList<MinerResults>();

		File outputDirectory = new File(OUTPUT_DIRECTORY_PATHSTRING);
		for (String resultFileName : outputDirectory.list())	{
			Float calculatedThreshold = getThresholdGANXiS(resultFileName);
			if (calculatedThreshold == null)	{
				continue;
			}

			ParametersSetSLPA singleResultParameters = algorithmLaunchParameters.clone();
			singleResultParameters.setThreshold(calculatedThreshold);

			MinerResults singleResult = new SLPAResults(singleResultParameters, OUTPUT_DIRECTORY_PATHSTRING+resultFileName);
			listOfMinedResults.add(singleResult);
		}

		if (listOfMinedResults.isEmpty())	{
			throw new GraphMiningException(PLUGIN_ID+" got no results.");
		}

		return listOfMinedResults;
	}

	private static Float getThresholdGANXiS(String resultFileName)	{
		Matcher matcher = GANXIS_RESULT_FILE_NAME_PATTERN.matcher(resultFileName);
		if (!matcher.matches())	{
			return null;
		}

		String thresholdString = matcher.group(THRESHOLD_GROUP);
		return Float.parseFloat(thresholdString);
	}
}
