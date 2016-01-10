package PathTracer.renderer;

import PathTracer.interfaces.SceneObject;

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
}
