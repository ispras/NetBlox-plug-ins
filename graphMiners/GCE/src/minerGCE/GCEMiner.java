package minerGCE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

public class GCEMiner extends ExternalApplicationProvider {
	private static final String MINERS_ROOT;
	private static final String APPLICATION;
	static	{
		MINERS_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");

		if (SystemUtils.IS_OS_WINDOWS)	{
			APPLICATION = MINERS_ROOT + "GCECommunityFinder.exe";
		}
		else	{
			APPLICATION = MINERS_ROOT + "GCECommunityFinder";
		}

	}


	public static InputStream mine(GraphOnDrive graphOnDrive, GCE_ParametersSet parameters) throws GraphMiningException	{
		List<String> command = generateCommand(graphOnDrive, parameters);

		try {
			PipedInputStream gceOutput = new PipedInputStream();
			OutputStream processOutput = new PipedOutputStream(gceOutput);
			runExternal(command, MINERS_ROOT, processOutput);

			return gceOutput;

		} catch (ExternalException e) {
			throw new GraphMiningException("Could not run GCE: "+e.getMessage());
		} catch (IOException e) {
			throw new GraphMiningException("Could not process GCE output: "+e.getMessage());
		}
	}


	private static List<String> generateCommand(GraphOnDrive graphOnDrive, GCE_ParametersSet parameters)	{
		List<String> command = new LinkedList<String>();
		command.add(APPLICATION);
		command.add(graphOnDrive.getGraphFilePathString());
		command.add(String.valueOf(parameters.getMinimalCliqueSize()));
		command.add(String.valueOf(parameters.getMinimalValueForOneSeedToOverlapWithAnotherSeed()));
		command.add(String.valueOf(parameters.getAlphaValueForFitnessFunctionGreedilyExpandingCliques()));
		command.add(String.valueOf(parameters.getProportionOfNodesWithinCoreCliqueForSufficientCoverage()));

		return command;
	}
}
