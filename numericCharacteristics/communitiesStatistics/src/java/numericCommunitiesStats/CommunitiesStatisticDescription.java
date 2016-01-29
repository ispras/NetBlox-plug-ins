package numericCommunitiesStats;

import org.osgi.framework.Bundle;

import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;
import ru.ispras.modis.NetBlox.utils.MiningJobBase;

public class CommunitiesStatisticDescription extends DescriptionMeasure {

	public CommunitiesStatisticDescription() {
		super(MiningJobBase.JobBase.NODES_GROUPS_SET);
	}

	@Override
	protected Bundle getImplementingPluginBundle() {
		return Activator.getContext().getBundle();
	}
}
