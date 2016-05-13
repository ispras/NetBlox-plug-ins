package minerCFinderAdapter;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class DescriptionCFinder extends DescriptionGraphMiningAlgorithm {
	private RangeOfValues<Float> minWeightThresholds = null;
	private RangeOfValues<Float> maxWeightThresholds = null;
	private RangeOfValues<Float> maxCliquesSearchTime = null;
	private RangeOfValues<Float> weightIntensityThresholds = null;
	private RangeOfValues<Integer> kCliquesSizes = null;


	public void setMinWeightTresholds(RangeOfValues<Float> values)	{
		minWeightThresholds = values;
	}

	public void setMaxWeightTresholds(RangeOfValues<Float> values)	{
		maxWeightThresholds = values;
	}

	public void setMaxCliquesSearchTimes(RangeOfValues<Float> values)	{
		maxCliquesSearchTime = values;
	}

	public void setWeightIntensityTresholds(RangeOfValues<Float> values)	{
		weightIntensityThresholds = values;
	}

	public void setCliquesSizes(RangeOfValues<Integer> values)	{
		kCliquesSizes = values;
	}


	@Override
	public Collection<RangeOfValues<?>> getAllVariations()	{
		Collection<RangeOfValues<?>> variations = super.getAllVariations();

		addNonNullVariation(minWeightThresholds, variations);
		addNonNullVariation(maxWeightThresholds, variations);
		addNonNullVariation(maxCliquesSearchTime, variations);
		addNonNullVariation(weightIntensityThresholds, variations);
		addNonNullVariation(kCliquesSizes, variations);

		return variations;
	}

	@Override
	protected Bundle getImplementingPluginBundle() {
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		return new CFinderParametersIterator();
	}



	private class CFinderParametersIterator extends AlgorithmParametersIterator	{
		private Iterator<Float> minWeightThresholdsIterator = getIterator(minWeightThresholds);
		private Iterator<Float> maxWeightThresholdsIterator = getIterator(maxWeightThresholds);
		private Iterator<Float> maxCliquesSearchTimeIterator = getIterator(maxCliquesSearchTime);
		private Iterator<Float> weightIntensityThresholdsIterator = getIterator(weightIntensityThresholds);
		private Iterator<Integer> cliquesSizesIterator = getIterator(kCliquesSizes);

		private Float minWeightThreshold = null;
		private Float maxWeightThreshold = null;
		private Float maxCliqueTime = null;
		private Float weightIntensityThreshold = null;
		private Integer kCliqueSize = null;


		public CFinderParametersIterator()	{
			minWeightThreshold = initiateValue(minWeightThresholdsIterator);	//XXX The order matters here and in resolveValues(). That's no good.
			maxWeightThreshold = initiateValue(maxWeightThresholdsIterator);
			maxCliqueTime = initiateValue(maxCliquesSearchTimeIterator);
			weightIntensityThreshold = initiateValue(weightIntensityThresholdsIterator);
			kCliqueSize = initiateValue(cliquesSizesIterator);

			launchNumber = initiateValue(launchesIterator);

			if (!hasNext())	{ //In case ALL ranges are absent, we know at the moment of initialisation we won't be able to perform a single iteration.
				hasSingleIteration = true;
			}
		}


		@Override
		protected boolean resolveValues()	{
			if (!hasNext(minWeightThresholdsIterator))	{
				if (!hasNext(maxWeightThresholdsIterator))	{
					if (!hasNext(maxCliquesSearchTimeIterator))	{
						if (!hasNext(weightIntensityThresholdsIterator))	{
							if (!hasNext(cliquesSizesIterator))	{
								if (!super.resolveValues())	{
									return false;
								}
								cliquesSizesIterator = getIterator(kCliquesSizes);
							}
							kCliqueSize = getNext(cliquesSizesIterator);
							weightIntensityThresholdsIterator = getIterator(weightIntensityThresholds);
						}
						weightIntensityThreshold = getNext(weightIntensityThresholdsIterator);
						maxCliquesSearchTimeIterator = getIterator(maxCliquesSearchTime);
					}
					maxCliqueTime = getNext(maxCliquesSearchTimeIterator);
					maxWeightThresholdsIterator = getIterator(maxWeightThresholds);
				}
				maxWeightThreshold = getNext(maxWeightThresholdsIterator);
				minWeightThresholdsIterator = getIterator(minWeightThresholds);
			}
			minWeightThreshold = getNext(minWeightThresholdsIterator);

			return true;
		}

		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no CFinder parameters available for next iteration as requested.");
			}

			ParametersSetCFinder parametersSet = new ParametersSetCFinder(getNameInScenario(), getId(),
					makeValueFromRangeInstance(minWeightThresholds, minWeightThreshold),
					makeValueFromRangeInstance(maxWeightThresholds, maxWeightThreshold),
					makeValueFromRangeInstance(maxCliquesSearchTime, maxCliqueTime),
					makeValueFromRangeInstance(weightIntensityThresholds, weightIntensityThreshold),
					makeValueFromRangeInstance(kCliquesSizes, kCliqueSize));
			parametersSet.setLaunchNumber(makeValueFromRangeInstance(launchNumbers, launchNumber));
			return parametersSet;
		}


		@Override
		public boolean hasNext() {
			return hasNext(minWeightThresholdsIterator) || hasNext(maxWeightThresholdsIterator) ||
					hasNext(maxCliquesSearchTimeIterator) || hasNext(weightIntensityThresholdsIterator) ||
					hasNext(cliquesSizesIterator) || super.hasNext();
		}
	}
}
