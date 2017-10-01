package ParallelGA;

/**
 * @author Kyle Zeller
 * This class can calculate the fitness values of a chromosome as well as students in a chromosome.
 */
public class FitnessCalculator {

    /**
     * Calculates the entire fitness of a chromosome
     * @param c a chromosome
     * @return the entire fitness of a chromosome
     */
    public int getOverallFitness(Chromosome c) {
        int sumAffinity = 0;
        int rowColLength = (int) Math.sqrt(c.getSeatArr().length);

        for (int x = 0; x < rowColLength; x++) {
            for (int y = 0; y < rowColLength; y++) {
                sumAffinity += this.getSingleFitness(c, x, y);
            }
        }
        return sumAffinity;
    }

    /**
     * Calculates the sum of the squared differences between the current student and the adjacent student.
     * This is how much the current student likes the adjacent students.
     * Only the top, bottom, left, and right spots are calculated with respect to the current spot on the array, to
     * alleviate unnecessary computation time of calculating the four corners as well.
     * @param c chromosome
     * @param x position on x-axis
     * @param y position on y-axis
     * @return how much the current student likes the adjacent students
     */
    public int getSingleFitness(Chromosome c, int x, int y) {
        int gridSize = (int) Math.sqrt(c.getSeatArr().length);

        int currentVal = c.getSeatArr()[(gridSize * x) + y].getStuAff();

        int sum = 0;
        int left = x - 1;
        int right = x + 1;
        int top = y - 1;
        int bottom = y + 1;

        if (x != 0) { // left
            sum += (c.getSeatArr()[left + (gridSize * y)] != null) ? (int) Math.pow((currentVal - c.getSeatArr()[left + (gridSize * y)].getStuAff()), 2) : 0;
        }
        if (x != gridSize - 1) { // right
            sum += (c.getSeatArr()[right + (gridSize * y)] != null) ? (int) Math.pow((currentVal - c.getSeatArr()[right + (gridSize * y)].getStuAff()), 2) : 0;
        }
        if (y != 0) { // top
            sum += (c.getSeatArr()[top + (gridSize * x)] != null) ? (int) Math.pow((currentVal - c.getSeatArr()[top + (gridSize * x)].getStuAff()), 2) : 0;
        }
        if (y != gridSize - 1) { // bottom
            sum += (c.getSeatArr()[bottom + (gridSize * x)] != null) ? (int) Math.pow((currentVal - c.getSeatArr()[bottom + (gridSize * x)].getStuAff()), 2) : 0;
        }
        return (-sum);
    }
}
