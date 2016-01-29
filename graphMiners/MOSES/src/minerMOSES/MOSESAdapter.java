package minerMOSES;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;

import ru.ispras.modis.NetBlox.exceptions.ExternalException;
import ru.ispras.modis.NetBlox.exceptions.GraphMiningException;
import ru.ispras.modis.NetBlox.utils.ExternalApplicationProvider;

public class MOSESAdapter extends ExternalApplicationProvider {
	private static final String APP_LINUX_X86_64 = "moses-binary-linux-x86-64";
	private static final String APP_LINUX_32 = "moses-binary-linux-32bit";
	private static final String APP_I386 = "moses-binary-Mach-O-i386";

	private static final String MINERS_ROOT;
	private static final String APPLICATION;
	static	{
		MINERS_ROOT = getAppsForPluginRoot(Activator.getContext().getBundle(), "apps");

		if (SystemUtils.IS_OS_LINUX)	{
			if (SystemUtils.OS_ARCH.equalsIgnoreCase("amd64"))	{
				APPLICATION = MINERS_ROOT + APP_LINUX_X86_64;
			}
			else	{
				APPLICATION = MINERS_ROOT + APP_LINUX_32;
			}
		}
		else if (SystemUtils.IS_OS_WINDOWS)	{
			APPLICATION = null;
		}

		else	{
			APPLICATION = MINERS_ROOT + APP_I386;
		}
	}

	public static final String DISCOVERED_COMMUNITIES_PATH_STRING = getTempFolderPathString() + FILES_SEPARATOR + "moses.tmp";


	public static void mine(String graphFilePathString) throws GraphMiningException	{
		if (SystemUtils.IS_OS_WINDOWS)	{
			String notThisOS = "This plug-in is based on the author's implementation of MOSES algorithm which can't be run under Windows OS.";
			throw new GraphMiningException(notThisOS);
		}

		List<String> command = generateCommand(graphFilePathString, DISCOVERED_COMMUNITIES_PATH_STRING);
		try {
			runExternal(command, MINERS_ROOT, null);
		} catch (ExternalException e) {
			throw new GraphMiningException(e);
		}
	}


	private static List<String> generateCommand(String graphPath, String resultPath)	{
		List<String> command = new LinkedList<String>();
		command.add(APPLICATION);
		command.add(graphPath);
		command.add(resultPath);

		return command;
	}
}
