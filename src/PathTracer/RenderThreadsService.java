package PathTracer;

import PathTracer.interfaces.RenderDataHandler;
import PathTracer.interfaces.RenderDataProvider;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

final public class RenderThreadsService {
    final private int threadsCount = Runtime.getRuntime().availableProcessors() - 2;

    private RenderDataProvider renderDataProvider;
    private RenderDataHandler renderDataHandler;

    private ExecutorService executorService;
    private Queue<Future<List<Color>>> threadsPool;

    RenderThreadsService(RenderDataProvider renderDataProvider, RenderDataHandler renderDataHandler) {
        this.renderDataProvider = renderDataProvider;
        this.renderDataHandler = renderDataHandler;
    }

    /**
     * Fill threadsPool with RenderDataProvider' objects (`Tracer` class objects).
     */
    public void run () {
        this.executorService = Executors.newWorkStealingPool();
        this.threadsPool = new LinkedList<>();

        for (int i = 0; i < this.threadsCount; i++) {
            this.threadsPool.add(
                this.executorService.submit(
                    this.renderDataProvider.callback()
                )
            );
        }

        while (!this.threadsPool.isEmpty()) {
            this.startThread();
        }

        this.executorService.shutdown();
    }

    /**
     * Start thread from threadsPool, get thread data (colors collection) and run renderDataHandler with that data.
     */
    private void startThread () {
        try {
            this.renderDataHandler.callback(
                this.threadsPool.poll()
            );
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        this.threadsPool.add(
            this.executorService.submit(
                this.renderDataProvider.callback()
            )
        );
    }
}
