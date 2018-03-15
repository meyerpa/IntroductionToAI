package ga;

public class GeneticAlgorithm {

	protected int mPopulationSize;
	protected int mTournamentsSize;
	protected double mCrossoverProb;
	protected double mMutationProb;

	public GeneticAlgorithm(int populationSize,
			int tournamentsSize, double crossoverProb, double mutationProb) {
		mPopulationSize = populationSize;
		mTournamentsSize = tournamentsSize;
		mCrossoverProb = crossoverProb;
		mMutationProb = mutationProb;
		// ...
		
		createInitialPopulation();
	}

	public void createInitialPopulation() {
		// to be implemented
	}

	public void runOneGeneration() {s
		// to be implemented
	}

	public double getAverageFitness() {
		// to be implemented; remove 0.0
		return 0.0;
	}

	public double getBestFitness() {
		// to be implemented; remove 0.0
		return 0.0;
	}
	
	// other methods to be implemented

}
