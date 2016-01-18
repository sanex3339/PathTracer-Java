package PathTracer.renderer.Materials;

import PathTracer.renderer.RGBColor;
import PathTracer.renderer.Vector;

public class DiffuseMaterial extends AbstractMaterial {
    public static DiffuseMaterial BASE_MATERIAL = new DiffuseMaterial(RGBColor.GRAY);

    public DiffuseMaterial (RGBColor color) {
        super(color);
    }

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

    @Override
    public double getPDF (Vector direction, Vector normal) {
        double cosTheta = Vector.dot(
            direction,
            normal
        );

        return cosTheta / Math.PI;
    }
}
