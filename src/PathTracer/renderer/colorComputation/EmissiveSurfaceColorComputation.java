package PathTracer.renderer.colorComputation;

import PathTracer.interfaces.ColorComputation;
import PathTracer.interfaces.EmissiveSurface;
import PathTracer.renderer.PTColor;

public class EmissiveSurfaceColorComputation <T extends EmissiveSurface> implements ColorComputation {
    private T material;

    public EmissiveSurfaceColorComputation(T material) {
        this.material = material;
    }

    /**
     * @return PTColor
     */
    public PTColor calculateColor () {
        return this.material
            .getEmissionColor();
    }
}
