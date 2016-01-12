package PathTracer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FrameRenderer {
    private RenderCanvas renderCanvas;

    /**
     * Frame buffer
     */
    private List<Double> buffer = new ArrayList<>();

    /**
     * Current sample
     */
    private int currentSample = 1;

    private int imageWidth;
    private int imageHeight;
    private List<Color> colors;

    FrameRenderer (RenderCanvas renderCanvas) {
        this.renderCanvas = renderCanvas;

        this.imageWidth = this.renderCanvas.getWidth();
        this.imageHeight = this.renderCanvas.getHeight();
        this.initBuffer();
    }

    public void drawColors (List<Color> colors) {
        System.out.println("Sample: " + this.currentSample);

        this.colors = this.getSampledColors(colors);

        this.render();
    }

    public void saveToFile () {
        BufferedImage image = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        this.render();
        graphics.dispose();

        try {
            System.out.println("Exporting image");

            ImageIO.write(image, "jpg", new File("render.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get average colors for each pixel from buffer
     *
     * @param colors colors given by calculation of one render sample
     * @return List<Color> sampled colors
     */
    private List<Color> getSampledColors (List<Color> colors) {
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

        this.currentSample++;

        return sampledColors;
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
    private void render () {
        BufferStrategy bufferStrategy = this.renderCanvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        int i = 0;

        for (int y = 0; y < this.imageHeight; y++) {
            for (int x = 0; x < this.imageWidth; x++) {
                graphics.setColor(colors.get(i));
                graphics.fillRect(x, y, 1, 1);

                i++;
            }
        }

        graphics.dispose();
        bufferStrategy.show();
    }
}
