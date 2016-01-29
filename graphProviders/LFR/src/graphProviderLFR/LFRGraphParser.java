package graphProviderLFR;

import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.GraphsDescriptionParser;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLFloatRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphsOneType;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

/**
 * Parses the description of LFR graphs in scenario file.
 * 
 * @author Ilya
 */
class LFRGraphParser extends GraphsDescriptionParser	{
	class MinimalCommunitySizesProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> minimalCommunitySizes = getValues();
			if (minimalCommunitySizes != null  &&  !minimalCommunitySizes.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setMinimalCommunitySize(minimalCommunitySizes);
			}
		}
	}

	class MaximalCommunitySizesProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> maximalCommunitySizes = getValues();
			if (maximalCommunitySizes != null  &&  !maximalCommunitySizes.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setMaximalCommunitySize(maximalCommunitySizes);
			}
		}
	}

	class MuProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Float> muValues = getValues();
			if (muValues != null  &&  !muValues.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setMu(muValues);
			}
		}
	}

	class AverageNodeDegreeProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Integer> averageNodeDegrees = getValues();
			if (averageNodeDegrees != null  &&  !averageNodeDegrees.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setAverageNodeDegree(averageNodeDegrees);
			}
		}
	}

	class MaximalNodeDegreeProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Integer> maximalNodeDegrees = getValues();
			if (maximalNodeDegrees != null  &&  !maximalNodeDegrees.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setMaximalNodeDegree(maximalNodeDegrees);
			}
		}
	}

	class Exponent1DegreeSequenceProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Float> exponentValues = getValues(2, 3);
			if (exponentValues != null  &&  !exponentValues.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setExponent1(exponentValues);
			}
		}
	}

	class Exponent2CommunitiesSizesDistributionProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Float> exponentValues = getValues(1, 2);
			if (exponentValues != null  &&  !exponentValues.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setExponent2(exponentValues);
			}
		}
	}

	class AverageClusteringCoefficientProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Float> coefficientValues = getValues(0, 1);
			if (coefficientValues != null  &&  !coefficientValues.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setAverageClusteringCoefficients(coefficientValues);
			}
		}
	}


	private static final String TAG_N = "n";
	private static final String TAG_OM = "Om";
	private static final String TAG_ON = "OnShare";
	private static final String TAG_MINC = "minc";
	private static final String TAG_MAXC = "maxc";
	private static final String TAG_MU = "mu";
	private static final String TAG_AVERAGE_NODE_DEGREE = "averageNodeDegree";
	private static final String TAG_MAXIMAL_NODE_DEGREE = "maximalNodeDegree";
	private static final String TAG_EXPONENT1_DEGREE_SEQUENCE = "minusExponent1_degreeSequence";
	private static final String TAG_EXPONENT2_COMMUNITIES_SIZES_DISTRIBUTION = "minusExponent2_commSizesDistribution";
	private static final String TAG_AVERAGE_CLUSTERING_COEFFICIENT = "averageClusteringCoefficient";
	//TODO muTopology, muWeights, exponentWeightDistribution

	private final XMLIntegerRangeStringProcessor numbersOfElementsProcessor;
	private final XMLIntegerRangeStringProcessor lfrOmProcessor;
	private final XMLFloatRangeStringProcessor lfrOnProcessor;


	public LFRGraphParser()	{
		super();

		addTaggedParser(TAG_N, numbersOfElementsProcessor = new XMLIntegerRangeStringProcessor());
		addTaggedParser(TAG_OM, lfrOmProcessor = new XMLIntegerRangeStringProcessor());
		addTaggedParser(TAG_ON, lfrOnProcessor = new XMLFloatRangeStringProcessor());
		addTaggedParser(TAG_MINC, new MinimalCommunitySizesProcessor());
		addTaggedParser(TAG_MAXC, new MaximalCommunitySizesProcessor());
		addTaggedParser(TAG_MU, new MuProcessor());
		addTaggedParser(TAG_AVERAGE_NODE_DEGREE, new AverageNodeDegreeProcessor());
		addTaggedParser(TAG_MAXIMAL_NODE_DEGREE, new MaximalNodeDegreeProcessor());
		addTaggedParser(TAG_EXPONENT1_DEGREE_SEQUENCE, new Exponent1DegreeSequenceProcessor());
		addTaggedParser(TAG_EXPONENT2_COMMUNITIES_SIZES_DISTRIBUTION, new Exponent2CommunitiesSizesDistributionProcessor());
		addTaggedParser(TAG_AVERAGE_CLUSTERING_COEFFICIENT, new AverageClusteringCoefficientProcessor());
	}


	@Override
	public void closeElement()	{
		super.closeElement();

		DescriptionGraphsLFR graphsDescription = (DescriptionGraphsLFR)this.graphsDescription;

		RangeOfValues<Integer> numbersOfElements = numbersOfElementsProcessor.getValues();
		graphsDescription.setNumberOfNodes(numbersOfElements);

		RangeOfValues<Integer> omValues = lfrOmProcessor.getValues();
		graphsDescription.setOmValues(omValues);

		RangeOfValues<Float> onShareValues = lfrOnProcessor.getValues();
		graphsDescription.setOnShareValues(onShareValues);
	}


	@Override
	protected DescriptionGraphsOneType createGraphsDescription()	{
		return new DescriptionGraphsLFR();
	}
}
