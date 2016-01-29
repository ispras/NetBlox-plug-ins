package numericGraphStats.computers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.List;

import numericGraphStats.Activator;
import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;
import ru.ispras.modis.NetBlox.utils.NumericsExtractor;

public abstract class SNAPBasedComputer extends ExternalApplicationProvider {
	private static final String APPLICATION_RUNNER = "python";

	private static final String SNAP_ADAPTER_SCRIPT;
	private static final String COMPUTER_ROOT;
	static	{
		COMPUTER_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");
		SNAP_ADAPTER_SCRIPT = COMPUTER_ROOT + "SNAP_adapter.py";
	}

	protected int snapBasedTaskNumberCode;


	public NumericCharacteristic compute(GraphOnDrive graphOnDrive) throws MeasureComputationException	{
		List<String> command = generateCommand(graphOnDrive);

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


	private List<String> generateCommand(GraphOnDrive graphOnDrive)	{
		List<String> command = new LinkedList<String>();

		command.add(APPLICATION_RUNNER);
		command.add(SNAP_ADAPTER_SCRIPT);
		command.add(graphOnDrive.getGraphFilePathString());
		command.add(Boolean.toString(graphOnDrive.isDirected()));
		command.add(String.valueOf(snapBasedTaskNumberCode));

		return command;
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
