package PathTracer.renderer.Materials;

import PathTracer.renderer.RGBColor;
import PathTracer.renderer.Vector;

public class EmissiveMaterial extends AbstractMaterial {
    public EmissiveMaterial (RGBColor emissionColor, double intensity) {
        super(emissionColor, emissionColor, intensity);
    }

    public EmissiveMaterial (RGBColor emissionColor) {
        super(emissionColor, emissionColor, 1);
    }

    @Override
    public RGBColor getBRDF (Vector direction, Vector normal) {
        return super.getEmissionColor();
    }

    @Override
    public double getPDF (Vector direction, Vector normal) {
        return 1;
    }

    @Override
    public boolean isLightSource () {
        return true;
    }
}
