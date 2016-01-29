package numericGraphStats.computers;

/**
 * Computes clustering coefficient with Watts and Strogatz algorithm using
 * external SNAP implementation.
 * 
 * @author ilya
 */
public class ClusteringCoefficientComputerViaSNAP extends SNAPBasedComputer {
	public static final String NAME_IN_SCENARIO = "clusteringCoef";

	private static final int CLUSTERING_COEFFICIENT_TASK_NUMBER_CODE = 2;

	public ClusteringCoefficientComputerViaSNAP() {
		snapBasedTaskNumberCode = CLUSTERING_COEFFICIENT_TASK_NUMBER_CODE;
	}
}
