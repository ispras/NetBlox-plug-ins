package minerDijkstra;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

public class DijkstraMiningDescription extends DescriptionGraphMiningAlgorithm {
	private RangeOfValues<Integer> sourceNodesIDs = null;
	private RangeOfValues<Integer> targetNodesIDs = null;
	private boolean findForAll = false;
	private boolean findSingle = true;

	public void setSourceNodes(RangeOfValues<Integer> sourceNodesIDs)	{
		this.sourceNodesIDs = sourceNodesIDs;
	}
	public void setTargetNodes(RangeOfValues<Integer> targetNodesIDs)	{
		this.targetNodesIDs = targetNodesIDs;
	}
	public void setFindForAll(boolean b)	{
		findForAll = b;
	}
	public void setFindSingle(boolean b)	{
		findSingle = b;
	}

	public boolean findForAll()	{
		return findForAll;
	}
	public boolean isSourceNodeSpecified()	{
		return sourceNodesIDs != null  &&  !sourceNodesIDs.isEmpty();
	}
	public boolean isTargetNodeSpecified()	{
		return targetNodesIDs != null  &&  !targetNodesIDs.isEmpty();
	}

	@Override
	public Collection<RangeOfValues<?>> getAllVariations()	{
		Collection<RangeOfValues<?>> variations = super.getAllVariations();

		addNonNullVariation(sourceNodesIDs, variations);
		addNonNullVariation(targetNodesIDs, variations);

		return variations;
	}

	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}

	@Override
	public Iterator<ParametersSet> iterator() {
		return new DijkstraParametersIterator();
	}


	private class DijkstraParametersIterator extends AlgorithmParametersIterator	{
		private Iterator<Integer> sourceIDsIterator = getIterator(sourceNodesIDs);
		private Iterator<Integer> targetIDsIterator = getIterator(targetNodesIDs);

		private Integer sourceIDValue = null;
		private Integer targetIDValue = null;


		public DijkstraParametersIterator()	{
			sourceIDValue = initiateValue(sourceIDsIterator);	//XXX The order matters here and in resolveValues(). That's no good.
			targetIDValue = initiateValue(targetIDsIterator);

			launchNumber = initiateValue(launchesIterator);

			if (!hasNext())	{ //In case ALL ranges are absent, we know at the moment of initialisation we won't be able to perform a single iteration.
				hasSingleIteration = true;
			}
		}


		@Override
		public boolean hasNext() {
			return hasNext(sourceIDsIterator) || hasNext(targetIDsIterator) || super.hasNext();
		}


		@Override
		protected boolean resolveValues()	{
			if (!hasNext(sourceIDsIterator))	{
				if (!hasNext(targetIDsIterator))	{
					if (!super.resolveValues())	{
						return false;
					}
					targetIDsIterator = getIterator(targetNodesIDs);
				}
				sourceIDsIterator = getIterator(sourceNodesIDs);
				targetIDValue = getNext(targetIDsIterator);
			}
			sourceIDValue = getNext(sourceIDsIterator);

			return true;
		}

		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no Dijkstra's shortest path algorithm parameters available for next iteration as requested.");
			}

			DijkstraMinerParametersSet parameters = new DijkstraMinerParametersSet(getNameInScenario(), getId(),
					makeValueFromRangeInstance(sourceNodesIDs, sourceIDValue), makeValueFromRangeInstance(targetNodesIDs, targetIDValue), findSingle);
			return parameters;
		}
	}
}
