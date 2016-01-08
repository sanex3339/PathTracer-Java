package PathTracer.renderer;

public class Material {
    private RGBColor color;
    private Emission emission = new Emission(RGBColor.BLACK);
    private double lambertCoeff = 0.5;
    private double phongCoeff = 0.5;
    private double reflectionCoeff = 0;

    Material (RGBColor color, Emission emission, double reflectionCoeff) {
        this.color = color;
        this.emission = emission;
        this.reflectionCoeff = reflectionCoeff;
    }

    Material (RGBColor color, Emission emission) {
        this.color = color;
        this.emission = emission;
    }

    public Material (RGBColor color) {
        this.color = color;
    }

    Material (RGBColor color, double reflectionCoeff) {
        this.color = color;
        this.reflectionCoeff = reflectionCoeff;
    }

    public RGBColor getColor () {
        return this.color;
    }

    public Emission getEmission () {
        return this.emission;
    }

    public double getEmissionValue () {
        return this.emission.getEmissionValue();
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

    public boolean isLightSource () {
        return this.emission.isLightSource();
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
