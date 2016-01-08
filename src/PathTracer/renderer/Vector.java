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

    public Map<String, Double> getCoordinates () {
        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("x", this.x);
        coordinates.put("y", this.y);
        coordinates.put("z", this.z);

        return coordinates;
    }

    public double getX () {
        return this.x;
    }

    public double getY () {
        return this.y;
    }

    public double getZ () {
        return this.z;
    }

    public double getLength () {
        return Math.sqrt(
            Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2)
        );
    }

    public static Vector scale (Vector vector, double multiplier) {
        return new Vector(
            vector.x * multiplier, vector.y * multiplier, vector.z * multiplier
        );
    }

    public static Vector add (Vector vector1, Vector vector2) {
        return new Vector(
            vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z
        );
    }

    public static Vector substract (Vector vector1, Vector vector2) {
        return new Vector(
            vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z
        );
    }

    public static double dot (Vector vector1, Vector vector2) {
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
    }

    public static Vector cross (Vector vector1, Vector vector2) {
        return new Vector(
            vector1.y * vector2.z - vector1.z * vector2.y,
            vector1.z * vector2.x - vector1.x * vector2.z,
            vector1.x * vector2.y - vector1.y * vector2.x
        );
    }

    public static Vector normalize (Vector vector) {
        return Vector.scale(vector, 1 / vector.getLength());
    }

    public static Vector pow (Vector vector, double value) {
        return new Vector(
            Math.pow(vector.y, value),
            Math.pow(vector.z, value),
            Math.pow(vector.x, value)
        );
    }

    public static Vector inverse (Vector vector) {
        return Vector.scale(vector, -1);
    }

    public static Vector reflect (Vector vector, Vector normal) {
        double f = 2 * Vector.dot(vector, normal);

        return Vector.substract(
            vector,
            Vector.scale(normal, f)
        );
    }
}
