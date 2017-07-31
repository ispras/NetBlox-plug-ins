package graphProviderExternal;

import java.util.ArrayList;
import java.util.Iterator;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphsOneType;
import ru.ispras.modis.NetBlox.scenario.GraphParametersSet;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;

/**
 * Description of uncategorised graph parsed from the scenario file. Includes
 * the path to the directory with graph file and the name of that file.
 * 
 * @author ilya
 */
public class DescriptionGraphUncategorised extends DescriptionGraphsOneType {
	private String directoryPathname;
	private String graphFileName;


	public void setDirectoryPathname(String directoryPath)	{
		this.directoryPathname = directoryPath;
	}

	public void setGraphFileName(String fileName)	{
		this.graphFileName = fileName;
	}


	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		ArrayList<ParametersSet> uncategorisedParametersList = new ArrayList<ParametersSet>(1);

		GraphParametersSet parametersSet;
		if (launchNumbers == null)	{
			parametersSet = new ParametersSetExternalGraph(getId(), directed, weighted, directoryPathname, graphFileName, null);
			setSpecifiedFilenames(parametersSet);
			uncategorisedParametersList.add(parametersSet);
		}
		else	{
			for (Integer number : launchNumbers)	{
				ValueFromRange<Integer> generationNumber = new ValueFromRange<Integer>(launchNumbers.getRangeId(), number);
				parametersSet = new ParametersSetExternalGraph(getId(), directed, weighted, directoryPathname, graphFileName, generationNumber);
				setSpecifiedFilenames(parametersSet);
				uncategorisedParametersList.add(parametersSet);
			}
		}

		return uncategorisedParametersList.iterator();
	}
}
