package pipeMinedCharacteristic;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;

public class MeasureDescription extends DescriptionMeasure {
	public MeasureDescription() {
		super(MiningJobBase.JobBase.NUMERIC_CHARACTERISTIC);
	}

	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}
}
