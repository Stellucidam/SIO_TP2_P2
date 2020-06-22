import montecarlo.*;
import statistics.*;
import tsp.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, TspParsingException {

		TspDataSet tspData = new TspDataSet(new FileInputStream("resources/att532.dat"));

		StatCollector stat = new StatCollector();

		Random rnd = new Random(20200525);
		Experiment exp = new TspExperiment(tspData, 0, 0.6);

		MonteCarloSimulation.simulateNRuns(exp, 10, rnd, stat);

		System.out.printf("**********************%n  Simulation results%n**********************%n");
		System.out.printf("Nb of runs : %d%n", stat.getNumberOfObs());
		System.out.printf("Mean : %.2f%n", stat.getAverage());
		System.out.printf("C.I. : %.2f +/- %.2f%n", stat.getAverage(), stat.getConfidenceIntervalHalfWidth(0.95));
	}
}
