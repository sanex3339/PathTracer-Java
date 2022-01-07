package PathTracer.renderer;

import mikera.vectorz.Vector3;

public class PTVector {
    private Vector3 vector;

    public PTVector(double x, double y, double z) {
        this.vector = Vector3.of(x, y, z);
    }

    public Vector3 getVector () {
        return this.vector;
    }

    /**
     * @param vector
     * @param multiplier
     * @return Vector3
     */
    public static Vector3 scale (Vector3 vector, double multiplier) {
        return vector.multiplyCopy(multiplier);
    }

    /**
     * @param vector1
     * @param vector2
     * @return Vector33
     */
    public static Vector3 add (Vector3 vector1, Vector3 vector2) {
        return vector1.addCopy(vector2);
    }

    /**
     * @param vectors
     * @return Vector3
     */
    public static Vector3 add (Vector3... vectors) {
        Vector3 vector = vectors[0].clone();

        for (int i = 1, len = vectors.length; i < len; i++) {
            vector.add(vectors[i]);
        }

        return vector;
    }

    /**
     * @param vector1
     * @param vector2
     * @return Vector3
     */
    public static Vector3 substract (Vector3 vector1, Vector3 vector2) {
        Vector3 vector = vector1.clone();

        vector.sub(vector2);

        return vector;
    }

    /**
     * @param vector1
     * @param vector2
     * @return double
     */
    public static double dot (Vector3 vector1, Vector3 vector2) {
        return vector1.dotProduct(vector2);
    }

    /**
     * @param vector1
     * @param vector2
     * @return Vector3
     */
    public static Vector3 cross (Vector3 vector1, Vector3 vector2) {
        Vector3 vector = vector1.clone();

        vector.crossProduct(vector2);

        return vector;
    }

    /**
     * @param vector
     * @return Vector3
     */
    public static Vector3 normalize (Vector3 vector) {
        Vector3 normalizedVector = vector.clone();

        normalizedVector.normalise();

        return normalizedVector;
    }

    /**
     * @param vector
     * @param normal
     * @return Vector3
     */
    public static Vector3 reflect (Vector3 vector, Vector3 normal) {
        Vector3 clonedVector = vector.clone();

        double f = 2 * PTVector.dot(clonedVector, normal);

        return PTVector.substract(
            vector,
            PTVector.scale(normal, f)
        );
    }
}
