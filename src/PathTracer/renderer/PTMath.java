package PathTracer.renderer;

import mikera.vectorz.Vector3;

public final class PTMath {
    public final static double EPSILON = 0.001;

    /**
     * Get random point on hemisphere
     *
     * @param normal
     * @return Vector3
     */
    public static Vector3 cosineSampleHemisphere (Vector3 normal) {
        double u = RandomGenerator.getRandomDouble();
        double v = RandomGenerator.getRandomDouble();
        double r = Math.sqrt(u);
        double angle = 2 * Math.PI * v;
        Vector3 sDir;
        Vector3 tDir;

        if (Math.abs(normal.getX()) < 0.5) {
            sDir = PTVector.cross(normal, new PTVector(1,0,0).getVector());
        } else {
            sDir = PTVector.cross(normal, new PTVector(0,1,0).getVector());
        }

        tDir = PTVector.cross(normal, sDir);

        return PTVector.add(
            PTVector.scale(normal,  Math.sqrt(1 - u)),
            PTVector.add(
                PTVector.scale(sDir, r * Math.cos(angle)),
                PTVector.scale(tDir, r * Math.sin(angle))
            )
        );
    }
}
