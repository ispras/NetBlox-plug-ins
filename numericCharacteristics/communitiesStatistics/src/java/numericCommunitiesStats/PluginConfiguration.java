package numericCommunitiesStats;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class PluginConfiguration {
	private static final String CONFIG_FILE_NAME = "stats.properties.xml";

	private final XMLConfiguration config;
	private static PluginConfiguration configurationInstance;

	public PluginConfiguration(String pluginDirectoryPathname) throws ConfigurationException	{
		config = new XMLConfiguration();

		/*Bundle bundle = Activator.getContext().getBundle();
		URL configURL = bundle.getEntry(CONFIG_FILE_NAME);
		try {
			configURL = FileLocator.resolve(configURL);
		} catch (IOException e) {
			throw new PluginException("Couldn't initiate the "+bundle.getSymbolicName()+" plug-in: "+e.getMessage());
		}

		config.load(configURL);*/
		String configFileName = pluginDirectoryPathname + System.getProperty("file.separator") + CONFIG_FILE_NAME;
		config.load(new File(configFileName));
	}


	public static PluginConfiguration getInstance(String pluginDirectoryPathname)	{	//FUTURE_WORK Rewrite into something better looking.
		if (configurationInstance == null)	{
			try {
				configurationInstance = new PluginConfiguration(pluginDirectoryPathname);
			} catch (ConfigurationException e) {
				throw new RuntimeException(e);
			}
		}
		return configurationInstance;
	}

	//=======================================================================================================

	private static final String KEY_NCPPLOT_CLUSTER_SIZE_LIMIT = "cohesivenessByNCPPlotClusterSizeLimit";

	public int getNCPPlotClusterSizeLimit()	{
		String value = config.getString(KEY_NCPPLOT_CLUSTER_SIZE_LIMIT);
		return Integer.parseInt(value);
	}
}
