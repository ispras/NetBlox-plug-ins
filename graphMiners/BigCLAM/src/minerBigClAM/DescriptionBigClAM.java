package minerBigClAM;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class DescriptionBigClAM extends DescriptionGraphMiningAlgorithm {
	private RangeOfValues<Integer> numbersOfCommunities = null;
	private RangeOfValues<Integer> minCommunitiesNumbers = null;
	private RangeOfValues<Integer> maxCommunitiesNumbers = null;
	private RangeOfValues<Integer> numbersOfTrialsOfCommunitiesNumbers = null;
	//numbersOfThreads?
	private RangeOfValues<Float> alphaBacktrackingValues = null;
	private RangeOfValues<Float> betaBacktrackingValues = null;


	public void setNumbersOfCommunities(RangeOfValues<Integer> values)	{
		numbersOfCommunities = values;
	}

	public void setMinNumbersOfCommunities(RangeOfValues<Integer> values)	{
		minCommunitiesNumbers = values;
	}

	public void setMaxNumbersOfCommunities(RangeOfValues<Integer> values)	{
		maxCommunitiesNumbers = values;
	}

	public void setNumbersOfTrialsOfCommunitiesNumbers(RangeOfValues<Integer> values)	{
		numbersOfTrialsOfCommunitiesNumbers = values;
	}

	public void setAlphaBacktrackingValues(RangeOfValues<Float> values)	{
		alphaBacktrackingValues = values;
	}

	public void setBetaBacktrackingValues(RangeOfValues<Float> values)	{
		betaBacktrackingValues = values;
	}


	@Override
	public Collection<RangeOfValues<?>> getAllVariations()	{
		Collection<RangeOfValues<?>> variations = super.getAllVariations();

		addNonNullVariation(numbersOfCommunities, variations);
		addNonNullVariation(minCommunitiesNumbers, variations);
		addNonNullVariation(maxCommunitiesNumbers, variations);
		addNonNullVariation(numbersOfTrialsOfCommunitiesNumbers, variations);

		addNonNullVariation(alphaBacktrackingValues, variations);
		addNonNullVariation(betaBacktrackingValues, variations);

		return variations;
	}

	@Override
	protected Bundle getImplementingPluginBundle() {
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		return new BigClAMParametersIterator();
	}



	private class BigClAMParametersIterator extends AlgorithmParametersIterator	{
		private Iterator<Integer> numbersOfCommunitiesIterator = getIterator(numbersOfCommunities);
		private Iterator<Integer> minCommunitiesNumbersIterator = getIterator(minCommunitiesNumbers);
		private Iterator<Integer> maxCommunitiesNumbersIterator = getIterator(maxCommunitiesNumbers);
		private Iterator<Integer> numbersOfTrialsOfCommunitiesNumbersIterator = getIterator(numbersOfTrialsOfCommunitiesNumbers);
		private Iterator<Float> alphaIterator = getIterator(alphaBacktrackingValues);
		private Iterator<Float> betaIterator = getIterator(betaBacktrackingValues);

		private Integer numberOfCommunities = null;
		private Integer minCommunitiesNumber = null;
		private Integer maxCommunitiesNumber = null;
		private Integer numberOfTrialsOfNumbers = null;
		private Float alphaValue = null;
		private Float betaValue = null;


		public BigClAMParametersIterator()	{
			numberOfCommunities = initiateValue(numbersOfCommunitiesIterator);	//XXX The order matters here and in resolveValues(). That's no good.
			minCommunitiesNumber = initiateValue(minCommunitiesNumbersIterator);
			maxCommunitiesNumber = initiateValue(maxCommunitiesNumbersIterator);
			numberOfTrialsOfNumbers = initiateValue(numbersOfTrialsOfCommunitiesNumbersIterator);
			alphaValue = initiateValue(alphaIterator);
			betaValue = initiateValue(betaIterator);

			launchNumber = initiateValue(launchesIterator);

			if (!hasNext())	{ //In case ALL ranges are absent, we know at the moment of initialisation we won't be able to perform a single iteration.
				hasSingleIteration = true;
			}
		}


		@Override
		protected boolean resolveValues()	{
			if (!hasNext(numbersOfCommunitiesIterator))	{
				if (!hasNext(minCommunitiesNumbersIterator))	{
					if (!hasNext(maxCommunitiesNumbersIterator))	{
						if (!hasNext(numbersOfTrialsOfCommunitiesNumbersIterator))	{
							if (!hasNext(alphaIterator))	{
								if (!hasNext(betaIterator))	{
									if (!super.resolveValues())	{
										return false;
									}
									betaIterator = getIterator(betaBacktrackingValues);
								}
								betaValue = getNext(betaIterator);
								alphaIterator = getIterator(alphaBacktrackingValues);
							}
							alphaValue = getNext(alphaIterator);
							numbersOfTrialsOfCommunitiesNumbersIterator = getIterator(numbersOfTrialsOfCommunitiesNumbers);
						}
						numberOfTrialsOfNumbers = getNext(numbersOfTrialsOfCommunitiesNumbersIterator);
						maxCommunitiesNumbersIterator = getIterator(maxCommunitiesNumbers);
					}
					maxCommunitiesNumber = getNext(maxCommunitiesNumbersIterator);
					minCommunitiesNumbersIterator = getIterator(minCommunitiesNumbers);
				}
				minCommunitiesNumber = getNext(minCommunitiesNumbersIterator);
				numbersOfCommunitiesIterator = getIterator(numbersOfCommunities);
			}
			numberOfCommunities = getNext(numbersOfCommunitiesIterator);

			return true;
		}

		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no BigClAM parameters available for next iteration as requested.");
			}

			ParametersSetBigClAM parametersSet = new ParametersSetBigClAM(getNameInScenario(), getId(),
					makeValueFromRangeInstance(numbersOfCommunities, numberOfCommunities),
					makeValueFromRangeInstance(minCommunitiesNumbers, minCommunitiesNumber),
					makeValueFromRangeInstance(maxCommunitiesNumbers, maxCommunitiesNumber),
					makeValueFromRangeInstance(numbersOfTrialsOfCommunitiesNumbers, numberOfTrialsOfNumbers),
					makeValueFromRangeInstance(alphaBacktrackingValues, alphaValue), makeValueFromRangeInstance(betaBacktrackingValues, betaValue));
			parametersSet.setLaunchNumber(makeValueFromRangeInstance(launchNumbers, launchNumber));
			return parametersSet;
		}


		@Override
		public boolean hasNext() {
			return hasNext(numbersOfCommunitiesIterator) ||
					hasNext(minCommunitiesNumbersIterator) || hasNext(maxCommunitiesNumbersIterator) ||
					hasNext(numbersOfTrialsOfCommunitiesNumbersIterator) ||
					hasNext(alphaIterator) || hasNext(betaIterator) || super.hasNext();
		}
	}
}
