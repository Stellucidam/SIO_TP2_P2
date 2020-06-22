package montecarlo;

import statistics.InverseStdNormalCDF;
import statistics.StatCollector;

import java.util.Random;

/**
 * This class provides methods for simple Monte Carlo simulations.
 */
public class MonteCarloSimulation {
	/**
	 * Private constructor. Makes it impossible to instantiate.
	 */
	private MonteCarloSimulation() {
	}

	/**
	 * Simulates experiment exp n times, using rnd as a source of pseudo-random numbers and collect
	 * the results in stat.
	 *
	 * @param exp  experiment to be run each time
	 * @param n    number of runs to be performed
	 * @param rnd  random source to be used to simulate the experiment
	 * @param stat collector to be used to collect the results of each experiment
	 */
	public static void simulateNRuns(Experiment exp,
									 long n,
									 Random rnd,
									 StatCollector stat) {
		for (long run = 0; run < n; ++run) {
			stat.add(exp.execute(rnd));
		}
	}

	/**
	 * First simulates experiment exp initialNumberOfRuns times, then estimates the number of runs
	 * needed for a 95% confidence interval half width no more than maxHalfWidth. If final C.I. is
	 * too wide, simulates additionalNumberOfRuns before recalculating the C.I. and repeats the process
	 * as many times as needed.
	 * <p>
	 * Uses rnd as a source of pseudo-random numbers and collects the results in stat.
	 *
	 * @param exp                    experiment to be run each time
	 * @param level                  confidence level of the confidence interval
	 * @param maxHalfWidth           maximal half width of the confidence interval
	 * @param initialNumberOfRuns    initial number of runs to be performed
	 * @param additionalNumberOfRuns additional number of runs to be performed if C.I. is too wide
	 * @param rnd                    random source to be used to simulate the experiment
	 * @param stat                   collector to be used to collect the results of each experiment
	 */
	public static void simulateTillGivenCIHalfWidth(Experiment exp,
													double level,
													double maxHalfWidth,
													long initialNumberOfRuns,
													long additionalNumberOfRuns,
													Random rnd,
													StatCollector stat) {
		// 1) Dans une première phase Ninit simulations de l’expérience sont effectuées
		simulateNRuns(exp, initialNumberOfRuns, rnd, stat);

		// 2) À partir des données récoltées une estimation du nombre N de réalisations à générer
		//    afin d’obtenir un intervalle de confiance dont la demi-largeur ne dépasse pas (delta)max est
		//    effectuée. Cette valeur de N est ensuite arrondie, vers le haut, au plus proche multiple
		//    de Nadd.
		double normalQuantile = InverseStdNormalCDF.getQuantile(0.5 - level / 2.0);
		double estimationOfN = Math.pow(((normalQuantile * stat.getStandardDeviation()) / maxHalfWidth), 2);
		estimationOfN = Math.ceil(estimationOfN / additionalNumberOfRuns) * additionalNumberOfRuns;

		// 3) La simulation est poursuivie jusqu’à atteindre N réalisations de l’expérience.
		long numberOfRealisationToReachN = (long) estimationOfN - initialNumberOfRuns;
		simulateNRuns(exp, numberOfRealisationToReachN, rnd, stat);

		// 4) Si la demi-largeur de l’intervalle de confiance, calculé sur la base de ces N réalisations,
		//    est inférieure ou égale à (delta)max le processus s’arrête. Sinon Nadd simulations supplémentaires
		//    sont effectuées avant de recalculer un nouvel intervalle de confiance et de retester
		//    la condition d’arrêt. Ce processus est répété jusqu’à ce que la condition d’arrêt soit
		//    satisfaite.
		while (stat.getConfidenceIntervalHalfWidth(level) >= maxHalfWidth) {
			simulateNRuns(exp, additionalNumberOfRuns, rnd, stat);
		}
	}
}
