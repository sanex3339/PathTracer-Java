package PathTracer.renderer.Materials;

import PathTracer.renderer.RGBColor;
import PathTracer.renderer.Vector;

public class MirrorMaterial extends AbstractMaterial {
    public MirrorMaterial (RGBColor color, double reflectionCoefficient) {
        super(color, reflectionCoefficient);
    }

    @Override
    public RGBColor getBRDF (Vector direction, Vector normal) {
        return RGBColor.BLACK;
    }

    @Override
    public double getPDF (Vector direction, Vector normal) {
        return 1;
    }
}
