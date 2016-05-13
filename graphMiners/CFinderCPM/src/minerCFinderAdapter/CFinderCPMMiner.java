package minerCFinderAdapter;

import java.io.File;
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
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.MinerResults;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

public class CFinderCPMMiner extends ExternalApplicationProvider {
	public static final String PLUGIN_ID = Activator.getContext().getBundle().getSymbolicName();

	private static final boolean IS_64_BIT = System.getProperty("os.arch").equals("amd64");

	private static final String MINER_DIRECTORY;
	private static final String MINER_APP;
	private static final String TEMP_OUTPUT_DIRECTORY;
	static	{
		MINER_DIRECTORY = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps") + FILES_SEPARATOR;
		if (SystemUtils.IS_OS_MAC)	{
			MINER_APP = MINER_DIRECTORY + "CFinder_commandline_mac";
		}
		else if (SystemUtils.IS_OS_WINDOWS)	{
			if (IS_64_BIT)	{
				MINER_APP = MINER_DIRECTORY + "CFinder_commandline64.exe";
			}
			else	{
				MINER_APP = MINER_DIRECTORY + "CFinder_commandline.exe";
			}
		}
		else	{
			if (IS_64_BIT)	{
				MINER_APP = MINER_DIRECTORY + "CFinder_commandline64";
			}
			else	{
				MINER_APP = MINER_DIRECTORY + "CFinder_commandline";
			}
		}
		//System.out.println("sun.arch.data.model: "+System.getProperty("sun.arch.data.model"));
		//System.out.println("os.arch: "+System.getProperty("os.arch"));

		TEMP_OUTPUT_DIRECTORY = getTempFolderPathString() + FILES_SEPARATOR + "cfinder" + FILES_SEPARATOR;
	}

	private static final String SUBFOLDER_FOR_ONE_CLIQUES_SIZE_NAME_REGEX = "k=(\\d+)";
	private static final int SIZE_GROUP = 1;
	private static final Pattern SUBFOLDER_FOR_ONE_CLIQUES_SIZE_NAME_PATTERN = Pattern.compile(SUBFOLDER_FOR_ONE_CLIQUES_SIZE_NAME_REGEX);
	private static final String COMMUNITIES_FILE_NAME = "communities";
	private static final String DIRECTED_COMMUNITIES_FILE_NAME = "directed_communities";

	private static final String WHITESPACE_CHARACTER_REGEX = "\\s";
	private static final Pattern COMMUNITY_NUMBER_PATTERN = Pattern.compile("\\d+:");
	private static final String COMMUNITY_ID_REGEX = "[\\w\\p{Punct}]+";
	private static final Pattern COMMUNITY_ID_PATTERN = Pattern.compile(COMMUNITY_ID_REGEX);


	public static List<MinerResults> mine(GraphOnDrive graphOnDrive, ParametersSetCFinder parameters) throws GraphMiningException	{
		List<String> command = generateCommand(graphOnDrive, parameters);
		try {
			removeTemporaryOutput(TEMP_OUTPUT_DIRECTORY);

			runExternal(command, MINER_DIRECTORY, null);

			List<MinerResults> minedCommunitiesInLines = extractMinedCommunities(parameters, graphOnDrive.isDirected());

			removeTemporaryOutput(TEMP_OUTPUT_DIRECTORY);

			return minedCommunitiesInLines;
		} catch (ExternalException e) {
			throw new GraphMiningException(e);
		}
	}


	private static List<String> generateCommand(GraphOnDrive graphOnDrive, ParametersSetCFinder parameters) throws GraphMiningException	{
		List<String> command = new LinkedList<String>();
		command.add(MINER_APP);

		command.add("-i");
		command.add(graphOnDrive.getGraphFilePathString());
		command.add("-o");
		command.add(TEMP_OUTPUT_DIRECTORY);

		appendInformationAboutWeights(command, parameters, graphOnDrive.isWeighted());
		appendInfoAbout_Directedness_WeightIntensity_CliquesSearchTime(command, parameters, graphOnDrive.isDirected(), graphOnDrive.isWeighted());

		Integer cliquesSize = parameters.getCliqueSize();
		if (cliquesSize != null)	{
			command.add("-k");
			command.add(cliquesSize.toString());
		}

		return command;
	}

	private static void appendInformationAboutWeights(List<String> command, ParametersSetCFinder parameters, boolean isGraphWeighted)
			throws GraphMiningException	{
		Float minWeightThreshold = parameters.getMinWeightThreshold();
		Float maxWeightThreshold = parameters.getMaxWeightThreshold();

		if (!isGraphWeighted)	{
			if (minWeightThreshold != null  ||  maxWeightThreshold != null)	{
				System.out.println("WARNING: The graph is unweighted. Weight thresholds are ignored.");
			}
			return;
		}

		if (minWeightThreshold != null  &&  maxWeightThreshold != null  &&  maxWeightThreshold < minWeightThreshold)	{
			//This combination of parameters is faulty.
			throw new GraphMiningException("Minimal weight threshold must be less or equal then Maximal.");
		}
		if (minWeightThreshold != null)	{
			command.add("-w");
			command.add(minWeightThreshold.toString());
		}
		if (maxWeightThreshold != null)	{
			command.add("-W");
			command.add(maxWeightThreshold.toString());
		}
	}

	private static void appendInfoAbout_Directedness_WeightIntensity_CliquesSearchTime(List<String> command, ParametersSetCFinder parameters,
			boolean isGraphDirected, boolean isGraphWeighted)	{
		Float weightIntensityThreshold = parameters.getWeightIntensityThreshold();
		Float maxTimeOfCliquesSearch = parameters.getMaxSearchTimeForCliques();
		if (weightIntensityThreshold != null  &&  isGraphWeighted)	{
			if (maxTimeOfCliquesSearch != null)	{
				System.out.println("WARNING: Weight intensity threshold is set, so the time limit for cliques search is ignored.");
			}
			if (isGraphDirected)	{
				System.out.println("WARNING: Weight intensity threshold is set, so the directions of graph edges are ignored.");
			}

			command.add("-I");
			command.add(weightIntensityThreshold.toString());
			command.add("-U");
		}
		else	{
			if (weightIntensityThreshold != null)	{
				System.out.println("WARNING: The graph is unweighted. Weight intensity threshold is ignored.");
			}

			if (maxTimeOfCliquesSearch != null)	{
				command.add("-t");
				command.add(maxTimeOfCliquesSearch.toString());
			}

			command.add(isGraphDirected?"-D":"-U");
		}
	}


	private static List<MinerResults> extractMinedCommunities(ParametersSetCFinder launchParameters, boolean directed) throws GraphMiningException	{
		List<MinerResults> listOfMinedResults = new LinkedList<MinerResults>();

		File outputDirectory = new File(TEMP_OUTPUT_DIRECTORY);
		for (String candidateName : outputDirectory.list())	{
			String candidatePathname = TEMP_OUTPUT_DIRECTORY + candidateName;
			File folderCandidate = new File(candidatePathname);
			if (!folderCandidate.isDirectory())	{
				continue;
			}

			Integer cliquesSize = getFolderCliquesSize(candidateName);
			if (cliquesSize == null)	{
				continue;
			}
			ParametersSetCFinder singleResultParameters = launchParameters.clone();
			singleResultParameters.setCliquesSize(cliquesSize);

			String resultFilePathname = candidatePathname  +  FILES_SEPARATOR  +
					(directed ? DIRECTED_COMMUNITIES_FILE_NAME : COMMUNITIES_FILE_NAME);
			CFinderResults singleFileResult = extractResultsFromFile(resultFilePathname, singleResultParameters);
			if (!singleFileResult.isEmpty())	{
				listOfMinedResults.add(singleFileResult);
			}
		}

		return listOfMinedResults;
	}

	private static Integer getFolderCliquesSize(String folderName)	{
		Matcher matcher = SUBFOLDER_FOR_ONE_CLIQUES_SIZE_NAME_PATTERN.matcher(folderName);
		if (!matcher.matches())	{
			return null;
		}

		String sizeString = matcher.group(SIZE_GROUP);
		return Integer.parseInt(sizeString);
	}

	private static CFinderResults extractResultsFromFile(String resultFilePathname, ParametersSetCFinder singleResultParameters)	{
		CFinderResults result = new CFinderResults(singleResultParameters);

		try {
			List<String> lines = Files.readAllLines(Paths.get(resultFilePathname), Charset.defaultCharset());
			for (String line : lines)	{
				String[] lineElements = line.split(WHITESPACE_CHARACTER_REGEX);

				Matcher matcher = COMMUNITY_NUMBER_PATTERN.matcher(lineElements[0]);
				if (!matcher.matches())	{
					continue;
				}

				StringBuilder builder = new StringBuilder();
				boolean areAllIDsCorrect = true;
				for (int i=1 ; i<lineElements.length ; i++)	{
					matcher = COMMUNITY_ID_PATTERN.matcher(lineElements[i]);
					if (!matcher.matches())	{
						areAllIDsCorrect = false;
						break;
					}
					builder.append(' ').append(lineElements[i]);
				}
				if (!areAllIDsCorrect)	{
					continue;
				}

				String communityLine = builder.toString().trim();
				result.addCommunityLine(communityLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}


	private static void removeTemporaryOutput(String pathname)	{
		File candidate = new File(pathname);
		if (!candidate.exists())	{
			return;
		}

		if (candidate.isDirectory())	{
			for (String inner : candidate.list())	{
				removeTemporaryOutput(pathname + FILES_SEPARATOR + inner);
			}
		}

		candidate.delete();
	}
}
