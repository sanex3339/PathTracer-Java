package PathTracer.renderer;

public class Ray {
    private Vector origin;
    private Vector direction;
    private int iteration = 0;

    Ray (Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    Ray (Vector origin, Vector direction, int iteration) {
        this.origin = origin;
        this.direction = direction;
        this.iteration = iteration;
    }

    public Vector getOrigin () {
        return this.origin;
    }

    public Vector getDirection () {
        return this.direction;
    }

    public int getIteration () {
        return this.iteration;
    }

    public Vector getHitPoin (double distance) {
        return Vector.add(this.origin, Vector.scale(this.direction, distance));
    }

    public void setIteration (int iteration) {
        this.iteration = iteration;
    }
}
