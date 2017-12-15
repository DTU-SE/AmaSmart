package threadPool;

import java.util.concurrent.LinkedBlockingQueue;


public class ThreadPool {
	  private final int nThreads;
	    private final PoolWorker[] threads;
	    private  LinkedBlockingQueue queue;
	
	    
	    public ThreadPool(int nThreads) {
	    	
	    	this.queue = new LinkedBlockingQueue();
	        this.nThreads = nThreads;
	        queue = new LinkedBlockingQueue();
	        threads = new PoolWorker[nThreads];

	        for (int i = 0; i < nThreads; i++) {
	            threads[i] = new PoolWorker(queue);
	            threads[i].start();
	        }
	    }

	    
	    public void execute(Runnable task) {
	        synchronized (queue) {
	            queue.add(task);
	            queue.notify();
	        }

}
	    
	    
}
