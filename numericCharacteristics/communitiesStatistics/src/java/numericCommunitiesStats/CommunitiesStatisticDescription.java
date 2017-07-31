package numericCommunitiesStats;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;


public class CommunitiesStatisticDescription extends DescriptionMeasure {

	private RangeOfValues<Float> minWeight = null;
	private RangeOfValues<String> connectednessType = null;
	private RangeOfValues<String> trianglesType= null;
	private RangeOfValues<String> icdfType = null;
	private boolean bidirectional = false;
	
	public CommunitiesStatisticDescription() {
		super(MiningJobBase.JobBase.NODES_GROUPS_SET);
	}

	@Override
	protected Bundle getImplementingPluginBundle() {
		return Activator.getContext().getBundle();
	}

	public void setMinWeight(RangeOfValues<Float> minWeight) {
		this.minWeight = minWeight;
	}

	public void setConnectednessType(RangeOfValues<String> connectednessType) {
		this.connectednessType= connectednessType;
	}

	public void setTrianglesType(RangeOfValues<String> trianglesType) {
		this.trianglesType= trianglesType;
	}

	public void setIcdfType(RangeOfValues<String> icdfType) {
		this.icdfType= icdfType;
	}
	
	public void setBidirectional(Boolean b) {
		bidirectional= b;
	}

	@Override
	public Iterator<ParametersSet> iterator() {
		return new CommunitiesStatisticParametersIterator();
	}

	
	private class CommunitiesStatisticParametersIterator extends AlgorithmParametersIterator	{
		
		private Iterator<Float> minWeightIterator = getIterator(minWeight);
		private Iterator<String> connectednessTypeIterator = getIterator(connectednessType);
		private Iterator<String> trianglesTypeIterator = getIterator(trianglesType);
		private Iterator<String> icdfTypeIterator = getIterator(icdfType);

		private Float minWeightValue = null;
		private String connectednessTypeValue = null;
		private String trianglesTypeValue = null;
		private String icdfTypeValue = null;
		
		public CommunitiesStatisticParametersIterator()	{			
			//XXX The order matters here and in resolveValues(). That's no good.
			icdfTypeValue = initiateValue(icdfTypeIterator);
			trianglesTypeValue = initiateValue(trianglesTypeIterator);
			connectednessTypeValue = initiateValue(connectednessTypeIterator);
			minWeightValue = initiateValue(minWeightIterator);
			
			if (!hasNext())	{ //In case ALL ranges are absent, we know at the moment of initialization we won't be able to perform a single iteration.
				hasSingleIteration = true;
			}
		}

		@Override
		protected boolean resolveValues()	{
			if (!hasNext(icdfTypeIterator))	{
				if (!hasNext(trianglesTypeIterator))	{
					if (!hasNext(connectednessTypeIterator))	{
						if (!hasNext(minWeightIterator))	{
							if (!super.resolveValues())	{
								return false;
							}
							minWeightIterator = getIterator(minWeight);
						}
						minWeightValue = getNext(minWeightIterator);
						connectednessTypeIterator = getIterator(connectednessType);
					}
					connectednessTypeValue = getNext(connectednessTypeIterator);
					trianglesTypeIterator = getIterator(trianglesType);
				}
				trianglesTypeValue = getNext(trianglesTypeIterator);
				icdfTypeIterator = getIterator(icdfType);
			}
			icdfTypeValue = getNext(icdfTypeIterator);

			return true;
		}

		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no community stats parameters available for next iteration as requested.");
			}
			
			ParametersSetStats parametersSet = new ParametersSetStats(getNameInScenario(), getId(), jobBase,
					makeValueFromRangeInstance(minWeight, minWeightValue),
					makeValueFromRangeInstance(connectednessType, connectednessTypeValue),
					makeValueFromRangeInstance(trianglesType, trianglesTypeValue),
					makeValueFromRangeInstance(icdfType, icdfTypeValue),
					bidirectional);

			return parametersSet;
		}


		@Override
		public boolean hasNext() {
			return hasNext(minWeightIterator) || hasNext(connectednessTypeIterator) ||
					hasNext(trianglesTypeIterator) || hasNext(icdfTypeIterator) ||  super.hasNext();
		}
	}
	
}
