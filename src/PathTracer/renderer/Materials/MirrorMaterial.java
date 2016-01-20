package PathTracer.renderer.Materials;

import PathTracer.interfaces.ReflectiveSurface;
import PathTracer.renderer.RGBColor;
import PathTracer.renderer.Vector;

public class MirrorMaterial extends AbstractMaterial implements ReflectiveSurface {
    /**
     * Reflection coefficient 0..1
     */
    private double reflectionCoefficient = 0;

    public MirrorMaterial(RGBColor color, double reflectionCoefficient) {
        this.color = color;
        this.reflectionCoefficient = reflectionCoefficient;
    }

    /**
     * @param direction
     * @param normal
     * @return RGBColor
     */
    @Override
    public RGBColor getBRDF (Vector direction, Vector normal) {
        return this.getColor();
    }

    /**
     * @param direction
     * @param normal
     * @return double
     */
    @Override
    public double getPDF (Vector direction, Vector normal) {
        return 1;
    }

    /**
     * @return double
     */
    public double getReflectionCoefficient () {
        return this.reflectionCoefficient;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isLightSource () {
        return false;
    }
}
