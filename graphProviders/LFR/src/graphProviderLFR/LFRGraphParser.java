package graphProviderLFR;

import java.util.LinkedList;

import org.xml.sax.Attributes;

import ru.ispras.modis.NetBlox.exceptions.ScenarioException;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.GraphsDescriptionParser;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLFloatRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.xmlParser.ParserContext;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;
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
	class MuTopologyProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Float> muValues = getValues();
			if (muValues != null  &&  !muValues.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setMuTopology(muValues);
			}
		}
	}
	class MuWeightsProcessor extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Float> muValues = getValues();
			if (muValues != null  &&  !muValues.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setMuWeights(muValues);
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
	class ExponentWeightDistribution extends XMLFloatRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();
			RangeOfValues<Float> exponentValues = getValues();
			if (exponentValues != null  &&  !exponentValues.isEmpty())	{
				((DescriptionGraphsLFR)graphsDescription).setExponentWeightDistribution(exponentValues);
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
	private static final String TAG_MU_TOPOLOGY = "muTopology";
	private static final String TAG_MU_WEIGHTS = "muWeights";

	private static final String TAG_AVERAGE_NODE_DEGREE = "averageNodeDegree";
	private static final String TAG_MAXIMAL_NODE_DEGREE = "maximalNodeDegree";
	private static final String TAG_AVERAGE_CLUSTERING_COEFFICIENT = "averageClusteringCoefficient";

	private static final String TAG_EXPONENT1_DEGREE_SEQUENCE = "minusExponent1_degreeSequence";
	private static final String TAG_EXPONENT2_COMMUNITIES_SIZES_DISTRIBUTION = "minusExponent2_commSizesDistribution";
	private static final String TAG_EXPONENT_WEIGHT_DISTRIBUTION = "exponentWeightDistribution";

	private XMLIntegerRangeStringProcessor numbersOfElementsProcessor;
	private XMLIntegerRangeStringProcessor lfrOmProcessor;
	private XMLFloatRangeStringProcessor lfrOnProcessor;


	public LFRGraphParser()	{
		super();

		addTaggedParser(TAG_MINC, new MinimalCommunitySizesProcessor());
		addTaggedParser(TAG_MAXC, new MaximalCommunitySizesProcessor());

		addTaggedParser(TAG_MU, new MuProcessor());
		addTaggedParser(TAG_MU_TOPOLOGY, new MuTopologyProcessor());
		addTaggedParser(TAG_MU_WEIGHTS, new MuWeightsProcessor());

		addTaggedParser(TAG_AVERAGE_NODE_DEGREE, new AverageNodeDegreeProcessor());
		addTaggedParser(TAG_MAXIMAL_NODE_DEGREE, new MaximalNodeDegreeProcessor());
		addTaggedParser(TAG_AVERAGE_CLUSTERING_COEFFICIENT, new AverageClusteringCoefficientProcessor());

		addTaggedParser(TAG_EXPONENT1_DEGREE_SEQUENCE, new Exponent1DegreeSequenceProcessor());
		addTaggedParser(TAG_EXPONENT2_COMMUNITIES_SIZES_DISTRIBUTION, new Exponent2CommunitiesSizesDistributionProcessor());
		addTaggedParser(TAG_EXPONENT_WEIGHT_DISTRIBUTION, new ExponentWeightDistribution());
	}


	@Override
	public void createElement(XMLElementProcessor aparent, String tagName,
			Attributes attributes, ParserContext acontext) {
		super.createElement(aparent, tagName, attributes, acontext);

		addTaggedParser(TAG_N, numbersOfElementsProcessor = new XMLIntegerRangeStringProcessor());
		addTaggedParser(TAG_OM, lfrOmProcessor = new XMLIntegerRangeStringProcessor());
		addTaggedParser(TAG_ON, lfrOnProcessor = new XMLFloatRangeStringProcessor());
	}


	@Override
	public void closeElement()	{
		super.closeElement();

		DescriptionGraphsLFR graphsDescription = (DescriptionGraphsLFR)this.graphsDescription;

		RangeOfValues<Integer> numbersOfElements = numbersOfElementsProcessor.getValues();
		if (numbersOfElements.isEmpty())	{
			throw new ScenarioException("Missing size of graph in LFR description.");
		}
		graphsDescription.setNumberOfNodes(numbersOfElements);

		RangeOfValues<Integer> omValues = lfrOmProcessor.getValues();
		if (!omValues.isEmpty())	{
			graphsDescription.setOmValues(omValues);
		}

		RangeOfValues<Float> onShareValues = lfrOnProcessor.getValues();
		if (!onShareValues.isEmpty())	{
			graphsDescription.setOnShareValues(onShareValues);
		}
	}


	@Override
	protected DescriptionGraphsOneType createGraphsDescription()	{
		return new DescriptionGraphsLFR();
	}
}
