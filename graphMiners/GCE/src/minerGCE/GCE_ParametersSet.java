package minerGCE;

import java.util.ArrayList;
import java.util.List;

import ru.ispras.modis.NetBlox.scenario.GraphMiningParametersSet;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.utils.Pair;

public class GCE_ParametersSet extends GraphMiningParametersSet {
	private int minimalCliqueSize = 4;

	private float minimalValueForOneSeedToOverlapWithAnotherSeed = (float) 0.6;
	private float alphaValue = (float) 1.0;
	private float phi = (float) 0.75;


	public GCE_ParametersSet(String algorithmNameInScenario, String algorithmDescriptionID, int minimalCliqueSize) {
		super(MiningJobBase.JobBase.GRAPH, algorithmNameInScenario, algorithmDescriptionID);
		this.minimalCliqueSize = minimalCliqueSize;
	}


	public int getMinimalCliqueSize()	{
		return minimalCliqueSize;
	}

	public float getMinimalValueForOneSeedToOverlapWithAnotherSeed()	{
		return minimalValueForOneSeedToOverlapWithAnotherSeed;
	}

	public float getAlphaValueForFitnessFunctionGreedilyExpandingCliques()	{
		return alphaValue;
	}

	public float getProportionOfNodesWithinCoreCliqueForSufficientCoverage()	{
		return phi;
	}


	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		List<Pair<String, String>> result = new ArrayList<Pair<String, String>>(1);
		result.add(new Pair<String, String>("cl", String.valueOf(minimalCliqueSize)));	//XXX Change to reuse for generation?..
		return result;
	}
}
