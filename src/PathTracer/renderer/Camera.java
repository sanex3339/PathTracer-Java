package PathTracer.renderer;

public class Camera {
    private Vector direction;
    private Vector position;
    private Vector forwardVector;
    private Vector rightVector;
    private Vector upVector;
    private int screenWidth;
    private int screenHeight;

    public Camera (Vector position, Vector direction, int screenWidth, int screenHeight) {
        this.position = position;
        this.direction = direction;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.forwardVector = Vector.normalize(Vector.substract(this.direction, this.position));
        this.rightVector = Vector.scale(
            Vector.normalize(
                Vector.cross(
                    this.forwardVector,
                    new Vector(0, -1, 0)
                )
            ),
            3
        );
        this.upVector = Vector.scale(
            Vector.normalize(
                Vector.cross(
                    this.forwardVector,
                    this.rightVector
                )
            ),
            3
        );
    }

    public Vector getDirection () {
        return this.direction;
    }

    public Vector getPosition () {
        return this.position;
    }

    public Vector getForwardVector () {
        return this.forwardVector;
    }

    public Vector  getRightVector () {
        return this.rightVector;
    }

    public Vector getUpVector () {
        return this.upVector;
    }

    public double recenterX (double x) {
        double aspectCoeff = (this.screenHeight / this.screenWidth) * 2;

        return (x - (this.screenWidth / 2)) / aspectCoeff / this.screenWidth;
    }

    public double recenterY (double y) {
        return -(y - (this.screenHeight / 2)) / 2 / this.screenHeight;
    }
}
