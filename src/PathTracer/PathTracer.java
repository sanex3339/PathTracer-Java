package PathTracer;

import PathTracer.renderer.Scene;
import PathTracer.renderer.Tracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

final public class PathTracer implements Runnable {
    /**
     * Application window width
     */
    private int windowWidth = 300;

    /**
     * Application window height
     */
    private int windowHeight = 300;

    /**
     * Current sample
     */
    private int currentSample = 1;

    /**
     * 3D scene
     */
    private Scene scene;

    /**
     * Frame buffer
     */
    private List<Double> buffer = new ArrayList<>();

    private JButton renderButton;
    private RenderCanvas renderCanvas;
    private JFrame renderWindow;

    public PathTracer (int screenWidth, int screenHeight, Scene scene)  {
        this.windowWidth = screenWidth;
        this.windowHeight = screenHeight;
        this.scene = scene;
    }

    /**
     * PathTracer initializer.
     */
    public void init () {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();

        this.initBuffer();

        this.renderWindow = new JFrame("PathTracer");
        this.renderButton = new JButton("Click to render!");
        this.renderCanvas = new RenderCanvas(this.windowWidth, this.windowHeight);

        this.renderWindow.setLayout(new BorderLayout());

        this.renderButton.setSize(this.windowWidth, this.windowHeight);
        this.renderButton.addActionListener(this::renderButtonHandler);
        this.renderButton.setOpaque(false);
        this.renderButton.setContentAreaFilled(false);
        this.renderButton.setBorderPainted(false);
        this.renderButton.setFocusPainted(false);

        this.renderCanvas.setPreferredSize(
            new Dimension(this.windowWidth, this.windowHeight)
        );

        this.renderWindow.add(this.renderButton, BorderLayout.NORTH);
        this.renderWindow.add(this.renderCanvas, BorderLayout.NORTH);

        this.renderWindow.pack();

        this.renderWindow.setLocation(
            screenWidth / 2 - this.windowWidth / 2,
            screenHeight / 2 - this.windowHeight / 2
        );

        this.renderWindow.setResizable(false);
        this.renderWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.renderWindow.setVisible(true);
    }

    /**
     * Start render by executing RenderThreadsService
     */
    @Override
    public void run() {
        new RenderThreadsService(
            this::renderDataProvider,
            this::renderDataHandler
        ).run();
    }

    /**
     * Fill buffer with empty values
     */
    private void initBuffer () {
        for (int i = 0; i < this.windowWidth * this.windowHeight * 3; i++) {
            this.buffer.add(0.0);
        }
    }

    /**
     * @param event event object
     */
    private void renderButtonHandler (ActionEvent event) {
        this.renderButton.setText("Rendering...");

        Thread thread = new Thread(this, "test");
        thread.start();
    }


    /**
     * @return List<Color> Provide collection of calculated Colors object for each pixel
     */
    private Callable<List<Color>> renderDataProvider () {
        return new Tracer(this.windowWidth, this.windowHeight, this.scene);
    }

    /**
     * Get data from given thread
     *
     * @param thread
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void renderDataHandler (Future<List<Color>> thread) throws ExecutionException, InterruptedException {
        this.redrawCanvas(thread.get());
    }

    /**
     * @param colors calculated pixel colors from Tracer
     */
    private void redrawCanvas (List<Color> colors) {
        if (colors == null) {
            throw new NullPointerException("Invalid HasMap key inside `data` attribute");
        }

        List<Color> sampledColors = new ArrayList<>();

        System.out.println("Sample: " + this.currentSample);
        this.renderWindow.setTitle("Sample: " + this.currentSample);

        if (this.renderButton.isVisible()) {
            this.renderButton.setVisible(false);
        }

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
