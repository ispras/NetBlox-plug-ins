package numericCommunitiesMeasuresABL;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.dataStructures.IGraph;
import ru.ispras.modis.NetBlox.dataStructures.IGraph.INode;
import ru.ispras.modis.NetBlox.dataStructures.IGroupOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.ISetOfGroupsOfNodes;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;
import ru.ispras.modis.NetBlox.utils.NumericsExtractor;
import ru.ispras.modis.NetBlox.utils.Pair;

public class CommunitiesMeasuresABLComputer extends  ExternalApplicationProvider {
	public static final String PLUGIN_ID = Activator.getContext().getBundle().getSymbolicName();

	private static final String APPLICATION_RUNNER = "python";

	private static final String PYTHON_SCRIPT;
	private static final String COMPUTER_ROOT;
	public static final String OUTPUT_DIRECTORY_PATHSTRING;
	static	{
		COMPUTER_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");
		PYTHON_SCRIPT = COMPUTER_ROOT + "measuresABL_computer.py";
		OUTPUT_DIRECTORY_PATHSTRING = getTempFolderPathString() + FILES_SEPARATOR + PLUGIN_ID + FILES_SEPARATOR;
	}

	private List<String> generateCommand(GraphOnDrive graphOnDrive, String communitiesFilePathString, int measureCode)	{
		List<String> command = new LinkedList<String>();

		command.add(APPLICATION_RUNNER);
		command.add(PYTHON_SCRIPT);
		command.add(graphOnDrive.getGraphFilePathString());
		command.add(Boolean.toString(graphOnDrive.isDirected()));
		command.add(Boolean.toString(graphOnDrive.isWeighted()));
		command.add(String.valueOf(communitiesFilePathString));
		command.add(String.valueOf(measureCode));

		return command;
	}

	private List<String> generateCommandWithAttr(IGraph graph, String pathToGraphFile,
			String pathToGraphAttributesFile, String pathToCommunitiesFilePathString,
			int measureCode) {
		List<String> command = new LinkedList<String>();

		command.add(APPLICATION_RUNNER);
		command.add(PYTHON_SCRIPT);
		command.add(pathToGraphFile);
		command.add(Boolean.toString(graph.isDirected()));
		command.add(Boolean.toString(graph.isWeighted()));
		command.add(pathToCommunitiesFilePathString);
		command.add(String.valueOf(measureCode));
		command.add(pathToGraphAttributesFile);

		return command;
	}


	public NumericCharacteristic compute(GraphOnDrive graphOnDrive, String communitiesFilePathString, int measureCode) throws MeasureComputationException	{
		List<String> command = generateCommand(graphOnDrive, communitiesFilePathString, measureCode);

		PipedInputStream processOutput = new PipedInputStream();
		try {
			OutputStream outputStream = new PipedOutputStream(processOutput);
			runExternal(command, COMPUTER_ROOT, outputStream);

			NumericCharacteristic result = extractResult(processOutput);
			return result;
		} catch (IOException | ExternalException e) {
			throw new MeasureComputationException(e);
		}
	}

	public NumericCharacteristic compute(IGraph graph, ISetOfGroupsOfNodes groupsOfNodes, int measureCode,
			String attributeName) throws MeasureComputationException {
		String pathToGraphFile = OUTPUT_DIRECTORY_PATHSTRING + "network.dat";
		String pathToGraphAttributesFile = OUTPUT_DIRECTORY_PATHSTRING + "nodes_attributes.dat";
		String pathToCommunitiesFilePathString = OUTPUT_DIRECTORY_PATHSTRING + "communities.dat";
		
		Path outputPath = Paths.get(OUTPUT_DIRECTORY_PATHSTRING);
		if (!Files.exists(outputPath))	{
			try {
				Files.createDirectories(outputPath);
			} catch (IOException e) {
				throw new MeasureComputationException(e);
			}
		}
		
		createGraphFile(graph, pathToGraphFile);
		createCommunitiesFile(groupsOfNodes, pathToCommunitiesFilePathString);
		createFileWithAttributes(graph, pathToGraphAttributesFile, attributeName);

		List<String> command = generateCommandWithAttr(graph, pathToGraphFile,
				pathToGraphAttributesFile, pathToCommunitiesFilePathString, measureCode);

		PipedInputStream processOutput = new PipedInputStream();
		try {
			OutputStream outputStream = new PipedOutputStream(processOutput);
			runExternal(command, COMPUTER_ROOT, outputStream);

			NumericCharacteristic result = extractResult(processOutput);
			return result;
		} catch (IOException | ExternalException e) {
			throw new MeasureComputationException(e);
		}
	}


	private void createGraphFile(IGraph graph, String pathToGraphFile) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathToGraphFile), "utf-8"))) {
			for (Pair<INode, INode> edge: graph.getEdges()) {
				INode node1 = edge.get1st();
				INode node2 = edge.get2nd();
				writer.write(node1.getId() + " " + node2.getId());
				if (graph.isWeighted()) {
					writer.write(" " + graph.getEdgeWeight(node1, node2).toString());
				}
				writer.write("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createCommunitiesFile(ISetOfGroupsOfNodes groupsOfNodes, String pathToCommunitiesFilePathString) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathToCommunitiesFilePathString), "utf-8"))) {
			Iterator<IGroupOfNodes> iter = groupsOfNodes.iterator();
			while(iter.hasNext()) {
				IGroupOfNodes group = iter.next();
				Iterator<INode> it = group.iterator();
				while (it.hasNext()) {
					// FIXME no final " "
					writer.write(it.next().getId() + " ");
				}
				writer.write("\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFileWithAttributes(IGraph graph, String pathToGraphAttributesFile, String attributeName) throws MeasureComputationException {
		List<String> nodesAttributesNames = graph.getNodeAttributesNames();
		if (!nodesAttributesNames.contains(attributeName)) {
			throw new MeasureComputationException("Given graph doesn't have attribute '" + attributeName +"'.");
		}
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathToGraphAttributesFile), "utf-8"))) {
			for (INode node: graph.getNodes()) {
				writer.write(node.getId() + " " + node.getAttribute(attributeName) + "\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Extracts the result value from the stdout of python script that has been used.
	 * Should be overridden in children if the expected result is more complex than a single float value.
	 * @param stream
	 * @return
	 * @throws IOException 
	 * @throws MeasureComputationException 
	 */
	protected NumericCharacteristic extractResult(InputStream stream) throws IOException, MeasureComputationException	{
		Float value = NumericsExtractor.extractSingleFloat(stream);
		NumericCharacteristic result =  new NumericCharacteristic(NumericCharacteristic.Type.SINGLE_VALUE, value);
		return result;
	}
}