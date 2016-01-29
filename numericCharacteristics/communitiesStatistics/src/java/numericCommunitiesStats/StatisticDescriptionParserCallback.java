package numericCommunitiesStats;

import java.io.InputStream;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IMeasureDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;

public class StatisticDescriptionParserCallback implements IMeasureDescriptionParser {

	@Override
	public DescriptionMeasure parseMeasureDescription(InputStream tagContent) {
		return new CommunitiesStatisticDescription();
	}

}
