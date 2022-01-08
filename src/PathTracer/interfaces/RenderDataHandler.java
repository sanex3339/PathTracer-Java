package PathTracer.interfaces;

import PathTracer.renderer.RenderResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@FunctionalInterface
public interface RenderDataHandler {
    void callback (Future<RenderResult> thread) throws ExecutionException, InterruptedException;
}