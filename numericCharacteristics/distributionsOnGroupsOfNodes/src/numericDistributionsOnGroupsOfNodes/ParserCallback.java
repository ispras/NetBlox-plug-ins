package numericDistributionsOnGroupsOfNodes;

import java.io.InputStream;

import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IGraphMiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.extensionInterfaces.IMeasureDescriptionParser;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.DescriptionMeasure;

public class ParserCallback implements IGraphMiningDescriptionParser, IMeasureDescriptionParser {

	@Override
	public DescriptionGraphMiningAlgorithm parseMiningDescription(InputStream tagContent) {
		return new DescriptionAsMiningAlgorithm();
	}

	@Override
	public DescriptionMeasure parseMeasureDescription(InputStream tagContent) {
		return new DescriptionAsCommunitiesStatistic();
	}
}
