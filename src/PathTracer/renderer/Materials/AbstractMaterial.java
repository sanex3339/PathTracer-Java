package PathTracer.renderer.Materials;

import PathTracer.renderer.RGBColor;
import PathTracer.renderer.Vector;

public abstract class AbstractMaterial {
    /**
     * RGB color 0..255
     */
    private RGBColor color = RGBColor.GRAY;

    /**
     * RGB emission color 0..255
     */
    private RGBColor emissionColor = RGBColor.BLACK;

    /**
     * Light intensity
     */
    private double intensity = 0;

    /**
     * Reflection coefficient 0..1
     */
    private double reflectionCoefficient = 0;

    /**
     * @param direction
     * @param normal
     * @return RGBColor - material BRDF
     */
    public abstract RGBColor getBRDF (Vector direction, Vector normal);

    /**
     * @param direction
     * @param normal
     * @return double - material PDF
     */
    public abstract double getPDF (Vector direction, Vector normal);

    public AbstractMaterial (RGBColor color, RGBColor emissionColor, double intensity) {
        this.color = color;
        this.emissionColor = emissionColor;
        this.intensity = intensity;
    }

    public AbstractMaterial (RGBColor color, double reflectionCoefficient) {
        this.color = color;
        this.reflectionCoefficient = reflectionCoefficient;
    }

    public AbstractMaterial (RGBColor color) {
        this.color = color;
    }

    public RGBColor getColor () {
        return this.color;
    }

    public RGBColor getEmissionColor () {
        return this.emissionColor;
    }

    public double getIntensity () {
        return this.intensity;
    }

    public double getReflectionCoefficient () {
        return this.reflectionCoefficient;
    }

    public boolean isLightSource () {
        return false;
    }
}
