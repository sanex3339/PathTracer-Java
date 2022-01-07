package PathTracer.renderer;

import mikera.vectorz.Vector3;

public class Camera {
    private Vector3 direction;
    private Vector3 position;
    private Vector3 forwardVector;
    private Vector3 rightVector;
    private Vector3 upVector;
    private double zoom = 1.5;
    private double screenWidth;
    private double screenHeight;

    public Camera (Vector3 position, Vector3 direction, int screenWidth, int screenHeight) {
        this.position = position;
        this.direction = direction;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.forwardVector = PTVector.normalize(PTVector.substract(this.direction, this.position));
        this.rightVector = PTVector
            .scale(
                PTVector.normalize(
                    PTVector.cross(
                        this.forwardVector,
                        new PTVector(0, -1, 0).getVector()
                    )
                ),
                this.zoom
            );
        this.upVector = PTVector
            .scale(
                PTVector.normalize(
                    PTVector.cross(
                        this.forwardVector,
                        this.rightVector
                    )
                ),
                this.zoom
            );
    }

    /**
     * @return Vector3
     */
    public Vector3 getDirection () {
        return this.direction;
    }

    /**
     * @param x
     * @param y
     * @return Vector3
     */
    public Vector3 getPerspectiveVector (double x, double y) {
        return PTVector.normalize(
            PTVector.add(
                this.getForwardVector(),
                PTVector.scale(
                    this.getRightVector(),
                    this.recenterX(x)
                ),
                PTVector.scale(
                    this.getUpVector(),
                    this.recenterY(y)
                )
            )
        );
    }

    /**
     * @return Vector3
     */
    public Vector3 getPosition () {
        return this.position;
    }

    /**
     * @return Vector3
     */
    private Vector3 getForwardVector () {
        return this.forwardVector;
    }

    /**
     * @return Vector3
     */
    private Vector3  getRightVector () {
        return this.rightVector;
    }

    /**
     * @return Vector3
     */
    private Vector3 getUpVector () {
        return this.upVector;
    }

    /**
     * @param x
     * @return double
     */
    private double recenterX (double x) {
        double aspectCoefficient = (this.screenHeight / this.screenWidth) * 2;

        return (x - (this.screenWidth / 2)) / aspectCoefficient / this.screenWidth;
    }

    /**
     * @param y
     * @return double
     */
    private double recenterY (double y) {
        return -(y - (this.screenHeight / 2)) / 2 / this.screenHeight;
    }
}
