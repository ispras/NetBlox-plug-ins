package graphProviderLFR;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphsOneType;
import ru.ispras.modis.NetBlox.scenario.GraphParametersSet;
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
	private RangeOfValues<Float> muTopology = null;	//Mixing coefficient for the topology (case of weighted LFR).
	private RangeOfValues<Float> muWeights = null;	//Mixing coefficient for the weight distribution (case of weighted LFR).

	private RangeOfValues<Integer> averageNodeDegrees = null;
	private RangeOfValues<Integer> maximalNodeDegrees = null;
	private RangeOfValues<Float> averageClusteringCoefficients = null;

	private RangeOfValues<Float> minusExponents1_degreeSequence = null;
	private RangeOfValues<Float> minusExponents2_commSizesDistribution = null;
	private RangeOfValues<Float> exponentWeightDistribution = null;


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
	public void setMuTopology(RangeOfValues<Float> values)	{
		muTopology = values;
	}
	public void setMuWeights(RangeOfValues<Float> values)	{
		muWeights = values;
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
	public void setExponentWeightDistribution(RangeOfValues<Float> values)	{
		exponentWeightDistribution = values;
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
		addNonNullVariation(muTopology, variations);
		addNonNullVariation(muWeights, variations);

		addNonNullVariation(averageNodeDegrees, variations);
		addNonNullVariation(maximalNodeDegrees, variations);
		addNonNullVariation(averageClusteringCoefficients, variations);

		addNonNullVariation(minusExponents1_degreeSequence, variations);
		addNonNullVariation(minusExponents2_commSizesDistribution, variations);
		addNonNullVariation(exponentWeightDistribution, variations);

		return variations;
	}

	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		if (weighted)	{
			if (mu != null)	{
				System.out.println("WARNING:\t"+mu.getRangeTag()+" is for unweighted graphs. It will be ignored.");
				mu = null;
			}
		}
		else	{
			if (muTopology != null)	{
				System.out.println("WARNING:\t"+muTopology.getRangeTag()+" is for weighted graphs. It will be ignored.");
				muTopology = null;
			}
			if (muWeights != null)	{
				System.out.println("WARNING:\t"+muWeights.getRangeTag()+" is for weighted graphs. It will be ignored.");
				muWeights = null;
			}
			if (exponentWeightDistribution != null)	{
				System.out.println("WARNING:\t"+exponentWeightDistribution.getRangeTag()+" is for weighted graphs. It will be ignored.");
				exponentWeightDistribution = null;
			}
		}

		if (directed  &&  averageClusteringCoefficients != null)	{
			System.out.println("WARNING:\t"+averageClusteringCoefficients.getRangeTag()+" is for undirected graphs. It will be ignored.");
			averageClusteringCoefficients = null;
		}

		return new GraphIterator();
	}


	/**
	 * An iterator over the combinations of LFR parameters.
	 * @author ilya
	 */
	private class GraphIterator extends IteratorForParametersProbablyMissing	{
		private Iterator<Integer> nIterator = numbersOfNodes.iterator();
		private Iterator<Integer> omIterator = getIterator(om);
		private Iterator<Float> onShareIterator = getIterator(on_share);
		private Iterator<Integer> generationsIterator = getIterator(launchNumbers);

		private Iterator<Integer> minimalCommunitySizesIterator = getIterator(minimalCommunitySizes);
		private Iterator<Integer> maximalCommunitySizesIterator = getIterator(maximalCommunitySizes);
		private Iterator<Integer> averageNodeDegreesIterator = getIterator(averageNodeDegrees);
		private Iterator<Integer> maximalNodeDegreesIterator = getIterator(maximalNodeDegrees);
		private Iterator<Float> averageClusteringCoefficientIterator = getIterator(averageClusteringCoefficients);

		private Iterator<Float> muIterator = getIterator(mu);
		private Iterator<Float> muTopologyIterator = getIterator(muTopology);
		private Iterator<Float> muWeightsIterator = getIterator(muWeights);
		private Iterator<Float> exponents1Iterator = getIterator(minusExponents1_degreeSequence);
		private Iterator<Float> exponents2Iterator = getIterator(minusExponents2_commSizesDistribution);
		private Iterator<Float> exponentWeightDistributionIterator = getIterator(exponentWeightDistribution);

		private Integer numberOfNodes;
		private Float onShareValue = getNext(onShareIterator);
		private Integer omValue = getNext(omIterator);
		private Integer generationsValue = getNext(generationsIterator);

		private Integer minimalCommunitySize = getNext(minimalCommunitySizesIterator);
		private Integer maximalCommunitySize = getNext(maximalCommunitySizesIterator);
		private Float muValue = getNext(muIterator);
		private Float muTopologyValue = getNext(muTopologyIterator);
		private Float muWeightsValue = getNext(muWeightsIterator);
		private Integer averageNodeDegree = getNext(averageNodeDegreesIterator);
		private Integer maximalNodeDegree = getNext(maximalNodeDegreesIterator);
		private Float minusExponent1 = getNext(exponents1Iterator);
		private Float minusExponent2 = getNext(exponents2Iterator);
		private Float exponentWeightDistributionValue = getNext(exponentWeightDistributionIterator);
		private Float averageClusteringCoefficient = getNext(averageClusteringCoefficientIterator);

		@Override
		public boolean hasNext() {
			return nIterator.hasNext() || hasNext(omIterator) || hasNext(onShareIterator) || hasNext(generationsIterator) ||
					hasNext(minimalCommunitySizesIterator) || hasNext(maximalCommunitySizesIterator) ||
					hasNext(muIterator) || hasNext(muTopologyIterator) || hasNext(muWeightsIterator) ||
					hasNext(averageNodeDegreesIterator) || hasNext(maximalNodeDegreesIterator) ||
					hasNext(exponents1Iterator) || hasNext(exponents2Iterator) || hasNext(exponentWeightDistributionIterator) ||
					hasNext(averageClusteringCoefficientIterator);
		}

		protected boolean resolveValues()	{
			//XXX Rewrite this via a recursive function.
			if (!nIterator.hasNext())	{
				if (!hasNext(onShareIterator))	{
					if (!hasNext(omIterator))	{
						if (!hasNext(generationsIterator))	{
							if (!hasNext(minimalCommunitySizesIterator))	{
								if (!hasNext(maximalCommunitySizesIterator))	{
									if (!hasNext(muIterator))	{
										if (!hasNext(muTopologyIterator))	{
											if (!hasNext(muWeightsIterator))	{
												if (!hasNext(averageNodeDegreesIterator))	{
													if (!hasNext(maximalNodeDegreesIterator))	{
														if (!hasNext(exponents1Iterator))	{
															if (!hasNext(exponents2Iterator))	{
																if (!hasNext(exponentWeightDistributionIterator))	{
																	if (!hasNext(averageClusteringCoefficientIterator))	{
																		return false;
																	}
																	exponentWeightDistributionIterator = getIterator(exponentWeightDistribution);
																	averageClusteringCoefficient = getNext(averageClusteringCoefficientIterator);
																}
																exponents2Iterator = getIterator(minusExponents2_commSizesDistribution);
																exponentWeightDistributionValue = getNext(exponentWeightDistributionIterator);
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
												muWeightsIterator = getIterator(muWeights);
												averageNodeDegree = getNext(averageNodeDegreesIterator);
											}
											muTopologyIterator = getIterator(muTopology);
											muWeightsValue = getNext(muWeightsIterator);
										}
										muIterator = getIterator(mu);
										muTopologyValue = getNext(muTopologyIterator);
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
						omIterator = getIterator(om);
						generationsValue = getNext(generationsIterator);
					}
					onShareIterator = getIterator(on_share);
					omValue = getNext(omIterator);
				}
				nIterator = numbersOfNodes.iterator();
				onShareValue = getNext(onShareIterator);
			}
			numberOfNodes = nIterator.next();

			return true;
		}
	
		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no LFR parameters available for next iteration as requested.");
			}

			GraphParametersSet parametersSet = new ParametersSetLFR(getNameInScenario(), getId(), directed, weighted,
					makeValueFromRangeInstance(numbersOfNodes, numberOfNodes),
					makeValueFromRangeInstance(om, omValue), makeValueFromRangeInstance(on_share, onShareValue),
					makeValueFromRangeInstance(minimalCommunitySizes, minimalCommunitySize),
					makeValueFromRangeInstance(maximalCommunitySizes, maximalCommunitySize),
					makeValueFromRangeInstance(mu, muValue), makeValueFromRangeInstance(muTopology, muTopologyValue),
					makeValueFromRangeInstance(muWeights, muWeightsValue),
					makeValueFromRangeInstance(averageNodeDegrees, averageNodeDegree), makeValueFromRangeInstance(maximalNodeDegrees, maximalNodeDegree),
					makeValueFromRangeInstance(minusExponents1_degreeSequence, minusExponent1),
					makeValueFromRangeInstance(minusExponents2_commSizesDistribution, minusExponent2),
					makeValueFromRangeInstance(exponentWeightDistribution, exponentWeightDistributionValue),
					makeValueFromRangeInstance(averageClusteringCoefficients, averageClusteringCoefficient),
					makeValueFromRangeInstance(launchNumbers, generationsValue));
			setSpecifiedFilenames(parametersSet);
			return parametersSet;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
