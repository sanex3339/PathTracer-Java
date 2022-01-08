package PathTracer.renderer;

public class Vector {
    public double x;
    public double y;
    public double z;
    private Double length = null;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return double
     */
    public double getLength () {
        if (this.length != null) {
            return this.length;
        }

        this.length = Math.sqrt(
            Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2)
        );

        return this.length;
    }

    /**
     * @param vector
     * @param multiplier
     * @return Vector
     */
    public static Vector scale (Vector vector, double multiplier) {
        return new Vector(
            vector.x * multiplier,
            vector.y * multiplier,
            vector.z * multiplier
        );
    }

    /**
     * @param vector1
     * @param vector2
     * @return Vector
     */
    public static Vector add (Vector vector1, Vector vector2) {
        return new Vector(
            vector1.x + vector2.x,
            vector1.y + vector2.y,
            vector1.z + vector2.z
        );
    }

    /**
     * @param vectors
     * @return Vector
     */
    public static Vector add (Vector ...vectors) {
        double x = vectors[0].x;
        double y = vectors[0].y;
        double z = vectors[0].z;

        for (int i = 1, len = vectors.length; i < len; i++) {
            x += vectors[i].x;
            y += vectors[i].y;
            z += vectors[i].z;
        }

        return new Vector(
            x, y, z
        );
    }

    /**
     * @param vector1
     * @param vector2
     * @return Vector
     */
    public static Vector substract (Vector vector1, Vector vector2) {
        return new Vector(
            vector1.x - vector2.x,
            vector1.y - vector2.y,
            vector1.z - vector2.z
        );
    }

    /**
     * @param vector1
     * @param vector2
     * @return double
     */
    public static double dot (Vector vector1, Vector vector2) {
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
    }

    /**
     * @param vector1
     * @param vector2
     * @return Vector
     */
    public static Vector cross (Vector vector1, Vector vector2) {
        return new Vector(
            vector1.y * vector2.z - vector1.z * vector2.y,
            vector1.z * vector2.x - vector1.x * vector2.z,
            vector1.x * vector2.y - vector1.y * vector2.x
        );
    }

    /**
     * @param vector
     * @return Vector
     */
    public static Vector normalize (Vector vector) {
        return Vector.scale(vector, 1 / vector.getLength());
    }

    /**
     * @param vector
     * @param normal
     * @return Vector
     */
    public static Vector reflect (Vector vector, Vector normal) {
        double f = 2 * Vector.dot(vector, normal);

        return Vector.substract(
            vector,
            Vector.scale(normal, f)
        );
    }
}
