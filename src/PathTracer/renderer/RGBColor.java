package PathTracer.renderer;

public class RGBColor {
    private int r;
    private int g;
    private int b;

    public static RGBColor BLACK = new RGBColor(0, 0, 0);
    public static RGBColor black = RGBColor.BLACK;
    public static RGBColor GRAY = new RGBColor(115, 115, 115);
    public static RGBColor gray = RGBColor.GRAY;
    public static RGBColor RED = new RGBColor(255, 0, 0);
    public static RGBColor red = RGBColor.RED;
    public static RGBColor GREEN = new RGBColor(0, 255, 0);
    public static RGBColor green = RGBColor.GREEN;
    public static RGBColor BLUE = new RGBColor(0, 0, 255);
    public static RGBColor blue = RGBColor.BLUE;

    public RGBColor(int red, int green, int blue) {
        this.r = red;
        this.g = green;
        this.b = blue;
    }

    public RGBColor(double red, double green, double blue) {
        this.r = (int) red;
        this.g = (int) green;
        this.b = (int) blue;
    }

    public RGBColor(float red, float green, float blue) {
        this.r = (int) red;
        this.g = (int) green;
        this.b = (int) blue;
    }

    public int getRed () {
        return this.r;
    }

    public int getGreen () {
        return this.g;
    }

    public int getBlue () {
        return this.b;
    }

    public RGBColor add (RGBColor color) {
        return RGBColor.clampRGBColor(
            new RGBColor(
                this.getRed() + color.getRed(),
                this.getGreen() + color.getGreen(),
                this.getBlue() + color.getBlue()
            )
        );
    }

    public RGBColor substract (RGBColor color) {
        return RGBColor.clampRGBColor(
            new RGBColor(
                this.getRed() - color.getRed(),
                this.getGreen() - color.getGreen(),
                this.getBlue() - color.getBlue()
            )
        );
    }

    public RGBColor scale(double multiplier) {
        return RGBColor.clampRGBColor(
            new RGBColor(
                (int) (this.getRed() * multiplier),
                (int) (this.getGreen() * multiplier),
                (int) (this.getBlue() * multiplier)
            )
        );
    }

    public RGBColor multiple (RGBColor color) {
        return RGBColor.clampRGBColor(
            new RGBColor(
                this.getRed() * color.getRed(),
                this.getGreen() * color.getGreen(),
                this.getBlue() * color.getBlue()
            )
        );
    }

    public RGBColor divide (double value) {
        return RGBColor.clampRGBColor(
            new RGBColor(
                (int) (this.getRed() / value),
                (int) (this.getGreen() / value),
                (int) (this.getBlue() / value)
            )
        );
    }

    public RGBColor filter (RGBColor color) {
        return RGBColor.clampRGBColor(
            new RGBColor(
                this.getRed() * (color.getRed() / 255.0),
                this.getGreen() * (color.getGreen() / 255.0),
                this.getBlue() * (color.getBlue() / 255.0)
            )
        );
    }

    public boolean equals (RGBColor color) {
        if (color == null) {
            return false;
        }

        return this.getRed() == color.getRed() &&
            this.getGreen() == color.getGreen() &&
            this.getBlue() == color.getBlue();
    }

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

    private void setR(int value) {
        this.r = value;
    }

    private void setG(int value) {
        this.g = value;
    }

    private void setB(int value) {
        this.b = value;
    }
}
