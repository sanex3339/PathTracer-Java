package PathTracer.renderer;

public class Material {
    private RGBColor color;
    private RGBColor emission = new RGBColor(0, 0, 0);
    private double lambertCoeff = 0.5;
    private double phongCoeff = 0.5;
    private double reflectionCoeff = 0;

    Material (RGBColor color, RGBColor emission, double reflectionCoeff) {
        this.color = color;
        this.emission = emission;
        this.reflectionCoeff = reflectionCoeff;
    }

    Material (RGBColor color, RGBColor emission) {
        this.color = color;
        this.emission = emission;
    }

    public Material (RGBColor color) {
        this.color = color;
    }

    public RGBColor getColor () {
        return this.color;
    }

    public RGBColor getEmission () {
        return this.emission;
    }

    public double getEmissionValue () {
        return (
            this.emission.getRed() + this.emission.getGreen() + this.emission.getBlue()
        ) / 3;
    }

    public double getLambertCoeff () {
        return this.lambertCoeff;
    }

    public double getPhongCoeff () {
        return this.phongCoeff;
    }

    public double getReflectionCoeff () {
        return this.reflectionCoeff;
    }

    public Material setLambertCoeff (double lambertCoeff) {
        this.lambertCoeff = lambertCoeff;
        this.phongCoeff = 1 - lambertCoeff;

        return this;
    }

    public Material setPhongCoeff (double phongCoeff) {
        this.phongCoeff = phongCoeff;
        this.lambertCoeff = 1 - phongCoeff;

        return this;
    }
}
