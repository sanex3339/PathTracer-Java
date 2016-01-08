package PathTracer;

import PathTracer.interfaces.Callback;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

final public class RenderThreadsController implements Runnable {
    final public int threadsCount = Runtime.getRuntime().availableProcessors();

    public int screenWidth;
    public int screenHeight;

    private ExecutorService executorService;
    private List<Future<List<Color>>> threadsPool;
    private Callback callback;

    /**
     * @param callback
     */
    RenderThreadsController(int screenWidth, int screenHeight, Callback callback) {
        Thread thread = new Thread(this, "RenderThreadsController");
        thread.start();

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.callback = callback;
    }

    /**
     * @return RenderThread
     */
    private Callable<List<Color>> getRenderThread() {
        return new RenderThread(
            this.screenWidth,
            this.screenHeight
        );
    }

    public void run () {
        this.executorService = Executors.newCachedThreadPool();
        this.threadsPool = new ArrayList<>();

        for (int i = 0; i < this.threadsCount; i++) {
            this.threadsPool.add(
                this.executorService.submit(
                    this.getRenderThread()
                )
            );

            this.startThread();
        }

        while (this.threadsPool.size() > 0) {
            this.startThread();
        }

        executorService.shutdown();
    }

    /**
     * Start thread from threadsPool, get thread data and run callback with that data.
     */
    private void startThread () {
        try {
            Future<List<Color>> thread = this.threadsPool.get(0);
            
            this.callback.callback(thread.get());

            this.threadsPool.remove(0);
            this.threadsPool.add(
                this.executorService.submit(
                    this.getRenderThread()
                )
            );
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
