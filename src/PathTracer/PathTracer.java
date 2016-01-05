package PathTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

final public class PathTracer {
    public int windowWidth = 300;
    public int windowHeight = 300;

    private JButton renderButton;
    private RenderCanvas renderCanvas;
    private JFrame renderWindow;

    public PathTracer (int screenWidth, int screenHeight)  {
        this.windowWidth = screenWidth;
        this.windowHeight = screenHeight;

        this.renderWindow = new JFrame("PathTracer");
        this.renderButton = new JButton("Start Render");
        this.renderCanvas = new RenderCanvas(this.windowWidth, this.windowHeight);
    }

    public void init () {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();

        this.renderWindow.setLayout(new BorderLayout());

        this.renderButton.addActionListener(this::renderButtonHandler);

        this.renderWindow.add(this.renderCanvas);
        this.renderWindow.add(this.renderButton, BorderLayout.NORTH);

        this.renderWindow.setBounds(
            screenWidth / 2 - this.windowWidth / 2,
            screenHeight / 2 - this.windowHeight / 2,
            this.windowWidth,
            this.windowHeight
        );
        this.renderWindow.setResizable(false);
        this.renderWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.renderWindow.setVisible(true);
    }

    /**
     * @param event event object
     */
    private void renderButtonHandler (ActionEvent event) {
        new RenderThreadsController<>(this::redrawCanvasCallback);
    }

    /**
     * @param data HasMap with data
     */
    private void redrawCanvasCallback (Map<String, Color> data) {
        Color color = data.get("color");

        if (color == null) {
            throw new NullPointerException("Invalid HasMap key inside `data` attribute");
        }

        System.out.println(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());

        this.renderCanvas.update(color);
    }
}
