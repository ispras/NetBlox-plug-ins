package graphProviderExternal;

import java.util.ArrayList;
import java.util.Iterator;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphsOneType;
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

		if (launchNumbers == null)	{
			uncategorisedParametersList.add(new ParametersSetExternalGraph(getId(), directed, weighted,
					directoryPathname, graphFileName, referenceCommunitiesRelativeFileName,
					externalSetsForMiningFiles, externalSetsForCharacterizationFiles, attributesFileName, null));
		}
		else	{
			for (Integer number : launchNumbers)	{
				ValueFromRange<Integer> generationNumber = new ValueFromRange<Integer>(launchNumbers.getRangeId(), number);
				uncategorisedParametersList.add(new ParametersSetExternalGraph(getId(), directed, weighted,
						directoryPathname, graphFileName, referenceCommunitiesRelativeFileName,
						externalSetsForMiningFiles, externalSetsForCharacterizationFiles, attributesFileName, generationNumber));
			}
		}

		return uncategorisedParametersList.iterator();
	}
}
