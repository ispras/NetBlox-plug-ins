package minerDijkstra;

import org.xml.sax.Attributes;

import ru.ispras.modis.NetBlox.exceptions.ScenarioException;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.MiningDescriptionParser;
import ru.ispras.modis.NetBlox.parser.basicParsersAndUtils.XMLIntegerRangeStringProcessor;
import ru.ispras.modis.NetBlox.parser.xmlParser.ParserContext;
import ru.ispras.modis.NetBlox.parser.xmlParser.XMLElementProcessor;
import ru.ispras.modis.NetBlox.scenario.DescriptionGraphMiningAlgorithm;
import ru.ispras.modis.NetBlox.scenario.RangeOfValues;

/**
 * Parses the description of Dijkstra's shortest path algorithm for graph mining.
 * 
 * @author ilya
 */
public class DijkstraMinerDescriptionParser extends MiningDescriptionParser {
	class SourceIDsProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> ids = getValues();
			if (ids != null  &&  !ids.isEmpty())	{
				((DijkstraMiningDescription) getParsedDescription()).setSourceNodes(ids);
			}
		}
	}

	class TargetIDsProcessor extends XMLIntegerRangeStringProcessor	{
		@Override
		public void closeElement()	{
			super.closeElement();

			RangeOfValues<Integer> ids = getValues();
			if (ids != null  &&  !ids.isEmpty())	{
				((DijkstraMiningDescription) getParsedDescription()).setTargetNodes(ids);
			}
		}
	}


	private static final String TAG_SOURCE_NODE_ID = "sourceNodeID";
	private static final String TAG_TARGET_NODE_ID = "targetNodeID";

	private static final String ATTRIBUTE_FIND_FOR_ALL = "findForAll";
	private static final String ATTRIBUTE_FIND_SINGLE = "findSingle";


	public DijkstraMinerDescriptionParser()	{
		super();
		addTaggedParser(TAG_SOURCE_NODE_ID, new SourceIDsProcessor());
		addTaggedParser(TAG_TARGET_NODE_ID, new TargetIDsProcessor());
	}


	@Override
	protected DescriptionGraphMiningAlgorithm createMinerDescription() {
		return new DijkstraMiningDescription();
	}


	@Override
	public void createElement(XMLElementProcessor aparent, String tagName,
			Attributes attributes, ParserContext acontext) {
		super.createElement(aparent, tagName, attributes, acontext);

		String findBooleanInString = attributes.getValue(ATTRIBUTE_FIND_FOR_ALL);
		if (findBooleanInString != null)	{
			boolean findForAll = Boolean.parseBoolean(findBooleanInString);
			((DijkstraMiningDescription)getParsedDescription()).setFindForAll(findForAll);
		}

		findBooleanInString = attributes.getValue(ATTRIBUTE_FIND_SINGLE);
		if (findBooleanInString != null)	{
			boolean findSingle = Boolean.parseBoolean(findBooleanInString);
			((DijkstraMiningDescription)getParsedDescription()).setFindSingle(findSingle);
		}
	}


	@Override
	public void closeElement(){
		super.closeElement();

		DijkstraMiningDescription description = (DijkstraMiningDescription)getParsedDescription();
		if (!description.findForAll())	{	//XXX How are we supposed to deal with discovered errors in scenario?..
			if (!description.isSourceNodeSpecified())	{
				throw new ScenarioException("The source node for Dijkstra's algorithm hasn't been specified, while the mode supposes it should be.");
			}
			if (!description.isTargetNodeSpecified())	{
				throw new ScenarioException("The target node for Dijkstra's algorithm hasn't been specified, while the mode supposes it should be.");
			}
		}
	}
}
