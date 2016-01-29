package graphProviderLFR;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphsOneType;
import ru.ispras.modis.NetBlox.scenario.IteratorForParametersProbablyMissing;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;

public class DescriptionGraphsLFR extends DescriptionGraphsOneType {
	private RangeOfValues<Integer> om = null;	//Number of communities in an intersection.
	private RangeOfValues<Float> on_share = null; //Share of nodes that participate in intersections.

	private RangeOfValues<Integer> minimalCommunitySizes = null;
	private RangeOfValues<Integer> maximalCommunitySizes = null;
	private RangeOfValues<Float> mu = null;	//Mixing coefficient.

	private RangeOfValues<Integer> averageNodeDegrees = null;
	private RangeOfValues<Integer> maximalNodeDegrees = null;

	private RangeOfValues<Float> minusExponents1_degreeSequence = null;
	private RangeOfValues<Float> minusExponents2_commSizesDistribution = null;
	private RangeOfValues<Float> averageClusteringCoefficients = null;


	public void setOmValues(RangeOfValues<Integer> values)	{
		om = values;
	}

	public void setOnShareValues(RangeOfValues<Float> values)	{
		on_share = values;
	}

	public void setMinimalCommunitySize(RangeOfValues<Integer> sizes)	{
		minimalCommunitySizes = sizes;
	}

	public void setMaximalCommunitySize(RangeOfValues<Integer> sizes)	{
		maximalCommunitySizes = sizes;
	}

	public void setMu(RangeOfValues<Float> values)	{
		mu = values;
	}

	public void setAverageNodeDegree(RangeOfValues<Integer> k)	{
		averageNodeDegrees = k;
	}

	public void setMaximalNodeDegree(RangeOfValues<Integer> maxk)	{
		maximalNodeDegrees = maxk;
	}

	public void setExponent1(RangeOfValues<Float> values)	{
		minusExponents1_degreeSequence = values;
	}

	public void setExponent2(RangeOfValues<Float> values)	{
		minusExponents2_commSizesDistribution = values;
	}

	public void setAverageClusteringCoefficients(RangeOfValues<Float> values)	{
		averageClusteringCoefficients = values;
	}


	@Override
	public Collection<RangeOfValues<?>> getAllVariations()	{
		Collection<RangeOfValues<?>> variations = super.getAllVariations();

		addNonNullVariation(om, variations);
		addNonNullVariation(on_share, variations);
		addNonNullVariation(minimalCommunitySizes, variations);
		addNonNullVariation(maximalCommunitySizes, variations);
		addNonNullVariation(mu, variations);
		addNonNullVariation(averageNodeDegrees, variations);
		addNonNullVariation(maximalNodeDegrees, variations);
		addNonNullVariation(minusExponents1_degreeSequence, variations);
		addNonNullVariation(minusExponents2_commSizesDistribution, variations);
		addNonNullVariation(averageClusteringCoefficients, variations);

		return variations;
	}

	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		return new GraphIterator();
	}


	/**
	 * An iterator over the combinations of LFR parameters.
	 * @author ilya
	 */
	private class GraphIterator extends IteratorForParametersProbablyMissing	{
		private Iterator<Integer> nIterator = numbersOfNodes.iterator();
		private Iterator<Integer> omIterator = om.iterator();
		private Iterator<Float> onShareIterator = on_share.iterator();
		private Iterator<Integer> generationsIterator = getIterator(launchNumbers);

		private Iterator<Integer> minimalCommunitySizesIterator = getIterator(minimalCommunitySizes);
		private Iterator<Integer> maximalCommunitySizesIterator = getIterator(maximalCommunitySizes);
		private Iterator<Float> muIterator = getIterator(mu);
		private Iterator<Integer> averageNodeDegreesIterator = getIterator(averageNodeDegrees);
		private Iterator<Integer> maximalNodeDegreesIterator = getIterator(maximalNodeDegrees);
		private Iterator<Float> exponents1Iterator = getIterator(minusExponents1_degreeSequence);
		private Iterator<Float> exponents2Iterator = getIterator(minusExponents2_commSizesDistribution);
		private Iterator<Float> averageClusteringCoefficientIterator = getIterator(averageClusteringCoefficients);

		private Integer numberOfNodes;
		private Float onShareValue = onShareIterator.next();
		private Integer omValue = omIterator.next();
		private Integer generationsValue = getNext(generationsIterator);

		private Integer minimalCommunitySize = getNext(minimalCommunitySizesIterator);
		private Integer maximalCommunitySize = getNext(maximalCommunitySizesIterator);
		private Float muValue = getNext(muIterator);
		private Integer averageNodeDegree = getNext(averageNodeDegreesIterator);
		private Integer maximalNodeDegree = getNext(maximalNodeDegreesIterator);
		private Float minusExponent1 = getNext(exponents1Iterator);
		private Float minusExponent2 = getNext(exponents2Iterator);
		private Float averageClusteringCoefficient = getNext(averageClusteringCoefficientIterator);

		@Override
		public boolean hasNext() {
			return nIterator.hasNext() || omIterator.hasNext() || onShareIterator.hasNext() || hasNext(generationsIterator) ||
					hasNext(minimalCommunitySizesIterator) || hasNext(maximalCommunitySizesIterator) || hasNext(muIterator) ||
					hasNext(averageNodeDegreesIterator) || hasNext(maximalNodeDegreesIterator) ||
					hasNext(exponents1Iterator) || hasNext(exponents2Iterator) || hasNext(averageClusteringCoefficientIterator);
		}

		protected boolean resolveValues()	{
			//XXX Rewrite this via a recursive function.
			if (!nIterator.hasNext())	{
				if (!onShareIterator.hasNext())	{
					if (!omIterator.hasNext())	{
						if (!hasNext(generationsIterator))	{
							if (!hasNext(minimalCommunitySizesIterator))	{
								if (!hasNext(maximalCommunitySizesIterator))	{
									if (!hasNext(muIterator))	{
										if (!hasNext(averageNodeDegreesIterator))	{
											if (!hasNext(maximalNodeDegreesIterator))	{
												if (!hasNext(exponents1Iterator))	{
													if (!hasNext(exponents2Iterator))	{
														if (!hasNext(averageClusteringCoefficientIterator))	{
															return false;
														}
														exponents2Iterator = getIterator(minusExponents2_commSizesDistribution);
														averageClusteringCoefficient = getNext(averageClusteringCoefficientIterator);
													}
													exponents1Iterator = getIterator(minusExponents1_degreeSequence);
													minusExponent2 = getNext(exponents2Iterator);
												}
												maximalNodeDegreesIterator = getIterator(maximalNodeDegrees);
												minusExponent1 = getNext(exponents1Iterator);
											}
											averageNodeDegreesIterator = getIterator(averageNodeDegrees);
											maximalNodeDegree = getNext(maximalNodeDegreesIterator);
										}
										muIterator = getIterator(mu);
										averageNodeDegree = getNext(averageNodeDegreesIterator);
									}
									maximalCommunitySizesIterator = getIterator(maximalCommunitySizes);
									muValue = getNext(muIterator);
								}
								minimalCommunitySizesIterator = getIterator(minimalCommunitySizes);
								maximalCommunitySize = getNext(maximalCommunitySizesIterator);
							}
							generationsIterator = getIterator(launchNumbers);
							minimalCommunitySize = getNext(minimalCommunitySizesIterator);
						}
						omIterator = om.iterator();
						generationsValue = getNext(generationsIterator);
					}
					onShareIterator = on_share.iterator();
					omValue = omIterator.next();
				}
				nIterator = numbersOfNodes.iterator();
				onShareValue = onShareIterator.next();
			}
			numberOfNodes = nIterator.next();

			return true;
		}
	
		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no LFR parameters available for next iteration as requested.");
			}

			ParametersSet parametersSet = new ParametersSetLFR(getNameInScenario(), getId(), directed, weighted,
					makeValueFromRangeInstance(numbersOfNodes, numberOfNodes),
					new ValueFromRange<Integer>(om.getRangeId(), omValue), new ValueFromRange<Float>(on_share.getRangeId(), onShareValue),
					makeValueFromRangeInstance(minimalCommunitySizes, minimalCommunitySize),
					makeValueFromRangeInstance(maximalCommunitySizes, maximalCommunitySize),
					makeValueFromRangeInstance(mu, muValue),
					makeValueFromRangeInstance(averageNodeDegrees, averageNodeDegree), makeValueFromRangeInstance(maximalNodeDegrees, maximalNodeDegree),
					makeValueFromRangeInstance(minusExponents1_degreeSequence, minusExponent1),
					makeValueFromRangeInstance(minusExponents2_commSizesDistribution, minusExponent2),
					makeValueFromRangeInstance(averageClusteringCoefficients, averageClusteringCoefficient),
					referenceCommunitiesRelativeFileName, externalSetsForMiningFiles, externalSetsForCharacterizationFiles, attributesFileName,
					makeValueFromRangeInstance(launchNumbers, generationsValue));
			return parametersSet;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
