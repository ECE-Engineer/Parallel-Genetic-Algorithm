package ParallelGA;

import java.util.Arrays;

/**
 * @author Kyle Zeller
 * This class provides access to the array of populations within it as well as the
 * chromosome arrays inside each of those as well as the student arrays inside each of those.
 */
public class GA {
    //array of populations
    private final Population[] populations;
    //constant for the amount of populations in each GA
    private final int popConst = 16;

    /**
     * Creates a new GA
     */
    public GA() {
        populations = new Population[popConst];
        for (int i = 0; i < popConst; ++i) {
            populations[i] = new Population();
        }
    }

    /**
     * Creates a new GA based on the population array
     * @param arr the population array
     */
    public GA(Population[] arr) {
        populations = arr;
    }

    /**
     * Calculates the next generation of populations based on fitness metrics
     * This is an extra layer of optimization chosen arbitrarily to see more
     * improvement in the populations quicker.
     * @return the next generation of populations based on fitness metrics
     */
    public Population[] nextGeneration() {
        Population[] buffer = new Population[popConst];

        int position = 0;
        //find the top half most fit populations
        Population[] half = this.getBestHalfPopulations();
        for (Population aHalf : half) {
            buffer[position] = aHalf;
            ++position;
        }
        //breed some arbitrary half of all the populations
        for (int i = popConst/2; i < popConst; ++i) {
            buffer[i] = new Population(populations[i].breed());
        }

        return buffer;
    }

    /**
     * Gets the population array
     * @return the population array
     */
    public Population[] getPopulations() {
        return populations;
    }

    /**
     * Calculates the most fit chromosome in every population
     * @return the most fit chromosome in every population
     */
    public Chromosome getBestChromosomeOverall() {
        Chromosome[] topChromosomes = new Chromosome[popConst];

        for (int i = 0; i < popConst; ++i) {
            topChromosomes[i] = populations[i].getWinningChromosome();
        }

        FitnessCalculator fitCalc = new FitnessCalculator();
        //calculate the fitness of all the chromosomes
        double[] fitnessArr = new double[popConst];
        for (int i = 0; i < popConst; ++i) {
            fitnessArr[i] = fitCalc.getOverallFitness(topChromosomes[i]);
        }

        final double[] copy = new double[fitnessArr.length];
        System.arraycopy(fitnessArr, 0, copy, 0, copy.length);

        //sort all the chromosomes
        Arrays.sort(fitnessArr);

        //return the best chromosome
        int position = 0;
        //lookup the fitness in the copy array to find the corresponding position to use
        for (int j = 0; j < copy.length; ++j) {
            if (copy[j] == fitnessArr[0]) {
                position = j;
            }
        }
        return topChromosomes[position];
    }

    /**
     * Displays statistics about each populations average fitness
     */
    public void displayAllPopulationAverages() {
        for (int i = 0; i < popConst; ++i) {
            System.out.println("POPULATION: " + i + " average = " + populations[i].getPopulationAverage());
        }
    }

    /**
     * Calculates the top half most fit populations based on their fitness averages
     * @return the top half most fit populations based on their fitness averages
     */
    public Population[] getBestHalfPopulations() {
        //calculate the fitness of all the chromosomes
        double[] fitnessArr = new double[popConst];
        for (int i = 0; i < popConst; ++i) {
            fitnessArr[i] = populations[i].getPopulationAverage();
        }

        final double[] copy = new double[fitnessArr.length];
        System.arraycopy(fitnessArr, 0, copy, 0, copy.length);

        //sort all the chromosomes
        Arrays.sort(fitnessArr);

        //return the best half populations
        Population[] buffer = new Population[popConst/2];
        int count = 0;
        //lookup the fitness in the copy array to find the corresponding position to use
        for (int i = 0; i < fitnessArr.length/2; ++i) {
            for (int j = 0; j < copy.length; ++j) {
                if (copy[j] == fitnessArr[i]) {
                    buffer[count] = populations[j];
                    ++count;
                    break;
                }
            }
        }
        return buffer;
    }

    /**
     * Calculates the fitness of the most fit population
     * @return the fitness of the most fit population
     */
    public double getBestPopulationValue() {
        //calculate the fitness of all the chromosomes
        double[] fitnessArr = new double[popConst];
        for (int i = 0; i < popConst; ++i) {
            fitnessArr[i] = populations[i].getPopulationAverage();
        }

        //sort all the chromosomes
        Arrays.sort(fitnessArr);

        return fitnessArr[0];
    }

    /**
     * Calculates the most fit population
     * @return the most fit population
     */
    public Population getBestPopulation() {
        //calculate the fitness of all the chromosomes
        double[] fitnessArr = new double[popConst];
        for (int i = 0; i < popConst; ++i) {
            fitnessArr[i] = populations[i].getPopulationAverage();
        }

        //sort all the chromosomes
        Arrays.sort(fitnessArr);

        Population answer = null;
        for (int i = 0; i < popConst; ++i) {
            if (fitnessArr[0] == populations[i].getPopulationAverage()) {
                answer = populations[i];
                break;
            }
        }

        return answer;
    }

    /**
     * Calculates the average of all the populations in this generation of the GA
     * @return the average of all the populations in this generation of the GA
     */
    public double getGenerationAverage() {
        double genAve = 0;
        for (int i = 0; i < popConst; ++i) {
            genAve += populations[i].getPopulationAverage();
        }

        return (genAve/popConst);
    }
}
