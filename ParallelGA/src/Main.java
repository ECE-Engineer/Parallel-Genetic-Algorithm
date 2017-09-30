import java.awt.*;
import java.io.IOException;

public class Main {
    /**
     * @author Kyle Zeller
     * This class invokes the graphical user interface, which displays it to the screen and listens for the button actions to be pressed
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //make the gui visible
        EventQueue.invokeLater(() -> {
            try {
                final GUI gui = new GUI();
                gui.setVisible(true);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
