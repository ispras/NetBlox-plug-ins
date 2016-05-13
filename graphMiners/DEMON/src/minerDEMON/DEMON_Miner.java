package minerDEMON;


import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

public class DEMON_Miner extends ExternalApplicationProvider {
	public static final String PLUGIN_ID = Activator.getContext().getBundle().getSymbolicName();

	private static final String APPLICATION_RUNNER = "python";
	private static final String APPLICATION;
	private static final String MINERS_ROOT;
	static final String OUTPUT_PATHSTRING;

	static	{
		MINERS_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");
		APPLICATION = MINERS_ROOT + "Demon.py";
		OUTPUT_PATHSTRING = getTempFolderPathString() + FILES_SEPARATOR + PLUGIN_ID + FILES_SEPARATOR + "communities";
	}

	private List<String> generateCommand(GraphOnDrive graphOnDrive, ParametersSetDEMON parameters)	{
		List<String> command = new LinkedList<String>();

		command.add(APPLICATION_RUNNER);
		command.add(APPLICATION);
		command.add(graphOnDrive.getGraphFilePathString());
		command.add(OUTPUT_PATHSTRING);
		command.add(Boolean.toString(graphOnDrive.isWeighted()));
		command.add(Float.toString(parameters.getEpsilon()));
		command.add(Integer.toString(parameters.getMinCommunitySize()));

		return command;
	}

	public void mine(GraphOnDrive graphOnDrive, ParametersSetDEMON parameters) throws GraphMiningException	{
		List<String> command = generateCommand(graphOnDrive, parameters);

		try {
			runExternal(command, MINERS_ROOT, null);
		} catch (ExternalException e) {
			throw new GraphMiningException("Could not run DEMON: "+e.getMessage());
		}
	}
}
