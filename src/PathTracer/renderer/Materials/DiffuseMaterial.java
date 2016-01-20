package PathTracer.renderer.Materials;

import PathTracer.renderer.RGBColor;
import PathTracer.renderer.Vector;

public class DiffuseMaterial extends AbstractMaterial {
    public DiffuseMaterial (RGBColor color) {
        super(color);
    }

    /**
     * @param direction
     * @param normal
     * @return RGBColor
     */
    @Override
    public RGBColor getBRDF (Vector direction, Vector normal) {
        double cosTheta = Vector.dot(
            direction,
            normal
        );

        return this
            .getColor()
            .scale(cosTheta)
            .divide(Math.PI);
    }

    /**
     * @param direction
     * @param normal
     * @return double
     */
    @Override
    public double getPDF (Vector direction, Vector normal) {
        double cosTheta = Vector.dot(
            direction,
            normal
        );

        return cosTheta / Math.PI;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isLightSource () {
        return false;
    }
}
