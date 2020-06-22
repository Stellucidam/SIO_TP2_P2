package tsp;

import java.util.*;

/**
 * Class representing a solution for an instance of the TSP.
 * <p>
 * The class also provides access to heuristics method to calculate a solution.
 */
public class TspTour {

	// Reference to problem instance
	private final TspData data;
	// Array storing the permutation of city indices defining current solution
	private int[] tour;
	// Length of current tour
	private long tourLength;


	public TspTour(TspData data) {
		this.data = data;
		tour = new int[data.getNumberOfCities()];
		createCanonicalTour();
	}

	/**
	 * Creates a solution visiting cities in increasing order of their indices.
	 */
	public void createCanonicalTour() {
		tourLength = data.getDistance(data.getNumberOfCities() - 1, 0);
		tour[0] = 0;
		for (int i = 1; i < data.getNumberOfCities(); i++) {
			tour[i] = i;
			tourLength += data.getDistance(i - 1, i);
		}
	}

	/**
	 * Creates a solution visiting cities in random order.
	 *
	 * @param seed Seed used to initialize the random generator
	 */
	public void createRandomTour(long seed) {
		Random rnd = new Random(seed);
		int j;

		for (int i = data.getNumberOfCities() - 1; i > 0; --i) {
			j = rnd.nextInt(i + 1);
			swapInTour(i, j);
		}
		recomputeTourLength();
	}

	/**
	 * Swap two cities in tour.
	 *
	 * @param i Index of the first city.
	 * @param j Index of the second cty.
	 */
	private void swapInTour(int i, int j) {
		int tmp = tour[i];
		tour[i] = tour[j];
		tour[j] = tmp;
	}

	/**
	 * Recompute length of the current tour.
	 */
	public void recomputeTourLength() {
		tourLength = data.getDistance(tour[0], tour[tour.length - 1]);
		for (int i = 0; i < tour.length - 1; ++i) {
			tourLength += data.getDistance(tour[i], tour[i + 1]);
		}
	}

	/**
	 * Returns the length of this tour.
	 *
	 * @return Length of current solution
	 */
	public long getTourLength() {
		return tourLength;
	}

	/**
	 * Returns index of the city at a given position in current solution.
	 * Position parameter must be between 1 and number of cities of this tour.
	 *
	 * @param pos Position in the tour of the city index to be retrieved
	 * @return Index of the city at requested position
	 * @throws IndexOutOfBoundsException If parameter is not between 1 and the number of cities of this tour
	 */
	public int getCityAtPosition(int pos) {
		// Check for out of bounds position
		if (pos < 1 || pos > tour.length) {
			throw new IndexOutOfBoundsException("Position out of bounds.");
		}

		return tour[pos - 1];
	}

	/**
	 * Returns a string representation of the current tour.
	 *
	 * @return String representation of the current tour
	 */
	public String toString() {
		return Arrays.toString(tour);
	}


	/****************************************************************************************************************
	 *
	 * Ne modifiez rien dans ce qui précède. Ajoutez votre code uniquement en fin de fichier. Merci !
	 *
	 * Ajoutez votre nom ci-dessous svp.
	 *
	 * Nom : Fleurimont Clarisse
	 *
	 ****************************************************************************************************************/

	/**
	 * Apply the "Nearest Neighbor From Both Ends" heuristic starting tour construction at given city index.
	 *
	 * @param start Index of starting city
	 * @throws IndexOutOfBoundsException If parameter is not a valid city index
	 */
	public void CreateNearestNeighborFromBothEndsTourSolution(int start) {
		// TODO corriger
		// Check for out of bounds start index
		if (start < 0 || start >= tour.length) {
			throw new IndexOutOfBoundsException("Starting city index out of bounds.");
		}

		// Create array of visited status for the cities
		boolean visited[] 	= new boolean[data.getNumberOfCities()];
		visited[start] 		= true;

		tour[0] 	= start;
		tourLength 	= 0;

		// Determine the nearest city to start and add it to the tour
		int cityS = start,
				cityT = start,
				sIndex = 0,
				tIndex = 0;

		// While the number of unvisited cities is >= 1...
		while (numberOfUnvisitedCities(visited) >= 1) {
			// Determine the nearest unvisited city to s or t
			int nearestToS = nearestCityTo(cityS, visited);
			int nearestToT = nearestCityTo(cityT, visited);

			int distFromT = data.getDistance(cityT, nearestToT);
			int distFromS = data.getDistance(cityS, nearestToS);

			// Check which city (nearestToS or nearestToT) is the nearest to it's corresponding neighbor
			// We chose the city with the smallest distance to it's neighbor
			// If both distances are equal, we chose the smallest city (int value)
			if ( distFromS > distFromT ||
					(distFromS == distFromT && nearestToS > nearestToT)) {
				// Add the next city next to t
				tour[data.getNumberOfCities() - ++tIndex] = nearestToT;
				tourLength += distFromT;
				// Remove the city next to t form the list of unvisited cities
				visited[nearestToT] = true;
				// Update t
				cityT = nearestToT;
			} else {
				// Add the next city next to s
				tour[++sIndex] = nearestToS;
				tourLength += distFromS;
				// Remove the city next to s form the list of unvisited cities
				visited[nearestToS] = true;
				// Update s
				cityS = nearestToS;
			}
		}
		tourLength += data.getDistance(cityS, cityT);
	}

	/**
	 * Used to get the numbers of cities yet to be added to the tour
	 * @param visited (boolean[]) status of each city
	 * @return counter (int) the number of unvisited cities
	 */
	private int numberOfUnvisitedCities(boolean[] visited) {
		int counter = 0;
		for (boolean v: visited) {
			if(!v)
				++counter;
		}
		return counter;
	}

	/**
	 * Finds the nearest city to the givent city
	 * @param city (int) the cityto which we have to find the nearest neighbor
	 * @param visited (boolean[]) list of status of cities
	 * @return nearestCity (int) the nearest city to the given city or -1 if nothing was found
	 */
	private int nearestCityTo(int city, boolean[] visited) {
		long distMin = Long.MAX_VALUE;
		int nearestCity = -1;

		for (int i = 0; i < data.getNumberOfCities(); ++i) {
			int dist = data.getDistance(city, i);

			if (dist < distMin && !visited[i]){
				nearestCity = i;
				distMin = dist;
			}
		}

		return nearestCity;
	}

	/**
	 * Applies the twoOptBest algorithm on the current tour
	 */
	public void twoOptBest() {
		long[] exchange;
		long improvement;
		boolean improvable;

		// While the are improvements to be made...
		do {
			// We search for the best ridge exchange
			exchange = findBestExchange();
			// We get the improvement resulting of the found exchange
			improvement = exchange[2];

			improvable = (improvement > 0);

			// If the improvement is positive
			if (improvable) {
				// We do the found exchange
				doExchange(exchange);
				// The improvement is reflected on the tour length
				tourLength -= improvement;
			}
		} while (improvable);
	}

	/**
	 * Finds the best exchange that can be done between two cities in the current tour
	 * The returned exchange either shortens the current tour length or [-1, -1]
	 * @return exchange (int[2])
	 */
	private long[] findBestExchange() {
		long[] exchange = {-1, -1, -1};
		long best = 0;

		// Go through the tour with i and j
		// i < j at all times
		for (int i = 0; i < tour.length - 3; ++i) {
			for (int j = i + 2; j < tour.length; ++j) {
				if(i == 0 && j == tour.length - 1){
					break;
				}

				// Calculate the sum distance of the old ridges
				long oldDistance =
						data.getDistance(tour[i], tour[i + 1]) +
								data.getDistance(tour[j], tour[(j + 1) % tour.length]);

				// Calculate the sum distance of the new ridges
				long newDistance =
						data.getDistance(tour[i], 					tour[j]) +
								data.getDistance(tour[(i+1) % tour.length], tour[(j+1) % tour.length]);

				// The improvement depends on the difference between the old and the new
				long newImprovement = oldDistance - newDistance;

				// If the new found improvement is better than the current best one
				if(newImprovement > best) {
					// We store the current improvement as the best yet
					best = newImprovement;
					exchange = new long[]{i, j, best};
				}
			}
		}

		return exchange;
	}

	public void doExchange(long[] exchange) {
		// We get the i and j positions which define the exchange to be done
		long i = exchange[0];
		long j = exchange[1];
		// Copy the current state of the tour
		int[] oldTour = Arrays.copyOf(tour, tour.length);
		int newJ = (int)j;

		// Reverse the section ]i, j] in tour
		for(int newI = (int)i + 1; newI <= j; ++newI, --newJ) {
			tour[newI] = oldTour[newJ];
		}

	}

}
