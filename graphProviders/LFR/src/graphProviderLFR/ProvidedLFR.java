package graphProviderLFR;

import ru.ispras.modis.NetBlox.graphAlgorithms.graphProvision.ProvidedGraph;

public class ProvidedLFR extends ProvidedGraph {
	private String graphFilePathString;
	private String coverFilePathString;

	public ProvidedLFR(String graphFilePathString, String coverFilePathString) {
		super(ProvidedLFR.ResultsProvisionFormat.FILE_PATH_STRING);

		this.graphFilePathString = graphFilePathString;
		this.coverFilePathString = coverFilePathString;
	}

	public String getGraphFilePathString()	{
		return graphFilePathString;
	}
	public String getCoverFilePathString()	{
		return coverFilePathString;
	}
}
