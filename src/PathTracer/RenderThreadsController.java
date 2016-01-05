package PathTracer;

import PathTracer.interfaces.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

final public class RenderThreadsController <T> implements Runnable {
    final public int threadsCount = Runtime.getRuntime().availableProcessors();

    public int currentSample = 1;

    private ExecutorService executorService;
    private List<Future<Map<String, T>>> threadsPool;
    private Callback<T> callback;

    /**
     * @param callback
     */
    RenderThreadsController(Callback<T> callback) {
        Thread thread = new Thread(this, "RenderThreadsController");
        thread.start();

        this.callback = callback;
    }

    /**
     * @return RenderThread
     */
    private Callable<Map<String, T>> getRenderThread() {
        return new RenderThread<T>(this.currentSample++);
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
        }

        int t = 20;

        while (t > 0) {
            this.startThread();
            t--;
        }

        /*while (this.threadsPool.size() > 0) {
            this.startThread();
        }*/

        executorService.shutdown();
    }

    /**
     * Start thread from threadsPool, get thread data and run callback with that data.
     */
    private void startThread () {
        try {
            Future<Map<String, T>> thread = this.threadsPool.get(0);

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
