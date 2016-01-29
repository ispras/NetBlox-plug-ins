package minerOSLOM;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;

public class DescriptionGCD_OSLOM extends DescriptionGraphMiningAlgorithm {
	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		if (doLaunchSeveralTimes())	{
			return new GCD_ParametersIterator();
		}

		ParametersSet parametersSet = new OSLOM_ParametersSet(getNameInScenario(), getId());
		ArrayList<ParametersSet> oneItemArray = new ArrayList<ParametersSet>(1);
		oneItemArray.add(parametersSet);

		return oneItemArray.iterator();
	}


	/**
	 * Iterator over the combinations of OSLOM parameters.
	 * 
	 * @author ilya
	 */
	private class GCD_ParametersIterator extends AlgorithmParametersIterator	{
		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no OSLOM parameters available for next iteration as requested.");
			}

			OSLOM_ParametersSet parametersSet = new OSLOM_ParametersSet(getNameInScenario(), getId());
			parametersSet.setLaunchNumber(makeValueFromRangeInstance(launchNumbers, launchNumber));
			return parametersSet;
		}		
	}
}
