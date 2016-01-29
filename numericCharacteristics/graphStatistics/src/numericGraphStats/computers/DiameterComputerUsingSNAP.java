package numericGraphStats.computers;


public class DiameterComputerUsingSNAP extends SNAPBasedComputer {
	public static final String NAME_IN_SCENARIO = "diameter";

	private static final int DIAMETER_TASK_NUMBER_CODE = 1;

	public DiameterComputerUsingSNAP()	{
		snapBasedTaskNumberCode = DIAMETER_TASK_NUMBER_CODE;
	}
}
