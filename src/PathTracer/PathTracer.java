package PathTracer;

import PathTracer.interfaces.ClickListener;
import PathTracer.renderer.Scene;
import PathTracer.renderer.Tracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
     * 3D scene
     */
    private Scene scene;

    private JFrame renderWindow;
    private SaveMenu saveMenu;
    private JButton renderButton;
    private RenderCanvas renderCanvas;

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

        this.renderWindow = new JFrame("PathTracer");
        this.renderButton = new JButton("Click to render!");
        this.renderCanvas = new RenderCanvas(this.windowWidth, this.windowHeight);
        this.saveMenu = new SaveMenu("SaveMenu");

        this.renderWindow.setLayout(new BorderLayout());

        this.renderButton.setSize(this.windowWidth, this.windowHeight);
        this.renderButton.addActionListener(this::renderButtonHandler);
        this.renderButton.setOpaque(false);
        this.renderButton.setContentAreaFilled(false);
        this.renderButton.setBorderPainted(false);
        this.renderButton.setFocusPainted(false);

        this.renderCanvas.addMouseListener((ClickListener) this::rightClickHandler);
        this.renderCanvas.setPreferredSize(
            new Dimension(this.windowWidth, this.windowHeight)
        );

        this.saveMenu.initMenu();
        this.saveMenu
            .getSaveImageItem()
            .addMouseListener((ClickListener)
                (e) -> this.renderCanvas.saveToFile()
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
     * @param event event object
     */
    private void renderButtonHandler (ActionEvent event) {
        this.renderButton.setText("Rendering...");

        Thread thread = new Thread(this, "RenderThread");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    /**
     * RenderCanvas right-click handler
     *
     * @param event MouseEvent
     */
    private void rightClickHandler(MouseEvent event) {
        if (!SwingUtilities.isRightMouseButton(event)) {
            return;
        }

        this.saveMenu.show(event.getComponent(), event.getX(), event.getY());
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
     * @param thread ExecutorService thread
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
        if (this.renderButton.isVisible()) {
            this.renderButton.setVisible(false);
        }

        this.renderCanvas.update(colors);
    }
}
