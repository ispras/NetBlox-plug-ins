package minerLinkComm;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class DescriptionLinkComm extends DescriptionGraphMiningAlgorithm {
	private RangeOfValues<Float> thresholds;

	public void setThresholds(RangeOfValues<Float> thresholds)	{
		this.thresholds = thresholds;
	}


	@Override
	public Collection<RangeOfValues<?>> getAllVariations()	{
		Collection<RangeOfValues<?>> variations = super.getAllVariations();
		addNonNullVariation(thresholds, variations);
		return variations;
	}

	@Override
	protected Bundle getImplementingPluginBundle() {
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		return new LinkCommParametersIterator();
	}



	private class LinkCommParametersIterator extends AlgorithmParametersIterator	{
		private Iterator<Float> thresholdsIterator = getIterator(thresholds);
		private Float threshold = null;


		public LinkCommParametersIterator()	{
			threshold = initiateValue(thresholdsIterator);

			launchNumber = initiateValue(launchesIterator);

			//	This is unnecessary, as threshold is an obligate parameter.
			//if (!hasNext())	{ //In case ALL ranges are absent, we know at the moment of initialisation we won't be able to perform a single iteration.
			//	hasSingleIteration = true;
			//}
		}


		@Override
		protected boolean resolveValues()	{
			if (!hasNext(thresholdsIterator))	{
				if (!super.resolveValues())	{
					return false;
				}
				thresholdsIterator = getIterator(thresholds);
			}
			threshold = getNext(thresholdsIterator);
			return true;
		}

		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no Link Communities detection parameters available for next iteration as requested.");
			}

			ParametersSet_LinkComm parametersSet = new ParametersSet_LinkComm(getNameInScenario(), getId(),
					makeValueFromRangeInstance(thresholds, threshold));
			parametersSet.setLaunchNumber(makeValueFromRangeInstance(launchNumbers, launchNumber));
			return parametersSet;
		}


		@Override
		public boolean hasNext() {
			return hasNext(thresholdsIterator) || super.hasNext();
		}
	}
}
