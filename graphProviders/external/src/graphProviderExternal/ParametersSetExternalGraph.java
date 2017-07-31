package graphProviderExternal;

import java.util.List;

import ru.ispras.modis.NetBlox.scenario.UploadedGraphDataParametersSet;
import ru.ispras.modis.NetBlox.scenario.ValueFromRange;
import ru.ispras.modis.NetBlox.utils.Pair;


/**
 * The set of parameters for an externally provided graph.
 * 
 * @author ilya
 */
public class ParametersSetExternalGraph extends UploadedGraphDataParametersSet {

	public ParametersSetExternalGraph(String graphDescriptionID, boolean directed, boolean weighted,
			String directoryPathname, String graphFileName, ValueFromRange<Integer> generation) {
		super("External", graphDescriptionID, directed, weighted, directoryPathname, graphFileName, generation);
		setPathToRequiredGraphExternallyFixed(true);
	}

	/**
	 * There're no parameters for these graphs except the path to the directory with the graph, see the superclass.
	 */
	@Override
	public List<Pair<String, String>> getSpecifiedParametersAsPairsOfUniqueKeysAndValues() {
		return null;
	}
}
