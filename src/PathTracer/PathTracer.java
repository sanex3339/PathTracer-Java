package PathTracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

final public class PathTracer {
    public int windowWidth = 300;
    public int windowHeight = 300;
    public int currentSample = 1;

    private List<Double> buffer = new ArrayList<>();

    private JButton renderButton;
    private RenderCanvas renderCanvas;
    private JFrame renderWindow;

    public PathTracer (int screenWidth, int screenHeight)  {
        this.windowWidth = screenWidth;
        this.windowHeight = screenHeight;

        this.renderWindow = new JFrame("PathTracer");
        this.renderButton = new JButton("Start Render");
        this.renderCanvas = new RenderCanvas(this.windowWidth, this.windowHeight);

        for (int i = 0; i < this.windowWidth * this.windowHeight * 3; i++) {
            this.buffer.add(0.0);
        }
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
        new RenderThreadsController(
            this.windowWidth,
            this.windowHeight,
            this::redrawCanvasCallback
        );
    }

    /**
     * @param colors
     */
    private void redrawCanvasCallback (List<Color> colors) {
        if (colors == null) {
            throw new NullPointerException("Invalid HasMap key inside `data` attribute");
        }

        List<Color> sampledColors = new ArrayList<>();

        System.out.println(this.currentSample);

        for (int i = 0; i < colors.size(); i++) {
            int redIndex = i * 3;
            int greenIndex = i * 3 + 1;
            int blueIndex = i * 3 + 2;

            this.buffer.set(
                redIndex,
                this.buffer.get(redIndex) + colors.get(i).getRed()
            );
            this.buffer.set(
                greenIndex,
                this.buffer.get(greenIndex) + colors.get(i).getGreen()
            );
            this.buffer.set(
                blueIndex,
                this.buffer.get(blueIndex) + colors.get(i).getBlue()
            );

            sampledColors.add(new Color(
                (int) (this.buffer.get(redIndex) / this.currentSample),
                (int) (this.buffer.get(greenIndex) / this.currentSample),
                (int) (this.buffer.get(blueIndex) / this.currentSample)
            ));
        }

        this.currentSample++;

        this.renderCanvas.draw(sampledColors);
    }
}
