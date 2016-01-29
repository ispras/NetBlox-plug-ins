package numericGraphStats;

import numericGraphStats.computers.AverageDegreeComputerViaSNAP;
import numericGraphStats.computers.ClusteringCoefficientComputerViaSNAP;
import numericGraphStats.computers.DegreesDistributionComputerViaSNAP;
import numericGraphStats.computers.DiameterComputerUsingSNAP;
import numericGraphStats.computers.SNAPBasedComputer;

public class ComputerFactory {
	private static DiameterComputerUsingSNAP diameterComputerUsingSNAP = null;
	private static ClusteringCoefficientComputerViaSNAP clusteringCoefComputerUsingSNAP = null;
	private static DegreesDistributionComputerViaSNAP degreesDistributionComputerUsingSNAP = null;
	private static AverageDegreeComputerViaSNAP averageDegreeComputerUsingSNAP = null;

	public static SNAPBasedComputer getComputer(String characteristicName)	{
		SNAPBasedComputer computer = null;

		if (characteristicName.equals(DiameterComputerUsingSNAP.NAME_IN_SCENARIO))	{
			if (diameterComputerUsingSNAP == null)	{
				diameterComputerUsingSNAP = new DiameterComputerUsingSNAP();
			}
			computer = diameterComputerUsingSNAP;
		}
		else if (characteristicName.equals(ClusteringCoefficientComputerViaSNAP.NAME_IN_SCENARIO))	{
			if (clusteringCoefComputerUsingSNAP == null)	{
				clusteringCoefComputerUsingSNAP = new ClusteringCoefficientComputerViaSNAP();
			}
			computer = clusteringCoefComputerUsingSNAP;
		}
		else if (characteristicName.equals(DegreesDistributionComputerViaSNAP.NAME_IN_SCENARIO))	{
			if (degreesDistributionComputerUsingSNAP == null)	{
				degreesDistributionComputerUsingSNAP = new DegreesDistributionComputerViaSNAP();
			}
			computer = degreesDistributionComputerUsingSNAP;
		}
		else if (characteristicName.equals(AverageDegreeComputerViaSNAP.NAME_IN_SCENARIO))	{
			if (averageDegreeComputerUsingSNAP == null)	{
				averageDegreeComputerUsingSNAP = new AverageDegreeComputerViaSNAP();
			}
			computer = averageDegreeComputerUsingSNAP;
		}

		return computer;
	}
}
