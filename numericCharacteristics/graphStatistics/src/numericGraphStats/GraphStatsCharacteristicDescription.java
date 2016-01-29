package numericGraphStats;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;

public class GraphStatsCharacteristicDescription extends DescriptionMeasure {

	public GraphStatsCharacteristicDescription() {
		super(MiningJobBase.JobBase.GRAPH);
	}

	@Override
	protected Bundle getImplementingPluginBundle()	{
		return Activator.getContext().getBundle();
	}

}
