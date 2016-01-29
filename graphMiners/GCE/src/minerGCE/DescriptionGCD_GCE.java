package minerGCE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;

public class DescriptionGCD_GCE extends DescriptionGraphMiningAlgorithm {
	private int minimalCliqueSize = 4;

	public void setMinimalCliqueSize(int size)	{
		minimalCliqueSize = size;
	}


	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		if (doLaunchSeveralTimes())	{
			return new GCD_ParametersIterator();
		}

		ParametersSet parametersSet = new GCE_ParametersSet(getNameInScenario(), getId(), minimalCliqueSize);
		ArrayList<ParametersSet> oneItemArray = new ArrayList<ParametersSet>(1);
		oneItemArray.add(parametersSet);

		return oneItemArray.iterator();
	}


	/**
	 * Iterator over the combinations of GCE parameters.
	 * 
	 * @author ilya
	 */
	private class GCD_ParametersIterator extends AlgorithmParametersIterator	{
		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no GCE parameters available for next iteration as requested.");
			}

			GCE_ParametersSet parametersSet = new GCE_ParametersSet(getNameInScenario(), getId(), minimalCliqueSize);
			parametersSet.setLaunchNumber(makeValueFromRangeInstance(launchNumbers, launchNumber));
			return parametersSet;
		}		
	}
}
