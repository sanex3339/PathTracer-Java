package PathTracer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class RenderWindow extends JFrame {
    int windowWidth = 300;
    int windowHeight = 300;

    /**
     * @param windowWidth
     * @param windowHeight
     */
    public RenderWindow (int windowWidth, int windowHeight) {
        super();

        this.setTitle("PathTracer");
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        setBounds(300, 300, this.windowWidth, this.windowHeight);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
    }
}
