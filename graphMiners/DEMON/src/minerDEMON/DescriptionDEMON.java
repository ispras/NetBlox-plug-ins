package minerDEMON;


import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class DescriptionDEMON extends DescriptionGraphMiningAlgorithm {
	
	private RangeOfValues<Float> epsilons = null;
	private RangeOfValues<Integer> minCommunitiesSizes = null;

	public void setEpsilons(RangeOfValues<Float> epsilons) {
		this.epsilons = epsilons;
	}

	public void setMinCommunitiesSizes(RangeOfValues<Integer> values)	{
		minCommunitiesSizes = values;
	}


	@Override
	public Collection<RangeOfValues<?>> getAllVariations()	{
		Collection<RangeOfValues<?>> variations = super.getAllVariations();

		addNonNullVariation(epsilons, variations);
		addNonNullVariation(minCommunitiesSizes, variations);
		return variations;
	}

	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		return new DEMONParametersIterator();
	}


	private class DEMONParametersIterator extends AlgorithmParametersIterator	{
		private Iterator<Float> epsilonsIterator = getIterator(epsilons);
		private Iterator<Integer> minCommunitiesSizesIterator = getIterator(minCommunitiesSizes);

		private Float epsilonValue = null;
		private Integer minimalCommunitySizeValue = null;

		public DEMONParametersIterator()	{
			epsilonValue = initiateValue(epsilonsIterator);
			minimalCommunitySizeValue = initiateValue(minCommunitiesSizesIterator);

			if (!hasNext())	{ //In case ALL ranges are absent, we know at the moment of initialization we won't be able to perform a single iteration.
				hasSingleIteration = true;
			}
		}

		@Override
		protected boolean resolveValues()	{
			if (!hasNext(epsilonsIterator))	{
				if (!hasNext(minCommunitiesSizesIterator))	{
					if (!super.resolveValues())	{
						return false;
					}
					minCommunitiesSizesIterator = getIterator(minCommunitiesSizes);
				}
				epsilonsIterator = getIterator(epsilons);
				minimalCommunitySizeValue = getNext(minCommunitiesSizesIterator);
			}
			epsilonValue = getNext(epsilonsIterator);

			return true;
		}

		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no DEMON parameters available for next iteration as requested.");
			}

			ParametersSetDEMON parametersSet = new ParametersSetDEMON(getNameInScenario(), getId(),
					makeValueFromRangeInstance(epsilons, epsilonValue),
					makeValueFromRangeInstance(minCommunitiesSizes, minimalCommunitySizeValue));
			parametersSet.setLaunchNumber(makeValueFromRangeInstance(launchNumbers, launchNumber));
			return parametersSet;
		}


		@Override
		public boolean hasNext() {
			return hasNext(epsilonsIterator) || hasNext(minCommunitiesSizesIterator) || super.hasNext();
		}
	}
}
