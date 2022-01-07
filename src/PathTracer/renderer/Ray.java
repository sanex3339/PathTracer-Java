package PathTracer.renderer;

import mikera.vectorz.Vector3;

public class Ray {
    private Vector3 origin;
    private Vector3 direction;
    private int iteration = 0;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Ray(Vector3 origin, Vector3 direction, int iteration) {
        this.origin = origin;
        this.direction = direction;
        this.iteration = iteration;
    }

    /**
     * @return Vector3
     */
    public Vector3 getOrigin () {
        return this.origin;
    }

    /**
     * @return Vector3
     */
    public Vector3 getDirection () {
        return this.direction;
    }

    public int getIteration () {
        return this.iteration;
    }

    /**
     * @param distance
     * @return Vector3
     */
    public Vector3 getHitPoint (double distance) {
        return PTVector.add(this.origin, PTVector.scale(this.direction, distance));
    }

    /**
     * @param iteration
     */
    public void setIteration (int iteration) {
        this.iteration = iteration;
    }
}
