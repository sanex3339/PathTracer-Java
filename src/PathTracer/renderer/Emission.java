package PathTracer.renderer;

public class Emission {
    private RGBColor emissionColor;
    private double emissionValue = 1;
    private double fadeRadius = 2500;

    public Emission (RGBColor emissionColor, double emissionValue, double fadeRadius) {
        this.emissionColor = emissionColor;
        this.emissionValue = emissionValue;
        this.fadeRadius = fadeRadius;
    }

    public Emission (RGBColor emission, double emissionValue) {
        this.emissionColor = emission;
        this.emissionValue = emissionValue;
    }

    public Emission (RGBColor emission) {
        this.emissionColor = emission;
    }

    public RGBColor getEmissionColor() {
        return this.emissionColor;
    }

    public double getEmissionValue () {
        return this.emissionValue;
    }

    public double getFadeRadius () {
        return this.fadeRadius;
    }

    public boolean isLightSource () {
        RGBColor emission = this.getEmissionColor();

        return emission.getRed() > 0 ||
            emission.getGreen() > 0 ||
            emission.getBlue() > 0;
    }
}
