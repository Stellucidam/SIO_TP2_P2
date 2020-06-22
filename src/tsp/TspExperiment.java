package tsp;

import montecarlo.Experiment;

import java.util.Random;

/**
 * Implementation of Experiment used for our Monte Carlo simulation.
 */

public class TspExperiment implements Experiment {

	// Reference to full data set
	private final TspDataSet data;
	// Index of central depot city
	private final int depot;
	// Sampling probability
	private final double samplingProb;

	public TspExperiment(TspDataSet data, int depot, double samplingProb) {
		this.data = data;
		this.depot = depot;
		this.samplingProb = samplingProb;
	}

	/**
	 * Selects a sample of the cities, each with probability samplingProb (except depot which is always selected).
	 * <p>
	 * Finds a sub-optimal tour by applying the NND heuristic followed by the 2-Opt-Best heuristic and returns
	 * the length of computed solution.
	 *
	 * @param rnd random source to be used to simulate the experiment
	 * @return length of the computed tour
	 */
	@Override
	public double execute(Random rnd) {
		// TODO Write your code here
		TspTour tspTour = new TspTour(data);
		tspTour.createRandomTour(new Random().nextLong());

		return tspTour.getTourLength();
	}
}
