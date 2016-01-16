package PathTracer.renderer;

public class Emission {
    private RGBColor emissionColor;
    private double emissionValue = 1360;

    public Emission (RGBColor emissionColor, double emissionValue) {
        this.emissionColor = emissionColor;
        this.emissionValue = emissionValue;
    }

    public Emission (RGBColor emission) {
        this.emissionColor = emission;
    }

    public RGBColor getEmissionColor() {
        return this.emissionColor;
    }

    public double getEmissionValue () {
        return this.emissionValue * 255;
    }

    public boolean isLightSource () {
        RGBColor emission = this.getEmissionColor();

        return emission.getRed() > 0 ||
            emission.getGreen() > 0 ||
            emission.getBlue() > 0;
    }
}
