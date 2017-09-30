/**
 * @author Kyle Zeller
 * This class provides the student affinity of a student in a classroom arrangement as
 * their liking to other students adjacent to them and is represented as an RGB value.
 */

public class Student {

    //student affinity
    private final int stuAffinity;

    /**
     * Creates a student
     * @param stuAffinity is the student affinity
     */
    public Student(int stuAffinity) {
        this.stuAffinity = stuAffinity;
    }

    /**
     * Gets the affinity value
     * @return student affinity value
     */
    public int getStuAff() {
        return this.stuAffinity;
    }

}