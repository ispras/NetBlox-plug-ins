package numericQualityNMI;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.MeasureComputationException;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;
import ru.ispras.modis.NetBlox.utils.NumericsExtractor;

public class NMIComputer extends ExternalApplicationProvider {
	private static final String NMI_COMPUTER;
	private static final String COMPUTER_ROOT;
	static	{
		COMPUTER_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");
		if (SystemUtils.IS_OS_WINDOWS)	{
			NMI_COMPUTER = COMPUTER_ROOT + "mutual.exe";
		}
		else	{
			NMI_COMPUTER = COMPUTER_ROOT + "mutual";
		}
	}

	public static Float compute(String referenceCoverPathString, String analysedCoverPathString) throws MeasureComputationException	{
		List<String> command = generateCommand(referenceCoverPathString, analysedCoverPathString);

		PipedInputStream processOutput = new PipedInputStream();
		try {
			OutputStream outputStream = new PipedOutputStream(processOutput);
			runExternal(command, COMPUTER_ROOT, outputStream);

			Float nmiValue = NumericsExtractor.extractSingleFloat(processOutput);
			return nmiValue;
		} catch (IOException | ExternalException e) {
			throw new MeasureComputationException(e);
		}
	}

	private static List<String> generateCommand(String referenceCoverPathString, String analysedCoverPathString)	{
		List<String> command = new LinkedList<String>();
		command.add(NMI_COMPUTER);
		command.add(referenceCoverPathString);
		command.add(analysedCoverPathString);

		return command;
	}
}
