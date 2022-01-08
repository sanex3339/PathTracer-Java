package PathTracer.renderer;

public class RGBColor {
    private int r;
    private int g;
    private int b;

    public static RGBColor BLACK = new RGBColor(0, 0, 0);
    public static RGBColor black = RGBColor.BLACK;
    public static RGBColor WHITE = new RGBColor(255, 255, 255);
    public static RGBColor white = RGBColor.WHITE;
    public static RGBColor GRAY = new RGBColor(115, 115, 115);
    public static RGBColor gray = RGBColor.GRAY;
    public static RGBColor RED = new RGBColor(255, 0, 0);
    public static RGBColor red = RGBColor.RED;
    public static RGBColor GREEN = new RGBColor(0, 255, 0);
    public static RGBColor green = RGBColor.GREEN;
    public static RGBColor BLUE = new RGBColor(0, 0, 255);
    public static RGBColor blue = RGBColor.BLUE;

    public RGBColor(PTColor ptColor) {
        this.r = (int) (ptColor.getRed() * 255);
        this.g = (int) (ptColor.getGreen() * 255);
        this.b = (int) (ptColor.getBlue() * 255);
    }

    public RGBColor(double red, double green, double blue) {
        this.r = (int) red;
        this.g = (int) green;
        this.b = (int) blue;
    }

    /**
     * @return int
     */
    public int getRed () {
        return this.r;
    }

    /**
     * @return int
     */
    public int getGreen () {
        return this.g;
    }

    /**
     * @return int
     */
    public int getBlue () {
        return this.b;
    }

    /**
     * @param color
     * @return RGBColor
     */
    public RGBColor add (RGBColor color) {
        return new RGBColor(
            this.getRed() + color.getRed(),
            this.getGreen() + color.getGreen(),
            this.getBlue() + color.getBlue()
        );
    }

    /**
     * @param color
     * @return RGBColor
     */
    public RGBColor substract (RGBColor color) {
        return new RGBColor(
            this.getRed() - color.getRed(),
            this.getGreen() - color.getGreen(),
            this.getBlue() - color.getBlue()
        );
    }

    /**
     * @param multiplier
     * @return RGBColor
     */
    public RGBColor scale(double multiplier) {
        return new RGBColor(
            (int) (this.getRed() * multiplier),
            (int) (this.getGreen() * multiplier),
            (int) (this.getBlue() * multiplier)
        );
    }

    /**
     * @param color
     * @return RGBColor
     */
    public RGBColor multiple (RGBColor color) {
        return new RGBColor(
            this.getRed() * color.getRed(),
            this.getGreen() * color.getGreen(),
            this.getBlue() * color.getBlue()
        );
    }

    /**
     * @param value
     * @return RGBColor
     */
    public RGBColor divide (double value) {
        return new RGBColor(
            (int) (this.getRed() / value),
            (int) (this.getGreen() / value),
            (int) (this.getBlue() / value)
        );
    }

    /**
     * @param color
     * @return RGBColor
     */
    public RGBColor filter (RGBColor color) {
        return new RGBColor(
            this.getRed() * (color.getRed() / 255.0),
            this.getGreen() * (color.getGreen() / 255.0),
            this.getBlue() * (color.getBlue() / 255.0)
        );
    }

    /**
     * @param color
     * @return boolean
     */
    public boolean equals (RGBColor color) {
        if (color == null) {
            return false;
        }

        return this.getRed() == color.getRed() &&
            this.getGreen() == color.getGreen() &&
            this.getBlue() == color.getBlue();
    }

    /**
     * @param color
     * @return PTColor
     */
    public static RGBColor clampRGBColor (RGBColor color) {
        if (color.getRed() > 255) {
            color.setR(255);
        }

        if (color.getRed() < 0) {
            color.setR(0);
        }

        if (color.getGreen() > 255) {
            color.setG(255);
        }

        if (color.getGreen() < 0) {
            color.setG(0);
        }

        if (color.getBlue() > 255) {
            color.setB(255);
        }

        if (color.getBlue() < 0) {
            color.setB(0);
        }

        return color;
    }

    /**
     * @param value
     */
    private void setR(int value) {
        this.r = value;
    }

    /**
     * @param value
     */
    private void setG(int value) {
        this.g = value;
    }

    /**
     * @param value
     */
    private void setB(int value) {
        this.b = value;
    }
}
