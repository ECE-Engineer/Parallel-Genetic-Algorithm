package ParallelGA;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kyle Zeller
 * This class provides the components of the chromosome as a 64 length student array.
 * It also provides the methods to initialize each student with a random value as well as
 * mutating and crossing genes from this chromosome with the next.
 */
public class Chromosome {
    //array of students
    private final Student[] studentArrangement;
    //constant for number of students in a chromosome
    private final int stuAmount = 64;
    //constant for the limit of an RGB value
    private final int maxRGB = 256;
    //constant for the mutation threshold
    private final int mutationConstant = 50;
    //constant for the limit of the mutation probability
    private final int mutationLimit = 101;

    /**
     * Creates a new chromosome
     */
    public Chromosome() {
        this.studentArrangement = new Student[stuAmount];
        for (int i = 0; i < stuAmount; ++i) {
            this.studentArrangement[i] = new Student(ThreadLocalRandom.current().nextInt(maxRGB));
        }
    }

    /**
     * Calculates the average RGB value for a chromosome
     * @return the average RGB value for a chromosome
     */
    public int getAverageRGB() {
        int gridSize = (int)Math.sqrt(this.getSeatArr().length);
        int ave = 0;
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                ave += this.getSeatArr()[(i * gridSize) + j].getStuAff();
            }
        }
        return (ave/this.getSeatArr().length);
    }

    /**
     * Calculates the fitness value for a chromosome
     * @return the fitness value for a chromosome
     */
    public int getFitness() {
        FitnessCalculator fitCalc = new FitnessCalculator();

        //calculate the fitness of the chromosome
        return fitCalc.getOverallFitness(this);
    }

    /**
     * Creates a new chromosome with an array of students
     * @param arr a new chromosome with an array of students
     */
    public Chromosome(Student[] arr) {
        this.studentArrangement = arr;
    }

    /**
     * Gets the student array of a chromosome
     * @return the student array of a chromosome
     */
    public Student[] getSeatArr() {
        return this.studentArrangement;
    }

    /**
     * Gets the student at the position
     * @param x the position of the student
     * @return the student at the position
     */
    public Student getStudent(int x) {
        return this.studentArrangement[x];
    }

    /**
     * Get the gene / selection of students from the student array from the beginning to the position
     * @param m the student array
     * @param pos the terminating position
     * @return the gene / selection of students from the student array from the beginning to the position
     */
    public Student[] firstN(Student[] m, int pos) {
        //create a student array buffer
        Student[] buffer = new Student[pos];
        for (int i = 0; i < pos; ++i) {
            buffer[i] = m[i];
        }
        return buffer;
    }

    /**
     * Gets the gene / selection of students from the pos to the end of the student array
     * @param f the student array
     * @param pos the starting position
     * @return the gene / selection of students from the pos to the end of the student array
     */
    public Student[] restN(Student[] f, int pos) {
        //create a student array buffer
        int thisCounter = 0;
        Student[] buffer = new Student[stuAmount-pos];
        for (int i = pos; i < stuAmount; ++i) {
            buffer[thisCounter] = f[i];
            ++thisCounter;
        }
        return buffer;
    }

    /**
     * Calculates the new student from the current student and the given student
     * @param arr the given student
     * @return the new student from the current student and the given student
     */
    public Student[] crossover(Student[] arr) {
        //create a student array buffer
        Student[] buffer = new Student[stuAmount];
        int pos = (ThreadLocalRandom.current().nextInt(this.stuAmount) + 1);
        Student[] gene1 = firstN(this.studentArrangement, pos);
        Student[] gene2 = restN(arr, pos);
        int counter = 0;
        for (int i = 0; i < gene1.length; ++i) {
            buffer[counter] = gene1[i];
            ++counter;
        }
        for (int i = 0; i < gene2.length; ++i) {
            buffer[counter] = gene2[i];
            ++counter;
        }
        return buffer;
    }

    /**
     * Calculates the new student from the old student w/ one random chosen student's affinity changed
     * @return the new student from the old student w/ one random chosen student's affinity changed
     */
    public Student[] mutate() {
        //create a student array buffer
        Student[] buffer = new Student[stuAmount];
        //randomly select and change the affinity of a student
        int selectInt = ThreadLocalRandom.current().nextInt(stuAmount);
        for (int i = 0; i < stuAmount; ++i) {
            if (i == selectInt) {
                buffer[i] = new Student(ThreadLocalRandom.current().nextInt(maxRGB));
            } else {
                buffer[i] = this.studentArrangement[i];
            }
        }
        return buffer;
    }

    /**
     * Calculates the new student from the old student w/ one random chosen student's affinity changed based on a probability
     * @return the new student from the old student w/ one random chosen student's affinity changed based on a probability
     */
    public Student[] maybeMutateChromosome() {
        if (ThreadLocalRandom.current().nextInt(mutationLimit) >= mutationConstant) {
            return this.mutate();
        } else {
            return this.getSeatArr();
        }
    }
}
