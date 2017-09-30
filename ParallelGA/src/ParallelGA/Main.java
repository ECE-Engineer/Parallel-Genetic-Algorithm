package ParallelGA;

import java.awt.*;

/**
 * @author Kyle Zeller
 * This class invokes the graphical user interface, which displays it to the screen and listens for the button actions to be pressed
 */
public class Main {
    public static void main(String[] args) {
        //make the gui visible
        EventQueue.invokeLater(() -> {
            final GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}
