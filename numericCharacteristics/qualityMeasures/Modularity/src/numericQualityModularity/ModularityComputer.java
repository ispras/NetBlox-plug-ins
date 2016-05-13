package numericQualityModularity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;
import ru.ispras.modis.NetBlox.utils.NumericsExtractor;

public class ModularityComputer extends ExternalApplicationProvider {

	private static final String APPLICATION_RUNNER = "python";

	private static final String PYTHON_SCRIPT;
	private static final String COMPUTER_ROOT;
	static	{
		COMPUTER_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");
		PYTHON_SCRIPT = COMPUTER_ROOT + "modularity_computer.py";
	}

	protected int snapBasedTaskNumberCode;

	private List<String> generateCommand(GraphOnDrive graphOnDrive, String communitiesFilePathString, ParametersSetModularity parameters)	{
		List<String> command = new LinkedList<String>();

		command.add(APPLICATION_RUNNER);
		command.add(PYTHON_SCRIPT);
		command.add(graphOnDrive.getGraphFilePathString());
		command.add(Boolean.toString(graphOnDrive.isDirected()));
		command.add(Boolean.toString(graphOnDrive.isWeighted()));
		command.add(String.valueOf(communitiesFilePathString));
		command.add(String.valueOf(parameters.getFormulaType()));
		command.add(String.valueOf(parameters.getBelongingFunction()));
		command.add(String.valueOf(parameters.getbelongingCoefficient()));

		return command;
	}

	public NumericCharacteristic compute(GraphOnDrive graphOnDrive,
			String communitiesFilePathString, ParametersSetModularity parameters)
			throws MeasureComputationException {
		List<String> command = generateCommand(graphOnDrive,
				communitiesFilePathString, parameters);

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
