package graphProviderLFR;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphGenerationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphProvision.IGraphGenerator;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphProvision.ProvidedGraph;
import ru.ispras.modis.NetBlox.scenario.GraphParametersSet;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

/**
 * The callback class that runs LFR generation process. An external implementation
 * of LFR-generator will be used.
 * 
 * @author ilya
 */
public class LFRGenerator extends ExternalApplicationProvider implements IGraphGenerator {
	private static final String GENERATOR_APPLICATION_NAME;
	private static final String GENERATOR_BINARY_DIRECTORY_PATHNAME;
	private static final String GENERATOR_DIRECTED_DIRECTORY_PATHNAME;
	private static final String GENERATOR_WEIGHTED_DIRECTORY_PATHNAME;
	private static final String GENERATOR_DIRECTED_WEIGHTED_DIRECTORY_PATHNAME;

	private static final String COMMUNITIES_FORMAT_CONVERTER_PATH;
	static	{
		String generatorsRoot = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");

		GENERATOR_BINARY_DIRECTORY_PATHNAME = generatorsRoot + "binary_networks" + FILES_SEPARATOR;
		GENERATOR_DIRECTED_DIRECTORY_PATHNAME = generatorsRoot + "directed_networks" + FILES_SEPARATOR;
		GENERATOR_WEIGHTED_DIRECTORY_PATHNAME = generatorsRoot + "weighted_networks" + FILES_SEPARATOR;
		GENERATOR_DIRECTED_WEIGHTED_DIRECTORY_PATHNAME = generatorsRoot + "weighted_directed_nets" + FILES_SEPARATOR;

		if (SystemUtils.IS_OS_WINDOWS)	{
			GENERATOR_APPLICATION_NAME = "benchmark.exe";
		}
		else	{
			GENERATOR_APPLICATION_NAME = "benchmark";
		}


		COMMUNITIES_FORMAT_CONVERTER_PATH = generatorsRoot + "convertLFRCommunities2CommunitiesInLines.py";
	}
	private static final String PYTHON_APPLICATION = "python";


	@Override
	public ProvidedGraph getGraph(GraphParametersSet providedParameters) throws GraphGenerationException {
		if (!(providedParameters instanceof ParametersSetLFR))	{
			throw new GraphGenerationException("Wrong parameters for generating LFR graph! "+providedParameters.toString());
		}
		ParametersSetLFR parameters = (ParametersSetLFR) providedParameters;

		String generatorDirectory = getGeneratorDirectoryPathString(parameters);

		List<String> generationCommand = generateGenerationCommand(parameters, generatorDirectory);

		try {
			runExternal(generationCommand, generatorDirectory, null);
		} catch (ExternalException e) {
			String errorMessage = "Failed to generate graph: "+parameters.toString()+"\nGenerator ended with error: "+e.getMessage();
			throw new GraphGenerationException(errorMessage);
		}

		return packGeneratedGraph(parameters, generatorDirectory);
	}


	private String getGeneratorDirectoryPathString(GraphParametersSet parameters)	{
		String generatorDirectory = GENERATOR_BINARY_DIRECTORY_PATHNAME;
		if (parameters.isDirected() && parameters.isWeighted())	{
			generatorDirectory = GENERATOR_DIRECTED_WEIGHTED_DIRECTORY_PATHNAME;
		}
		else if (parameters.isDirected())	{
			generatorDirectory = GENERATOR_DIRECTED_DIRECTORY_PATHNAME;
		}
		else if (parameters.isWeighted())	{
			generatorDirectory = GENERATOR_WEIGHTED_DIRECTORY_PATHNAME;
		}
		return generatorDirectory;
	}

	private List<String> generateGenerationCommand(ParametersSetLFR parameters, String generatorDirectory)	{
		List<String> command = new LinkedList<String>();
		command.add(generatorDirectory+GENERATOR_APPLICATION_NAME);

		command.add("-f");
		command.add(generatorDirectory+"flags.dat");

		int numberOfNodes = parameters.getNumberOfNodes();
		command.add("-N");
		command.add(String.valueOf(numberOfNodes));

		Integer om = parameters.getOm();
		if (om == null)	{	om = 0;	}
		command.add("-om");
		command.add(om.toString());

		Float onShare = parameters.getOnShare();
		if (onShare == null)	{	onShare = 0f;	}
		int on = (int)(numberOfNodes * onShare);
		command.add("-on");
		command.add(String.valueOf(on));

		Integer averageNodeDegree = parameters.getAverageNodeDegree();
		if (averageNodeDegree == null)	{
			averageNodeDegree = (int)Math.floor(15  *  (1 + (om-1)*onShare));
		}
		command.add("-k");
		command.add(averageNodeDegree.toString());

		Integer maximalNodeDegree = parameters.getMaximalNodeDegree();
		if (maximalNodeDegree == null)	{
			maximalNodeDegree = (int)Math.floor(2.5*averageNodeDegree);
		}
		command.add("-maxk");
		command.add(maximalNodeDegree.toString());

		addNotNullParameter(command, "-minc", parameters.getMinimalCommunitySize());
		addNotNullParameter(command, "-maxc", parameters.getMaximalCommunitySize());
		addNotNullParameter(command, "-t1", parameters.get_t1());
		addNotNullParameter(command, "-t2", parameters.get_t2());

		if (!parameters.isDirected())	{
			addNotNullParameter(command, "-C", parameters.getAverageClusteringCoefficient());
		}

		if (parameters.isWeighted())	{
			addNotNullParameter(command, "-mut", parameters.getMuTopology());
			addNotNullParameter(command, "-muw", parameters.getMuWeights());
			addNotNullParameter(command, "-beta", parameters.get_beta_exponentWeightDistribution());
		}
		else	{
			addNotNullParameter(command, "-mu", parameters.getMu());
		}

		return command;
	}

	private ProvidedGraph packGeneratedGraph(GraphParametersSet parameters, String generatorDirectory) throws GraphGenerationException	{
		String generatedGraphPathString = generatorDirectory + "network.dat";

		String generatedCommunitiesPathString = generatorDirectory + "community.dat";
		String tempCoverPathString = getTempFolderPathString() + FILES_SEPARATOR + parameters.getReferenceCoverFilename();
		convertCommunitiesFilesToNetBloxFormat(generatedCommunitiesPathString, tempCoverPathString, generatorDirectory);

		return new ProvidedLFR(generatedGraphPathString, tempCoverPathString);
	}

	public static void convertCommunitiesFilesToNetBloxFormat(String sourcePath, String resultPath, String applicationDirectory)
			throws GraphGenerationException	{
		List<String> command = new LinkedList<String>();
		command.add(PYTHON_APPLICATION);
		command.add(COMMUNITIES_FORMAT_CONVERTER_PATH);
		command.add(sourcePath);
		command.add(resultPath);

		try {
			runExternal(command, applicationDirectory, null);
		} catch (ExternalException e) {
			String errorMessage = "Couldn't bring generated communities to NetBlox format: "+e.getMessage();
			throw new GraphGenerationException(errorMessage);
		}
	}
}
