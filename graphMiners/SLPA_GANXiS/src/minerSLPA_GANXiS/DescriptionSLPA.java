package minerSLPA_GANXiS;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class DescriptionSLPA extends DescriptionGraphMiningAlgorithm {
	private RangeOfValues<Integer> numbersOfIterations = null;
	private RangeOfValues<Float> thresholds = null;
	private RangeOfValues<Integer> minimalCommunitiesSizes = null;
	private RangeOfValues<Integer> maximalCommunitiesSizes = null;


	public void setMaximalNumbersOfIterations(RangeOfValues<Integer> values)	{
		numbersOfIterations = values;
	}

	public void setThresholds(RangeOfValues<Float> thresholds)	{
		this.thresholds = thresholds;
	}

	public void setMinimalCommunitiesSizes(RangeOfValues<Integer> values)	{
		minimalCommunitiesSizes = values;
	}

	public void setMaximalCommunitiesSizes(RangeOfValues<Integer> values)	{
		maximalCommunitiesSizes = values;
	}


	@Override
	public Collection<RangeOfValues<?>> getAllVariations()	{
		Collection<RangeOfValues<?>> variations = super.getAllVariations();

		addNonNullVariation(numbersOfIterations, variations);
		addNonNullVariation(thresholds, variations);
		addNonNullVariation(minimalCommunitiesSizes, variations);
		addNonNullVariation(maximalCommunitiesSizes, variations);

		return variations;
	}

	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		return new SLPAParametersIterator();
	}



	private class SLPAParametersIterator extends AlgorithmParametersIterator	{
		private Iterator<Integer> numbersOfIterationsIterator = getIterator(numbersOfIterations);
		private Iterator<Float> thresholdsIterator = getIterator(thresholds);
		private Iterator<Integer> minimalCommunitiesSizesIterator = getIterator(minimalCommunitiesSizes);
		private Iterator<Integer> maximalCommunitiesSizesIterator = getIterator(maximalCommunitiesSizes);

		private Integer iterationsValue = null;
		private Float thresholdValue = null;
		private Integer minimalCommunitySizeValue = null;
		private Integer maximalCommunitySizeValue = null;


		public SLPAParametersIterator()	{
			iterationsValue = initiateValue(numbersOfIterationsIterator);	//XXX The order matters here and in resolveValues(). That's no good.
			thresholdValue = initiateValue(thresholdsIterator);
			minimalCommunitySizeValue = initiateValue(minimalCommunitiesSizesIterator);
			maximalCommunitySizeValue = initiateValue(maximalCommunitiesSizesIterator);

			launchNumber = initiateValue(launchesIterator);

			if (!hasNext())	{ //In case ALL ranges are absent, we know at the moment of initialisation we won't be able to perform a single iteration.
				hasSingleIteration = true;
			}
		}


		@Override
		protected boolean resolveValues()	{
			if (!hasNext(numbersOfIterationsIterator))	{
				if (!hasNext(thresholdsIterator))	{
					if (!hasNext(minimalCommunitiesSizesIterator))	{
						if (!hasNext(maximalCommunitiesSizesIterator))	{
							if (!super.resolveValues())	{
								return false;
							}
							maximalCommunitiesSizesIterator = getIterator(maximalCommunitiesSizes);
						}
						minimalCommunitiesSizesIterator = getIterator(minimalCommunitiesSizes);
						maximalCommunitySizeValue = getNext(maximalCommunitiesSizesIterator);
					}
					thresholdsIterator = getIterator(thresholds);
					minimalCommunitySizeValue = getNext(minimalCommunitiesSizesIterator);
				}
				numbersOfIterationsIterator = getIterator(numbersOfIterations);
				thresholdValue = getNext(thresholdsIterator);
			}
			iterationsValue = getNext(numbersOfIterationsIterator);

			return true;
		}

		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no SLPA (author's implementation) parameters available for next iteration as requested.");
			}

			ParametersSetSLPA parametersSet = new ParametersSetSLPA(getNameInScenario(), getId(),
					makeValueFromRangeInstance(numbersOfIterations, iterationsValue), makeValueFromRangeInstance(thresholds, thresholdValue),
					makeValueFromRangeInstance(minimalCommunitiesSizes, minimalCommunitySizeValue),
					makeValueFromRangeInstance(maximalCommunitiesSizes, maximalCommunitySizeValue));
			parametersSet.setLaunchNumber(makeValueFromRangeInstance(launchNumbers, launchNumber));
			return parametersSet;
		}


		@Override
		public boolean hasNext() {
			return hasNext(numbersOfIterationsIterator) || hasNext(thresholdsIterator) ||
					hasNext(minimalCommunitiesSizesIterator) || hasNext(maximalCommunitiesSizesIterator) || super.hasNext();
		}
	}
}
