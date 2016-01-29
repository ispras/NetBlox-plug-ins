package numericGraphStats;

import java.util.ArrayList;
import java.util.Iterator;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;

public class GraphStatsMiningDescription extends DescriptionGraphMiningAlgorithm {
	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}

	@Override
	public Iterator<ParametersSet> iterator() {
		GraphStatsParametersSet parametersSet = new GraphStatsParametersSet(getNameInScenario(), getId());
		ArrayList<ParametersSet> oneItemArray = new ArrayList<ParametersSet>(1);
		oneItemArray.add(parametersSet);

		return oneItemArray.iterator();
	}
}
