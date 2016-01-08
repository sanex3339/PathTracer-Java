package PathTracer.renderer;

public class RGBColor {
    private int red;
    private int green;
    private int blue;

    public RGBColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public RGBColor(double red, double green, double blue) {
        this.red = (int) red;
        this.green = (int) green;
        this.blue = (int) blue;
    }

    public RGBColor(float red, float green, float blue) {
        this.red = (int) red;
        this.green = (int) green;
        this.blue = (int) blue;
    }

    public int getRed () {
        return this.red;
    }

    public int getGreen () {
        return this.green;
    }

    public int getBlue () {
        return this.blue;
    }

    public RGBColor add (RGBColor color) {
        return new RGBColor(
            this.getRed() + color.getRed(),
            this.getGreen() + color.getGreen(),
            this.getBlue() + color.getBlue()
        );
    }

    public RGBColor substract (RGBColor color) {
        return new RGBColor(
            this.getRed() - color.getRed(),
            this.getGreen() - color.getGreen(),
            this.getBlue() - color.getBlue()
        );
    }

    public RGBColor scale(double multiplier) {
        return new RGBColor(
            (int) (this.getRed() * multiplier),
            (int) (this.getGreen() * multiplier),
            (int) (this.getBlue() * multiplier)
        );
    }

    public RGBColor multiple (RGBColor color) {
        return new RGBColor(
            this.getRed() * color.getRed(),
            this.getGreen() * color.getGreen(),
            this.getBlue() * color.getBlue()
        );
    }

    public RGBColor divide (double value) {
        return new RGBColor(
            (int) (this.getRed() / value),
            (int) (this.getGreen() / value),
            (int) (this.getBlue() / value)
        );
    }


    public static int clampColor (int color) {
        return color > 255 ? 255 : color;
    }
}
