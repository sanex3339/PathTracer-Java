package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private List<SceneObject> objects;

    public Scene(List<SceneObject> objects, Camera camera) {
        this.objects = objects;
        this.camera = camera;
    }

    public void addObject (SceneObject object) {
        this.objects.add(object);
    }

    public Camera getCamera () {
        return this.camera;
    }

    public List<SceneObject> getObjects () {
        return this.objects;
    }

    public List<SceneObject> getLights () {
        List<SceneObject> sceneLights = new ArrayList<>();

        for (SceneObject object : this.objects) {
            if (!object.getMaterial().isLightSource()) {
                continue;
            }

            sceneLights.add(object);
        }

        return sceneLights;
    }
}
