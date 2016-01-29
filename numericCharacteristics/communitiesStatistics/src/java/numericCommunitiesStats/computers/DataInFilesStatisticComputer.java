package numericCommunitiesStats.computers;

import ru.ispras.modis.NetBlox.dataStructures.NumericCharacteristic;
import ru.ispras.modis.NetBlox.graphAlgorithms.graphMining.GraphOnDrive;
import scala.Tuple2;

public abstract class DataInFilesStatisticComputer {

	public abstract NumericCharacteristic compute(GraphOnDrive graphOnDrive, String groupsOfNodesFilePathString);


	protected NumericCharacteristic makeCharacteristicOutOfDoubleValues(scala.collection.immutable.List<Object> values)	{
		if (values == null  ||  values.isEmpty())	{
			return null;
		}

		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.LIST_OF_VALUES);
		scala.collection.Iterator<Object> iterator = values.iterator();
		while (iterator.hasNext())	{
			Double value = (Double) iterator.next();
			result.addValue(value);
		}
		return result;
	}


	/**
	 * Receives a list of <code>Tuple2</code> scala tuples, in which the 1st element contains some data
	 * and the 2nd element keeps the value of characteristic that we need. The method extracts those
	 * significant characteristic values and puts them into a <code>CharacteristicOrMeasure</code> object.
	 */
	protected NumericCharacteristic extractCharacteristicSignificant2ndValues(
			scala.collection.immutable.List<Tuple2<Object, Object>> tuplesObjectAndSignificantValue)	{
		NumericCharacteristic result = new NumericCharacteristic(NumericCharacteristic.Type.LIST_OF_VALUES);

		scala.collection.Iterator<Tuple2<Object, Object>> iterator = tuplesObjectAndSignificantValue.iterator();
		while (iterator.hasNext())	{
			Tuple2<Object, Object> objectAndSignificantValuePair = iterator.next();
			Double characteristicValue = (Double) objectAndSignificantValuePair._2();
			result.addValue(characteristicValue);
		}

		return result;
	}
}
