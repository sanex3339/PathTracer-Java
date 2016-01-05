package PathTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class PathTracer {
    public int windowWidth = 300;
    public int windowHeight = 300;

    private JButton renderButton;
    private RenderCanvas renderCanvas;
    private RenderWindow renderWindow;

    public PathTracer (int screenWidth, int screenHeight)  {
        this.windowWidth = screenWidth;
        this.windowHeight = screenHeight;

        this.renderWindow = new RenderWindow(this.windowWidth, this.windowHeight);
        this.renderButton = new JButton("Start Render");
        this.renderCanvas = new RenderCanvas(this.windowWidth, this.windowHeight);
    }

    public void init () {
        this.renderWindow.setLayout(new BorderLayout());

        this.renderButton.addActionListener(this::renderButtonHandler);

        this.renderWindow.add(this.renderCanvas);
        this.renderWindow.add(this.renderButton, BorderLayout.NORTH);

        this.renderWindow.pack();
        this.renderWindow.setSize(new Dimension(300, 300));
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
