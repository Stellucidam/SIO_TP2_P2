package tsp;

import java.util.Random;

/**
 * Class storing a random selection of the cities of a TspDataSet.
 */
public class TspSampledDataWithDepot implements TspData {

	// Reference to full data set
	private final TspDataSet data;
	// Number of cities in sample
	private final int numberOfCities;
	// Array storing the real city id (in full data set) of ith sample city
	private final int[] idInFullData;

	/**
	 * Creates an instance of a tsp by selecting randomly a subset of.
	 *
	 * @param data TspDataSet to sample cities from
	 * @param depot central depot city index (always in sample)
	 * @param samplingProb probability of selection for each city in data (except depot)
	 * @param rnd random source to be used to create the sample
	 */
	public TspSampledDataWithDepot(TspDataSet data, int depot, double samplingProb, Random rnd) {
		// Check for out of bounds start index
		if (depot < 0 || depot >= data.getNumberOfCities()) {
			throw new IndexOutOfBoundsException("Depot index out of bounds.");
		}

		// Check for invalid sampling probability
		if (samplingProb < 0.0 || samplingProb > 1.0) {
			throw new IllegalArgumentException("Sampling probability should be between 0 and 1.");
		}

		this.data = data;

		// Dictionary between sample indices and full data set indices
		idInFullData = new int[data.getNumberOfCities()];
		idInFullData[0] = depot;
		int sampleSize = 1;

		// Each city (expect depot) is selected with probability samplingProb
		for (int i = 0; i < data.getNumberOfCities(); ++i) {
			if (i != depot && rnd.nextDouble() <= samplingProb) {
				idInFullData[sampleSize++] = i;
			}
		}
		numberOfCities = sampleSize;
	}

	/**
	 * Returns the distance between two cities.
	 *
	 * @param i First sample city index.
	 * @param j Second sample city index.
	 * @return Distance between the two cities.
	 * @throws IndexOutOfBoundsException If i or j are out of bounds.
	 */
	@Override
	public int getDistance(int i, int j) {
		// Check for out of bounds indices
		if (i < 0 || i >= numberOfCities || j < 0 || j >= numberOfCities) {
			throw new IndexOutOfBoundsException("City index out of bounds.");
		}
		return data.getDistance(idInFullData[i], idInFullData[j]);
	}

	/**
	 * Returns the number of cities of this sample.
	 *
	 * @return Number of cities in the sample.
	 */
	@Override
	public int getNumberOfCities() {
		return numberOfCities;
	}

	/**
	 * Returns X coordinate of a city
	 *
	 * @param i Sample city index
	 * @return X coordinate of the city
	 * @throws IndexOutOfBoundsException If i is out of bounds.
	 */
	@Override
	public int getXCoordinateForCity(int i) {
		// Check for out of bounds index
		if (i < 0 || i >= numberOfCities) {
			throw new IndexOutOfBoundsException("City index out of bounds.");
		}

		return data.getXCoordinateForCity(idInFullData[i]);
	}

	/**
	 * Returns Y coordinate of a city
	 *
	 * @param i Sample city index
	 * @return Y coordinate of the city
	 * @throws IndexOutOfBoundsException If i is out of bounds.
	 */
	@Override
	public int getYCoordinateForCity(int i) {
		// Check for out of bounds index
		if (i < 0 || i >= numberOfCities) {
			throw new IndexOutOfBoundsException("City index out of bounds.");
		}

		return data.getYCoordinateForCity(idInFullData[i]);

	}
}
