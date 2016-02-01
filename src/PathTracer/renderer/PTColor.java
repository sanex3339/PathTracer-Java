package PathTracer.renderer;

public class PTColor {
    private double r;
    private double g;
    private double b;

    public static PTColor BLACK = new PTColor(0, 0, 0);
    public static PTColor black = PTColor.BLACK;
    public static PTColor WHITE = new PTColor(1, 1, 1);
    public static PTColor white = PTColor.WHITE;
    public static PTColor GRAY = new PTColor(0.5, 0.5, 0.5);
    public static PTColor gray = PTColor.GRAY;
    public static PTColor RED = new PTColor(1, 0, 0);
    public static PTColor red = PTColor.RED;
    public static PTColor GREEN = new PTColor(0, 1, 0);
    public static PTColor green = PTColor.GREEN;
    public static PTColor BLUE = new PTColor(0, 0, 1);
    public static PTColor blue = PTColor.BLUE;

    public PTColor(RGBColor rgbColor) {
        this.r = (double) rgbColor.getRed() / 255;
        this.g = (double) rgbColor.getGreen() / 255;
        this.b = (double) rgbColor.getBlue() / 255;
    }

    public PTColor(double red, double green, double blue) {
        this.r = red;
        this.g = green;
        this.b = blue;
    }

    /**
     * @return double
     */
    public double getRed () {
        return this.r;
    }

    /**
     * @return double
     */
    public double getGreen () {
        return this.g;
    }

    /**
     * @return double
     */
    public double getBlue () {
        return this.b;
    }

    /**
     * @param color
     * @return PTColor
     */
    public PTColor add (PTColor color) {
        return new PTColor(
            this.getRed() + color.getRed(),
            this.getGreen() + color.getGreen(),
            this.getBlue() + color.getBlue()
        );
    }

    /**
     * @param color
     * @return PTColor
     */
    public PTColor substract (PTColor color) {
        return new PTColor(
            this.getRed() - color.getRed(),
            this.getGreen() - color.getGreen(),
            this.getBlue() - color.getBlue()
        );
    }

    /**
     * @param multiplier
     * @return PTColor
     */
    public PTColor scale(double multiplier) {
        return new PTColor(
            this.getRed() * multiplier,
            this.getGreen() * multiplier,
            this.getBlue() * multiplier
        );
    }

    /**
     * @param color
     * @return PTColor
     */
    public PTColor multiple (PTColor color) {
        return new PTColor(
            this.getRed() * color.getRed(),
            this.getGreen() * color.getGreen(),
            this.getBlue() * color.getBlue()
        );
    }

    /**
     * @param value
     * @return PTColor
     */
    public PTColor divide (double value) {
        return new PTColor(
            this.getRed() / value,
            this.getGreen() / value,
            this.getBlue() / value
        );
    }

    /**
     * @param color
     * @return boolean
     */
    public boolean equals (PTColor color) {
        if (color == null) {
            return false;
        }

        return this.getRed() == color.getRed() &&
            this.getGreen() == color.getGreen() &&
            this.getBlue() == color.getBlue();
    }

    /**
     * @param value
     */
    private void setR(double value) {
        this.r = value;
    }

    /**
     * @param value
     */
    private void setG(double value) {
        this.g = value;
    }

    /**
     * @param value
     */
    private void setB(double value) {
        this.b = value;
    }
}
