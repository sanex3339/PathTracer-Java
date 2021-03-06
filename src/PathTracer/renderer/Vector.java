package PathTracer.renderer;

import java.util.HashMap;
import java.util.Map;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return Map<String, Double>
     */
    public Map<String, Double> getCoordinates () {
        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("x", this.x);
        coordinates.put("y", this.y);
        coordinates.put("z", this.z);

        return coordinates;
    }

    /**
     * @return double
     */
    public double getX () {
        return this.x;
    }

    /**
     * @return double
     */
    public double getY () {
        return this.y;
    }

    /**
     * @return double
     */
    public double getZ () {
        return this.z;
    }

    /**
     * @return double
     */
    public double getLength () {
        return Math.sqrt(
            Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2)
        );
    }

    /**
     * @param vector
     * @return boolean
     */
    public boolean equals (Vector vector) {
        if (vector == null) {
            return false;
        }

        return this.getX() == vector.getX() &&
            this.getY() == vector.getY() &&
            this.getZ() == vector.getZ();
    }

    /**
     * @param vector
     * @param multiplier
     * @return Vector
     */
    public static Vector scale (Vector vector, double multiplier) {
        return new Vector(
            vector.x * multiplier, vector.y * multiplier, vector.z * multiplier
        );
    }

    /**
     * @param vectors
     * @return Vector
     */
    public static Vector add (Vector ...vectors) {
        double x = vectors[0].getX();
        double y = vectors[0].getY();
        double z = vectors[0].getZ();

        for (int i = 1, len = vectors.length; i < len; i++) {
            x += vectors[i].getX();
            y += vectors[i].getY();
            z += vectors[i].getZ();
        }

        return new Vector(
            x, y, z
        );
    }

    /**
     * @param vectors
     * @return Vector
     */
    public static Vector substract (Vector ...vectors) {
        double x = vectors[0].getX();
        double y = vectors[0].getY();
        double z = vectors[0].getZ();

        for (int i = 1, len = vectors.length; i < len; i++) {
            x -= vectors[i].getX();
            y -= vectors[i].getY();
            z -= vectors[i].getZ();
        }

        return new Vector(
            x, y, z
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
     * @param value
     * @return Vector
     */
    public static Vector pow (Vector vector, double value) {
        return new Vector(
            Math.pow(vector.y, value),
            Math.pow(vector.z, value),
            Math.pow(vector.x, value)
        );
    }

    /**
     * @param vector
     * @return Vector
     */
    public static Vector inverse (Vector vector) {
        return Vector.scale(vector, -1);
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
