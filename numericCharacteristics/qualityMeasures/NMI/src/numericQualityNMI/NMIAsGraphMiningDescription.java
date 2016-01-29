package numericQualityNMI;

import java.util.ArrayList;
import java.util.Iterator;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;

public class NMIAsGraphMiningDescription extends DescriptionGraphMiningAlgorithm {
	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}

	@Override
	public Iterator<ParametersSet> iterator() {
		NMIMiningParameters parametersSet = new NMIMiningParameters(getNameInScenario(), getId());
		ArrayList<ParametersSet> oneItemArray = new ArrayList<ParametersSet>(1);
		oneItemArray.add(parametersSet);

		return oneItemArray.iterator();
	}
}
