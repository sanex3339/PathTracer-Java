package PathTracer.interfaces;

public interface RefractiveSurface extends BaseSurface {
    double getRefractionCoefficient ();
    double getIOR ();
}
