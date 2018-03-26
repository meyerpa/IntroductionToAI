package ga;
import utils.Fitness;

import java.util.Random;
import java.util.Arrays;
import java.lang.Math;


public class GeneticAlgorithm {

	protected int mPopulationSize;
	protected int mTournamentsSize;
	protected double mCrossoverProb;
	protected double mMutationProb;
	protected int[] population;
	private int length = 16;		// 16 bits long, 8 for y, 8 for x
	private int generation = 0;
	private Random rand;			// random number generator

	public GeneticAlgorithm(int populationSize,
							int tournamentsSize, double crossoverProb, double mutationProb) {
		mPopulationSize = populationSize;
		mTournamentsSize = tournamentsSize;
		mCrossoverProb = crossoverProb;
		mMutationProb = mutationProb;

		rand = new Random(0);

		population = this.createInitialPopulation();
	}

	private int[] createInitialPopulation() {
		// creates a two byte array initialized randomly
		int[] pop = new int[mPopulationSize];
		for (int i = 0; i < mPopulationSize; i++) {
			pop[i] = this.generateRandomChrom();
		}
		return pop;
	}

	private int generateRandomChrom() {
		int maximum = (int) Math.pow(2, length);
		return rand.nextInt(maximum);
	}

	private int selectRandomChrom() {
		// Returns random chromosome from current generation
		int index = rand.nextInt(mPopulationSize);
		return population[index];
	}

	public void runOneGeneration() {
		// generates next generation
		generation = generation + 1;

		int[] next = new int[mPopulationSize];
		for (int i = 0; i < mPopulationSize - 1; i = i+2) {
			// tournament selection
			int first = selectChromosome();
			int second = selectChromosome();
			// crossover
			TwoChromosomes crossed = crossover(first, second);
			// mutate
			first = this.mutate(crossed.getFirst());
			second = this.mutate(crossed.getSecond());
			// assign value in array
			next[i] = first;
			next[i+1] = second;
		}
		// handle the case where population size is odd by
		// generating another chromosome
		if (mPopulationSize % 2 == 1)
		{
			// tournament selection
			int first = selectChromosome();
			int second = selectChromosome();
			// crossover
			TwoChromosomes crossed = crossover(first, second);
			// mutate
			first = this.mutate(crossed.getFirst());
			// assign value in array
			next[mPopulationSize - 1] = first;
		}

		population = Arrays.copyOf(next, next.length);
	}

	public double getAverageFitness() {
		// returns the average fitness (total fitness / population size)
		double totalFitness = 0.0;
		for (int i = 0; i < mPopulationSize; i++)
			totalFitness += this.getFitness(population[i]);
		return totalFitness / mPopulationSize;
	}

	public double getBestFitness() {
		// Returns the best fitness in the current population
		double[] fitness = new double[mPopulationSize];
		double max = Integer.MIN_VALUE;
		for (int i = 0; i < mPopulationSize; i++) {
			fitness[i] = this.getFitness(population[i]);
			max = fitness[i] > max ? fitness[i] : max;  // update max
		}
		return max;
	}

	private double getFitness(int chrom) {
		// returns the fitness based on the function
		// f(x,y) = (1-x)^2*e^((-x)^2*-(y+1)^2)-(x-x^3 - y^3) * e^(-x^2-y^2)
		// separates into x and y
		// maps onto the range of x in (-3, 3) and y in (-3, 3)
		int yBits = length / 2;
		double mapping = 6.0 / (256.0 - 1.0);
		double shift = 3.0;
		double x = mapping * (chrom / (int) Math.pow(2, yBits)) - shift; // gets x value using integer division
		double y = mapping * (chrom % (int) Math.pow(2, yBits)) - shift; //gets y value using modulus

		double fit = new Fitness().calculateFitness(x, y);
		return fit;
	}

	private int selectChromosome() {
		// randomly selects tournament size chromosomes, and selects the best chromosome
		double best = Double.MIN_VALUE;		// fitness of best chromosome
		int winner = 0;
		for (int i = 0; i < mTournamentsSize; i++) {
			int testChrom = this.selectRandomChrom();
			if (this.getFitness(testChrom) > best) {
				best = this.getFitness(testChrom);
				winner = testChrom;
			}
		}
		return winner;
	}

	private TwoChromosomes crossover(int chrom1, int chrom2) {
		// randomly selects crossover point and returns the new chromosomes
		// only do for crossover rate
		if (rand.nextDouble() < mCrossoverProb) {
			int crossoverPt = rand.nextInt(length);
			int bitMask = (1 << crossoverPt) - 1;            // has ones in lower crossoverPt e.g. 00000001111111...
			int lowerFirst = chrom1 & bitMask;
			// switch lower part of chrom2 with chrom1
			chrom1 = chrom1 & ~bitMask;                        // get rid of current lower values
			chrom1 = chrom1 | (chrom2 & bitMask);
			// put chrom2's lower values into chrom1
			chrom2 = chrom2 & ~bitMask;                        // clear gene2's lower values
			chrom2 = chrom2 | lowerFirst;                        // put gene1's lower values into gene2
		}
		return new TwoChromosomes(chrom1, chrom2);
	}

	private int mutate(int chrom) {
		// flips bit in chromosomes with the mutation probability or returns same gene
		for (int i = 0; i < length; i++)
			if (rand.nextDouble() < mMutationProb) {
				chrom = chrom ^ (1 << i);			// flip bit
		}
		return chrom;
	}
}


// Class to hold two chromosomes
final class TwoChromosomes {
	private int first;
	private int second;

	public TwoChromosomes(int one, int two) {
		first = one;
		second = two;
	}

	public int getFirst() {
		return first;
	}

	public int getSecond() {
		return second;
	}
}