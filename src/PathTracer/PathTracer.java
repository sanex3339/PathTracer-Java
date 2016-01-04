package PathTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    private void renderButtonHandler (ActionEvent event) {
        new RenderThreadsController(this::redrawCanvasCallback);
    }

    private void redrawCanvasCallback (Color color) {
        System.out.println(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());

        this.renderCanvas.update(color);
    }
}
