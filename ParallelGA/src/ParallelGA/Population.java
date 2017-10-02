package ParallelGA;

import java.util.Arrays;

/**
 * @author Kyle Zeller
 * This class provides access to the array of chromosomes within it as well as the
 * student arrays inside each of those.
 */
public class Population {
    //array of chromosomes
    private final Chromosome[] chromosomes;
    //constant from amount of chromosomes in a population
    private final int chromAmount = 64;

    /**
     * Create a new population
     */
    public Population() {
        this.chromosomes = new Chromosome[chromAmount];
        //initialize population
        //loop and create new chromosomes
        for (int i = 0; i < chromAmount; i++) {
            this.chromosomes[i] = new Chromosome();
        }
    }

    /**
     * Create new population based on an array of chromosomes
     * @param arr an array of chromosomes
     */
    public Population(Chromosome[] arr) {
        this.chromosomes = arr;
    }

    /**
     * Get the array of chromosomes in the current population
     * @return the array of chromosomes in the current population
     */
    public Chromosome[] getChromosomes() {
        return chromosomes;
    }

    /**
     * Get the chromosome at the position
     * @param x the position
     * @return the chromosome at the position
     */
    public Chromosome getChromosome(int x) {
        return this.chromosomes[x];
    }

    /**
     * Calculates the most fit chromosome in the population
     * @return the most fit chromosome in the population
     */
    public Chromosome getWinningChromosome() {
        FitnessCalculator fitCalc = new FitnessCalculator();
        //calculate the fitness of all the chromosomes
        double[] fitnessArr = new double[chromAmount];
        for (int i = 0; i < chromAmount; ++i) {
            fitnessArr[i] = fitCalc.getOverallFitness(chromosomes[i]);
        }

        double[] copy = fitnessArr;

        //sort all the chromosomes
        Arrays.sort(fitnessArr);

        //return the best chromosome
        int position = 0;
        //lookup the fitness in the copy array to find the corresponding position to use
        for (int j = 0; j < copy.length; ++j) {
            if (copy[j] == fitnessArr[fitnessArr.length-1]) {
                position = j;
            }
        }
        return chromosomes[position];
    }

    /**
     * Calculate the top half chromosomes in the population based on their fitness metrics
     * @return the top half chromosomes in the population based on their fitness metrics
     */
    public Chromosome[] getBestHalfChromosomes() {
        FitnessCalculator fitCalc = new FitnessCalculator();

        //calculate the fitness of all the chromosomes
        double[] fitnessArr = new double[chromAmount];
        for (int i = 0; i < chromAmount; ++i) {
            fitnessArr[i] = fitCalc.getOverallFitness(chromosomes[i]);
        }

        double[] copy = fitnessArr;

        //sort all the chromosomes
        Arrays.sort(fitnessArr);

        //return the best half chromosomes
        Chromosome[] buffer = new Chromosome[chromAmount/2];
        int count = 0;
        //lookup the fitness in the copy array to find the corresponding position to use
        for (int i = fitnessArr.length/2; i < fitnessArr.length; ++i) {
            for (int j = 0; j < copy.length; ++j) {
                if (copy[j] == fitnessArr[i]) {
                    buffer[count] = chromosomes[j];
                    ++count;
                    break;
                }
            }
        }
        return buffer;
    }

    /**
     * Calculate the new Chromosome array from the fitness metrics and the mutate and crossover methods
     * @return the new Chromosome array from the fitness metrics and the mutate and crossover methods
     */
    public Chromosome[] breed() {
        Chromosome[] bestHalf = getBestHalfChromosomes();
        Chromosome[] children = new Chromosome[bestHalf.length/2];
        for (int i = 0; i < children.length; ++i) {
            children[i] = new Chromosome(bestHalf[i].crossover(bestHalf[bestHalf.length-i-1].getSeatArr()));
            children[i] = new Chromosome(children[i].maybeMutateChromosome());
        }
        //create a random chromosome array
        Chromosome[] newChromosomes = new Chromosome[children.length];
        for (int i = 0; i < newChromosomes.length; ++i) {
            newChromosomes[i] = new Chromosome();
        }
        int newCounter = 0;
        //fill the buffer
        Chromosome[] buffer = new Chromosome[chromAmount];
        for (Chromosome aBestHalf : bestHalf) {
            buffer[newCounter] = aBestHalf;
            ++newCounter;
        }
        for (Chromosome aChildren : children) {
            buffer[newCounter] = aChildren;
            ++newCounter;
        }
        //save the random chromosomes
        for (Chromosome newChromosome : newChromosomes) {
            buffer[newCounter] = newChromosome;
            ++newCounter;
        }
        return buffer;
    }

    /**
     * Calculate the average fitness value of a population
     * @return the average fitness value of a population
     */
    public double getPopulationAverage() {
        FitnessCalculator fitCalc = new FitnessCalculator();

        double ave = 0;
        //calculate the fitness of all the chromosomes
        for (int i = 0; i < chromAmount; ++i) {
            ave += fitCalc.getOverallFitness(chromosomes[i]);
        }

        return (ave/chromAmount);
    }
}
