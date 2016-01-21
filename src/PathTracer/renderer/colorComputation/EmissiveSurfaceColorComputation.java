package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.ColorComputation;
import PathTracer.interfaces.EmissiveSurface;
import PathTracer.renderer.RGBColor;

public class EmissiveSurfaceColorComputation <T extends EmissiveSurface> implements ColorComputation {
    private T material;

    public EmissiveSurfaceColorComputation(T material) {
        this.material = material;
    }

    /**
     * @return RGBColor
     */
    public RGBColor calculateColor () {
        return this.material.getEmissionColor();
    }
}
