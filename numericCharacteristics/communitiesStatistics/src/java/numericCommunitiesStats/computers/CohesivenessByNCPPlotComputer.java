package numericCommunitiesStats.computers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import numericCommunitiesStats.Activator;
import numericCommunitiesStats.PluginConfiguration;

import org.apache.commons.lang.SystemUtils;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.utils.NumericsExtractor;
import ru.ispras.modis.NetBlox.utils.Pair;

public class CohesivenessByNCPPlotComputer extends DataInsideStatisticComputer {
	public static final String NAME_IN_SCENARIO = "commCohesiveness";

	private static final int COMMUNITY_SIZE_LIMIT;

	private static final String NCP_PLOT_DIRECTORY_PATHNAME;
	private static final String NCP_PLOT_APPLICATION;
	private static final String TEMP_INDUCED_SUBGRAPH_PATH;
	private static final String TEMP_NCPPLOT_OUTPUT_PATH_BASE;
	private static final String TEMP_NCPPLOT_OUTPUT_PATH;
	static	{
		NCP_PLOT_DIRECTORY_PATHNAME = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");
		if (SystemUtils.IS_OS_WINDOWS)	{
			NCP_PLOT_APPLICATION = NCP_PLOT_DIRECTORY_PATHNAME + "ncpplot.exe";
		}
		else	{
			NCP_PLOT_APPLICATION = NCP_PLOT_DIRECTORY_PATHNAME + "ncpplot";
		}
		TEMP_INDUCED_SUBGRAPH_PATH = getTempFolderPathString()+FILES_SEPARATOR+"inducedSubgraph.tmp";
		TEMP_NCPPLOT_OUTPUT_PATH_BASE = "ncpplot.res.tmp";
		TEMP_NCPPLOT_OUTPUT_PATH = NCP_PLOT_DIRECTORY_PATHNAME + "ncp." + TEMP_NCPPLOT_OUTPUT_PATH_BASE + ".tab";

		String bundleDirectoryName = new File(NCP_PLOT_DIRECTORY_PATHNAME).getParent();
		COMMUNITY_SIZE_LIMIT = PluginConfiguration.getInstance(bundleDirectoryName).getNCPPlotClusterSizeLimit();
	}


	@Override
	public NumericCharacteristic compute(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes) {
		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.LIST_OF_VALUES);

		for (IGroupOfNodes group : groupsOfNodes)	{
			Float cohesion = null;
			if (group.size() == 1)	{
				cohesion = (float) 1;
			}
			else if (group.size() > COMMUNITY_SIZE_LIMIT)	{
				cohesion = (float) 1;
				System.out.println("\t\tWARNING:\tThe size of group of nodes is greater then " + COMMUNITY_SIZE_LIMIT + ". Setting cohesiveness = 1.0 .");
			}
			else	{
				try	{
					cohesion = computeCohesiveness(graph, group);
				}
				catch (MeasureComputationException mce)	{
					mce.printStackTrace();
					cohesion = (float) 1;
				}
			}

			result.addValue(cohesion.doubleValue());
		}
		System.out.println("\t\tDone with computing Cohesiveness via NCPPlot.");

		return result;
	}


	private Float computeCohesiveness(IGraph graph, IGroupOfNodes group) throws MeasureComputationException	{
		IGraph communitySubgraph = graph.getSubgraphForGroup(group);

		boolean writeSuccess = writeFileWithSubgraph(communitySubgraph);
		if (!writeSuccess)	{
			String warning = "\t\tWARNING: The nodes of the cluster "+group.toString()+" are not connected by edges. Setting cohesiveness = 1.0 .";
			System.out.println(warning);
			return (float) 1;
		}

		List<String> ncpPlotCommand = generateCommand();
		try {
			runExternal(ncpPlotCommand, NCP_PLOT_DIRECTORY_PATHNAME, null);
		} catch (ExternalException e) {
			String errorMessage = "Failed to compute Cohesiveness for group of nodes: "+group.toString()+"\nNCPPlot ended with error: "+e.getMessage();
			throw new MeasureComputationException(errorMessage);
		}

		Collection<Float> ncpValues = null;
		try {
			ncpValues = extractSecondFloatValuesFromLines(TEMP_NCPPLOT_OUTPUT_PATH);
		} catch (IOException e) {
			throw new MeasureComputationException(e);
		}
		if (ncpValues == null  ||  ncpValues.isEmpty())	{
			throw new MeasureComputationException("NCPPlot didn't find any potential cohesiveness values.");
		}

		Float cohesiveness = Collections.min(ncpValues);

		return cohesiveness;
	}


	private boolean writeFileWithSubgraph(IGraph subgraph) throws MeasureComputationException	{
		Collection<Pair<IGraph.INode, IGraph.INode>> edges = subgraph.getEdges();

		List<String> linesWithEdges = new ArrayList<String>(edges.size());
		for (Pair<IGraph.INode, IGraph.INode> edge : edges)	{
			StringBuilder edgeLineBuilder = new StringBuilder().append(edge.get1st().getId()).append("\t").append(edge.get2nd().getId());
			linesWithEdges.add(edgeLineBuilder.toString());
		}
		if (linesWithEdges.isEmpty())	{
			return false;
		}

		try {
			Path path = Paths.get(TEMP_INDUCED_SUBGRAPH_PATH);
			Files.write(path, linesWithEdges, Charset.defaultCharset());
			return true;
		} catch (IOException e) {
			throw new MeasureComputationException(e);
		}
	}

	private static List<String> generateCommand()	{
		List<String> command = new LinkedList<String>();
		command.add(NCP_PLOT_APPLICATION);
		command.add("-i:"+TEMP_INDUCED_SUBGRAPH_PATH);
		command.add("-o:"+TEMP_NCPPLOT_OUTPUT_PATH_BASE);

		return command;
	}

	private static List<Float> extractSecondFloatValuesFromLines(String filePathString) throws IOException	{
		List<Float> values = new LinkedList<Float>();

		Path path = Paths.get(filePathString);
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		for (String line : lines)	{
			if (line.startsWith("#"))	{	//The simplified case of specifically NCPPlot output.
				continue;
			}
			Float value = NumericsExtractor.extractFloat(line);
			values.add(value);
		}

		return values;
	}
}
