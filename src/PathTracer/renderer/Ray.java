package PathTracer.renderer;

public class Ray {
    private Vector origin;
    private Vector direction;
    private int iteration = 0;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Ray(Vector origin, Vector direction, int iteration) {
        this.origin = origin;
        this.direction = direction;
        this.iteration = iteration;
    }

    /**
     * @return Vector
     */
    public Vector getOrigin () {
        return this.origin;
    }

    /**
     * @return Vector
     */
    public Vector getDirection () {
        return this.direction;
    }

    public int getIteration () {
        return this.iteration;
    }

    /**
     * @param distance
     * @return Vector
     */
    public Vector getHitPoint (double distance) {
        return Vector.add(this.origin, Vector.scale(this.direction, distance));
    }

    /**
     * @param iteration
     */
    public void setIteration (int iteration) {
        this.iteration = iteration;
    }
}
