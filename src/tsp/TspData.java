package tsp;

/**
 * Interface used to access a subset (a sample) of a full tsp data set.
 */
public interface TspData {

	/**
	 * Returns the distance between two cities.
	 *
	 * @param i First city index.
	 * @param j Second city index.
	 * @return Distance between the two cities.
	 * @throws IndexOutOfBoundsException If i or j are out of bounds.
	 */
	int getDistance(int i, int j);

	/**
	 * Returns the number of cities of this problem instance.
	 *
	 * @return Number of cities.
	 */
	int getNumberOfCities();

	/**
	 * Returns X coordinate of a city
	 *
	 * @param i City index
	 * @return X coordinate of the city
	 * @throws IndexOutOfBoundsException If i is out of bounds.
	 */
	int getXCoordinateForCity(int i);

	/**
	 * Returns Y coordinate of a city
	 *
	 * @param i City index
	 * @return Y coordinate of the city
	 * @throws IndexOutOfBoundsException If i is out of bounds.
	 */
	int getYCoordinateForCity(int i);
}
