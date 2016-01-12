package PathTracer;

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
    private int currentSample = 1;
    private int imageWidth;
    private int imageHeight;

    public FrameRenderer (int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        this.setSize(new Dimension(imageWidth, imageHeight));
        this.initBuffer();
    }

    @Override
    public void paint (Graphics g) {
        super.paint(g);

        this.createBufferStrategy(2);
    }

    /**
     * Update canvas with calculated sample colors
     */
    public void updateFrame (List<Color> sampleColors) {
        System.out.println("Sample: " + this.currentSample);

        this.averageColors = this.getAverageColors(sampleColors);
        this.currentSample++;

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
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        int i = 0;

        for (int y = 0; y < this.imageHeight; y++) {
            for (int x = 0; x < this.imageWidth; x++) {
                graphics.setColor(this.averageColors.get(i));
                graphics.fillRect(x, y, 1, 1);

                i++;
            }
        }

        graphics.dispose();
        bufferStrategy.show();
    }
}
