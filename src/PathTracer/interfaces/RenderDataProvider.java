package PathTracer.interfaces;

import PathTracer.renderer.RenderResult;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface RenderDataProvider {
    Callable<RenderResult> callback();
}