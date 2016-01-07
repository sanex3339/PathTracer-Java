package PathTracer.renderer;

import PathTracer.renderer.Lights.AbstractLight;
import PathTracer.renderer.Objects.AbstractObject;

import java.util.List;

public class Scene {
    private Camera camera;
    private List<AbstractObject> objects;
    private List<AbstractLight> lights;

    Scene (List<AbstractObject> objects, Camera camera) {
        this.objects = objects;
        this.camera = camera;
    }

    public void addObject(AbstractObject object) {
        this.objects.add(object);
    }

    public Camera getCamera () {
        return this.camera;
    }

    public List<AbstractObject> getObjects () {
        return this.objects;
    }

    public List<AbstractLight> getLights () {
        return this.lights;
    }
}
