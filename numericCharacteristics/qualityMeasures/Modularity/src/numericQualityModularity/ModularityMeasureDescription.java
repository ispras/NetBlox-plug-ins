package numericQualityModularity;


import java.util.Iterator;
import java.util.NoSuchElementException;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;
import ru.ispras.modis.NetBlox.scenario.ParametersSet;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;

public class ModularityMeasureDescription extends DescriptionMeasure {
	
	private RangeOfValues<String> formulaType = null;
	private RangeOfValues<String> belongingFunction = null;
	private RangeOfValues<String> belongingCoefficient= null;

	public ModularityMeasureDescription() {
		super(MiningJobBase.JobBase.NODES_GROUPS_SET);
	}

	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}

	public void setFormulaType(RangeOfValues<String> formulaType) {
		this.formulaType = formulaType;
	}

	public void setBelongingFunction(RangeOfValues<String> belongingFunction) {
		this.belongingFunction = belongingFunction;
	}

	public void setBelongingCoefficient(RangeOfValues<String> belongingCoefficient) {
		this.belongingCoefficient = belongingCoefficient;
	}


	@Override
	public Iterator<ParametersSet> iterator() {
		return new ModularityParametersIterator();
	}

	
	private class ModularityParametersIterator extends AlgorithmParametersIterator	{
		
		private Iterator<String> formulaTypeIterator = getIterator(formulaType);
		private Iterator<String> belongingFunctionIterator = getIterator(belongingFunction);
		private Iterator<String> belongingCoefficientIterator = getIterator(belongingCoefficient);

		private String formulaTypeValue = null;
		private String belongingFunctionValue = null;
		private String belongingCoefficientValue = null;


		public ModularityParametersIterator()	{			
			//XXX The order matters here and in resolveValues(). That's no good.
			belongingCoefficientValue = initiateValue(belongingCoefficientIterator);
			belongingFunctionValue = initiateValue(belongingFunctionIterator);
			formulaTypeValue = initiateValue(formulaTypeIterator);

			if (!hasNext())	{ //In case ALL ranges are absent, we know at the moment of initialization we won't be able to perform a single iteration.
				hasSingleIteration = true;
			}
		}


		@Override
		protected boolean resolveValues()	{
			
			if (!hasNext(belongingCoefficientIterator))	{
				if (!hasNext(belongingFunctionIterator))	{
					if (!hasNext(formulaTypeIterator))	{
						if (!super.resolveValues())	{
							return false;
						}
						formulaTypeIterator = getIterator(formulaType);
					}
					formulaTypeValue = getNext(formulaTypeIterator);
					belongingFunctionIterator = getIterator(belongingFunction);
				}
				belongingFunctionValue = getNext(belongingFunctionIterator);
				belongingCoefficientIterator = getIterator(belongingCoefficient);
			}
			belongingCoefficientValue = getNext(belongingCoefficientIterator);

			return true;
		}

		@Override
		public ParametersSet next() {
			if (!resolveValues())	{
				throw new NoSuchElementException("There're no Modularity parameters available for next iteration as requested.");
			}

			ParametersSetModularity parametersSet = new ParametersSetModularity(getNameInScenario(), getId(), jobBase,
					makeValueFromRangeInstance(formulaType, formulaTypeValue),
					makeValueFromRangeInstance(belongingFunction, belongingFunctionValue),
					makeValueFromRangeInstance(belongingCoefficient, belongingCoefficientValue));

			return parametersSet;
		}


		@Override
		public boolean hasNext() {
			return hasNext(formulaTypeIterator) || hasNext(belongingFunctionIterator) ||
					hasNext(belongingCoefficientIterator) || super.hasNext();
		}
	}

}
