package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private List<SceneObject> objects;
    private List<SceneObject> lights;

    public Scene(List<SceneObject> objects, Camera camera) {
        this.objects = objects;
        this.camera = camera;
    }

    /**
     * @param object
     */
    public void addObject (SceneObject object) {
        this.objects.add(object);
    }

    /**
     * @return Camera
     */
    public Camera getCamera () {
        return this.camera;
    }

    /**
     * @return List<SceneObject>
     */
    public List<SceneObject> getObjects () {
        return this.objects;
    }

    /**
     * @return List<SceneObject>
     */
    public List<SceneObject> getLights () {
        if (this.lights != null) {
            return this.lights;
        }

        List<SceneObject> sceneLights = new ArrayList<>();

        for (SceneObject object : this.objects) {
            if (!object.getMaterial().isLightSource()) {
                continue;
            }

            sceneLights.add(object);
        }

        this.lights = sceneLights;

        return this.lights;
    }
}
