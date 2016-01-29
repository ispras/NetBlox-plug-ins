package graphProviderLFR;

import java.util.LinkedList;
import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.Pair;


public class ParametersSetLFR extends GraphParametersSet {
	private ValueFromRange<Integer> om;			//Number of communities per node in an intersection.
	private ValueFromRange<Float> on_share;		//Share of nodes that participate in intersections.
	private ValueFromRange<Integer> minimalCommunitySize;
	private ValueFromRange<Integer> maximalCommunitySize;
	private ValueFromRange<Float> mu;	//Mixing coefficient.
	private ValueFromRange<Integer> averageNodeDegree;
	private ValueFromRange<Integer> maximalNodeDegree;
	private ValueFromRange<Float> t1_minusExpDegreeSequence;
	private ValueFromRange<Float> t2_minusExpCommunitiesSizesDistribution;
	private ValueFromRange<Float> averageClusteringCoefficient;


	public ParametersSetLFR(String graphTypeName, String graphDescriptionID, boolean directed, boolean weighted,
			ValueFromRange<Integer> numberOfNodes, ValueFromRange<Integer> om, ValueFromRange<Float> on_share,
			ValueFromRange<Integer> minimalCommunitySize, ValueFromRange<Integer> maximalCommunitySize, ValueFromRange<Float> mu,
			ValueFromRange<Integer> averageNodeDegree, ValueFromRange<Integer> maximalNodeDegree,
			ValueFromRange<Float> t1_minusExpDegreeSequence, ValueFromRange<Float> t2_minusExpCommunitiesSizesDistribution,
			ValueFromRange<Float> averageClusteringCoefficient,
			String referenceCommunitiesRelativeFileName,
			RangeOfValues<String> externalSetsForMiningFilenames, RangeOfValues<String> externalSetsForMeasuresFilenames,
			String attributesFileName, ValueFromRange<Integer> generation)	{
		super(graphTypeName, graphDescriptionID, directed, weighted, numberOfNodes,
				referenceCommunitiesRelativeFileName, externalSetsForMiningFilenames, externalSetsForMeasuresFilenames, attributesFileName, generation);

		this.om = om;
		this.on_share = on_share;
		this.minimalCommunitySize = minimalCommunitySize;
		this.maximalCommunitySize = maximalCommunitySize;
		this.mu = mu;
		this.averageNodeDegree = averageNodeDegree;
		this.maximalNodeDegree = maximalNodeDegree;
		this.t1_minusExpDegreeSequence = t1_minusExpDegreeSequence;
		this.t2_minusExpCommunitiesSizesDistribution = t2_minusExpCommunitiesSizesDistribution;
		this.averageClusteringCoefficient = averageClusteringCoefficient;
	}


	/**
	 * Get Om - number of communities per node in an intersection.
	 */
	public Integer getOm()	{
		return om.getValue();
	}

	/**
	 * Get On share - share of nodes that participate in intersections.
	 */
	public Float getOnShare()	{
		return on_share.getValue();
	}

	public Integer getMinimalCommunitySize()	{
		return (minimalCommunitySize==null) ? null : minimalCommunitySize.getValue();
	}

	public Integer getMaximalCommunitySize()	{
		return (maximalCommunitySize==null) ? null : maximalCommunitySize.getValue();
	}

	public Float getMu()	{
		return (mu==null) ? null : mu.getValue();
	}

	public Integer getAverageNodeDegree()	{
		return (averageNodeDegree==null) ? null : averageNodeDegree.getValue();
	}

	public Integer getMaximalNodeDegree()	{
		return (maximalNodeDegree==null) ? null : maximalNodeDegree.getValue();
	}

	public Float get_t1()	{
		return (t1_minusExpDegreeSequence==null) ? null : t1_minusExpDegreeSequence.getValue();
	}

	public Float get_t2()	{
		return (t2_minusExpCommunitiesSizesDistribution==null) ? null : t2_minusExpCommunitiesSizesDistribution.getValue();
	}

	public Float getAverageClusteringCoefficient()	{
		return (averageClusteringCoefficient==null) ? null : averageClusteringCoefficient.getValue();
	}


	@Override
	public boolean hasParametersFromSomeRange() {
		return (!om.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||  (!on_share.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				(minimalCommunitySize != null)  &&  (!minimalCommunitySize.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				(maximalCommunitySize != null)  &&  (!maximalCommunitySize.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				(mu != null)  &&  (!mu.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				(averageNodeDegree != null)  &&  (!averageNodeDegree.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				(maximalNodeDegree != null)  &&  (!maximalNodeDegree.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				(t1_minusExpDegreeSequence != null)  &&  (!t1_minusExpDegreeSequence.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				(t2_minusExpCommunitiesSizesDistribution != null)  &&
					(!t2_minusExpCommunitiesSizesDistribution.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				(averageClusteringCoefficient != null)  &&  (!averageClusteringCoefficient.getRangeId().equals(RangeOfValues.NO_RANGE_ID))  ||
				super.hasParametersFromSomeRange();
	}

	@Override
	public Object getValueForVariationId(String id) {
		Object result = null;
		if (id.equals(om.getRangeId()))	{
			result = om.getValue();
		}
		else if (id.equals(on_share.getRangeId()))	{
			result = on_share.getValue();
		}
		else if ((minimalCommunitySize != null)  &&  id.equals(minimalCommunitySize.getRangeId()))	{
			result = minimalCommunitySize.getValue();
		}
		else if ((maximalCommunitySize != null)  &&  id.equals(maximalCommunitySize.getRangeId()))	{
			result = maximalCommunitySize.getValue();
		}
		else if ((mu != null)  &&  id.equals(mu.getRangeId()))	{
			result = mu.getValue();
		}
		else if ((averageNodeDegree != null)  &&  id.equals(averageNodeDegree.getRangeId()))	{
			result = averageNodeDegree.getValue();
		}
		else if ((maximalNodeDegree != null)  &&  id.equals(maximalNodeDegree.getRangeId()))	{
			result = maximalNodeDegree.getValue();
		}
		else if ((t1_minusExpDegreeSequence != null)  &&  id.equals(t1_minusExpDegreeSequence.getRangeId()))	{
			result = t1_minusExpDegreeSequence.getValue();
		}
		else if ((t2_minusExpCommunitiesSizesDistribution != null)  &&  id.equals(t2_minusExpCommunitiesSizesDistribution.getRangeId()))	{
			result = t2_minusExpCommunitiesSizesDistribution.getValue();
		}
		else if ((averageClusteringCoefficient != null)  &&  id.equals(averageClusteringCoefficient.getRangeId()))	{
			result = averageClusteringCoefficient.getValue();
		}
		else	{
			result = super.getValueForVariationId(id);
		}

		return result;
	}


	@Override
	public String toString()	{
		StringBuilder builder = new StringBuilder("[LFR: [").
				append(isDirected()?"directed":"undirected").append("] [").
				append(isWeighted()?"weighted":"unweighted").
				append("] [n=").append(getNumberOfNodes()).
				append("] [Om=").append(getOm()).
				append("] [On_share=").append(getOnShare()).
				append("] [generation=").append(getGenerationNumber());

		if (minimalCommunitySize != null)	{
			builder.append("] [minimal community size=").append(getMinimalCommunitySize());
		}
		if (maximalCommunitySize != null)	{
			builder.append("] [maximal community size=").append(getMaximalCommunitySize());
		}
		if (mu != null)	{
			builder.append("] [mu=").append(getMu());
		}
		if (averageNodeDegree != null)	{
			builder.append("] [average node degree=").append(getAverageNodeDegree());
		}
		if (maximalNodeDegree != null)	{
			builder.append("] [maximal node degree=").append(getMaximalNodeDegree());
		}
		if (t1_minusExpDegreeSequence != null)	{
			builder.append("] [minus exponent1 for degrees sequence=").append(get_t1());
		}
		if (t2_minusExpCommunitiesSizesDistribution != null)	{
			builder.append("] [minus exponent2 for communities sizes distribution=").append(get_t2());
		}
		if (averageClusteringCoefficient != null)	{
			builder.append("] [average clustering coefficient=").append(getAverageClusteringCoefficient());
		}

		builder.append("]]");
		return builder.toString();
	}


	@Override
	public List<List<Pair<String, String>>> getSpecifiedParametersAsGroupsOfPairsOfUniqueKeysAndValues()	{
		List<List<Pair<String, String>>> result = null;

		List<Pair<String, String>> sublist = getCommonGraphParametersInList();
		sublist = appendNonNullParameter(sublist, minimalCommunitySize, "mc");	//TODO Reform to reuse when launching external application?
		sublist = appendNonNullParameter(sublist, maximalCommunitySize, "Mc");
		sublist = appendNonNullParameter(sublist, mu, "mu");
		sublist = appendNonNullParameter(sublist, averageNodeDegree, "k");
		sublist = appendNonNullParameter(sublist, maximalNodeDegree, "maxk");
		sublist = appendNonNullParameter(sublist, t1_minusExpDegreeSequence, "t1");
		sublist = appendNonNullParameter(sublist, t2_minusExpCommunitiesSizesDistribution, "t2");
		sublist = appendNonNullParameter(sublist, averageClusteringCoefficient, "C");
		result = appendNonNullSublist(result, sublist);

		sublist = appendNonNullParameter(null, om, "commPerNode");	//TODO Reform to reuse when launching external application?
		result = appendNonNullSublist(result, sublist);

		sublist = appendNonNullParameter(null, on_share, "overlapFrac");	//TODO Reform to reuse when launching external application?
		result = appendNonNullSublist(result, sublist);

		return result;
	}



	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> result = null;

		List<List<Pair<String, String>>> groupedList = getSpecifiedParametersAsGroupsOfPairsOfUniqueKeysAndValues();
		if (groupedList != null)	{
			result = new LinkedList<Pair<String, String>>();
			for (List<Pair<String, String>> sublist : groupedList)	{
				result.addAll(sublist);
			}
		}

		return result;
	}
}
