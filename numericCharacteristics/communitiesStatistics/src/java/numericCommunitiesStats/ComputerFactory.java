package numericCommunitiesStats;

import numericCommunitiesStats.computers.AverageCohesionViaTrianglesComputer;
import numericCommunitiesStats.computers.CohesionPerCommunityViaTrianglesComputer;
import numericCommunitiesStats.computers.CohesivenessByNCPPlotComputer;
import numericCommunitiesStats.computers.CommonEdgeFrequencyComputer;
import numericCommunitiesStats.computers.ConductanceBasedOnInternalEdgesComputer;
import numericCommunitiesStats.computers.ConnectedComponentsInCommunitiesComputer;
import numericCommunitiesStats.computers.ConnectedComponentsInCommunitiesDistributionComputer;
import numericCommunitiesStats.computers.DataInFilesStatisticComputer;
import numericCommunitiesStats.computers.DataInsideStatisticComputer;
import numericCommunitiesStats.computers.DensityComputer;
import numericCommunitiesStats.computers.InternalCommunityDegreeFractionComputer;
import numericCommunitiesStats.computers.NumberOfEdgesFromSizeComputer;
import numericCommunitiesStats.computers.SeparabilityComputer;
import numericCommunitiesStats.computers.TriangleParticipationRatioComputer;

public class ComputerFactory {
	private static CohesivenessByNCPPlotComputer cohesivenessComputer = null;
	private static TriangleParticipationRatioComputer tprComputer = null;

	public static DataInsideStatisticComputer getComputerForGroupsOfNodesInInternalRepresentation(String characteristicName)	{
		DataInsideStatisticComputer computer = null;

		if (characteristicName.equals(CohesivenessByNCPPlotComputer.NAME_IN_SCENARIO))	{
			if (cohesivenessComputer == null)	{
				cohesivenessComputer = new CohesivenessByNCPPlotComputer();
			}
			computer = cohesivenessComputer;
		}
		else if (characteristicName.equals(TriangleParticipationRatioComputer.NAME_IN_SCENARIO))	{
			if (tprComputer == null)	{
				tprComputer = new TriangleParticipationRatioComputer();
			}
			computer = tprComputer;
		}

		return computer;
	}


	private static SeparabilityComputer separabilityComputer = null;
	private static DensityComputer densityComputer = null;
	private static ConductanceBasedOnInternalEdgesComputer conductanceComputer = null;
	private static InternalCommunityDegreeFractionComputer icdfComputer = null;
	private static ConnectedComponentsInCommunitiesComputer ccicComputer = null;
	private static ConnectedComponentsInCommunitiesDistributionComputer ccicDistributionComputer = null;

	private static CohesionPerCommunityViaTrianglesComputer cohesionViaTrianglesComputer = null;
	private static AverageCohesionViaTrianglesComputer averageCohesionViaTrianglesComputer = null;
	private static CommonEdgeFrequencyComputer commonEdgeFrequencyComputer = null;
	private static NumberOfEdgesFromSizeComputer numberOfEdgesFromSizeComputer = null;

	public static DataInFilesStatisticComputer getComputerForGroupsOfNodesInFiles(String characteristicName)	{
		DataInFilesStatisticComputer computer = null;

		if (characteristicName.equals(SeparabilityComputer.NAME_IN_SCENARIO))	{
			if (separabilityComputer == null)	{
				separabilityComputer = new SeparabilityComputer();
			}
			computer = separabilityComputer;
		}
		else if (characteristicName.equals(DensityComputer.NAME_IN_SCENARIO))	{
			if (densityComputer == null)	{
				densityComputer = new DensityComputer();
			}
			computer = densityComputer;
		}
		else if (characteristicName.equals(ConductanceBasedOnInternalEdgesComputer.NAME_IN_SCENARIO))	{
			if (conductanceComputer == null)	{
				conductanceComputer = new ConductanceBasedOnInternalEdgesComputer();
			}
			computer = conductanceComputer;
		}
		else if (characteristicName.equals(InternalCommunityDegreeFractionComputer.NAME_IN_SCENARIO))	{
			if (icdfComputer == null)	{
				icdfComputer = new InternalCommunityDegreeFractionComputer();
			}
			computer = icdfComputer;
		}
		else if (characteristicName.equals(ConnectedComponentsInCommunitiesComputer.NAME_IN_SCENARIO))	{
			if (ccicComputer == null)	{
				ccicComputer = new ConnectedComponentsInCommunitiesComputer();
			}
			computer = ccicComputer;
		}
		else if (characteristicName.equals(ConnectedComponentsInCommunitiesDistributionComputer.NAME_IN_SCENARIO))	{
			if (ccicDistributionComputer == null)	{
				ccicDistributionComputer = new ConnectedComponentsInCommunitiesDistributionComputer();
			}
			computer = ccicDistributionComputer;
		}
		else if (characteristicName.equals(CohesionPerCommunityViaTrianglesComputer.NAME_IN_SCENARIO))	{
			if (cohesionViaTrianglesComputer == null)	{
				cohesionViaTrianglesComputer = new CohesionPerCommunityViaTrianglesComputer();
			}
			computer = cohesionViaTrianglesComputer;
		}
		else if (characteristicName.equals(AverageCohesionViaTrianglesComputer.NAME_IN_SCENARIO))	{
			if (averageCohesionViaTrianglesComputer == null)	{
				averageCohesionViaTrianglesComputer = new AverageCohesionViaTrianglesComputer();
			}
			computer = averageCohesionViaTrianglesComputer;
		}
		else if (characteristicName.equals(CommonEdgeFrequencyComputer.NAME_IN_SCENARIO))	{
			if (commonEdgeFrequencyComputer == null)	{
				commonEdgeFrequencyComputer = new CommonEdgeFrequencyComputer();
			}
			computer = commonEdgeFrequencyComputer;
		}
		else if (characteristicName.equals(NumberOfEdgesFromSizeComputer.NAME_IN_SCENARIO))	{
			if (numberOfEdgesFromSizeComputer == null)	{
				numberOfEdgesFromSizeComputer = new NumberOfEdgesFromSizeComputer();
			}
			computer = numberOfEdgesFromSizeComputer;
		}

		return computer;
	}
}
