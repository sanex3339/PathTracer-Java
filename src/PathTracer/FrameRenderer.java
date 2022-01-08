package PathTracer;

import PathTracer.renderer.RenderResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final public class FrameRenderer extends Canvas {
    /**
     * Frame buffer
     */
    private List<Double> buffer = new ArrayList<>();

    private List<Color> averageColors;
    private List<Integer> renderTimes = new ArrayList<>();

    private int currentSample = 1;

    /**
     * Coordinate of first X pixel
     */
    private int startX;

    /**
     * Coordinate of first Y pixel
     */
    private int startY;

    /**
     * Render image width
     */
    private int imageWidth;

    /**
     * Render image height
     */
    private int imageHeight;

    public FrameRenderer (int startX, int startY, int imageWidth, int imageHeight) {
        this.startX = startX;
        this.startY = startY;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        this.initBuffer();
    }

    @Override
    public void paint (Graphics g) {
        super.paint(g);

        this.createBufferStrategy(2);
    }

    /**
     * Update canvas with calculated sample colors
     *
     * @param renderResult
     */
    public void updateFrame (RenderResult renderResult) {
        this.averageColors = this.getAverageColors(renderResult.colors());
        this.currentSample++;

        BufferStrategy bufferStrategy = this.getBufferStrategy();

        if (bufferStrategy == null) {
            this.createBufferStrategy(2);
            bufferStrategy = this.getBufferStrategy();
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        this.render(graphics);

        bufferStrategy.show();

        double averageRenderTime = this.getAverageRenderTime(renderResult.renderTime());

        System.out.println(
            "Sample: " + this.currentSample + "; " +
            "Sample render time (ms): " + renderResult.renderTime() + "; " +
            "Average render time (ms): " + averageRenderTime);
    }

    /**
     * Save current render state to jpg file
     */
    public void saveToFile () {
        BufferedImage image = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        this.render(graphics);

        graphics.dispose();

        try {
            System.out.println("Exporting image");

            ImageIO.write(image, "png", new File("render.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get average colors for each pixel from buffer
     *
     * @param colors sample colors given by calculation of one render sample
     * @return List<Color> sampled average colors
     */
    private List<Color> getAverageColors(List<Color> colors) {
        List<Color> sampledColors = new ArrayList<>();

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

        return sampledColors;
    }

    /**
     * Returns average sample time
     *
     * @param sampleRenderTime
     * @return
     */
    private double getAverageRenderTime(int sampleRenderTime) {
        this.renderTimes.add(sampleRenderTime);

        return this.renderTimes.stream().mapToInt(val -> val).average().orElse(0);
    }

    /**
     * Fill buffer with empty values
     */
    private void initBuffer () {
        for (int i = 0; i < this.imageWidth * this.imageHeight * 3; i++) {
            this.buffer.add(0.0);
        }
    }

    /**
     * render image
     */
    private void render (Graphics graphics) {
        int i = 0;

        for (int y = this.startY; y < this.startY + this.imageHeight; y++) {
            for (int x = this.startX; x < this.startX + this.imageWidth; x++) {
                graphics.setColor(this.averageColors.get(i));
                graphics.fillRect(x, y, 1, 1);

                i++;
            }
        }

        graphics.dispose();
    }
}
