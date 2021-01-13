package concurpractices;

import java.util.concurrent.LinkedBlockingQueue;

class CustomTP{
	private final int size; // size of the thread pool
	private final Worker[] workers; // internal list of threads
	private final LinkedBlockingQueue<Runnable> queue; // queue of tasks
	private boolean stopped = false;
	
	public CustomTP(int size){
		this.size = size;
		workers = new Worker[size];
		queue = new LinkedBlockingQueue<Runnable>();
		
		for(int i = 0; i<size; i++){
			workers[i] = new Worker();
			workers[i].start();
		}
	}
	
	private class Worker extends Thread{
		public void run(){
			Runnable task;
			while(!(isStopped() && queue.isEmpty())){
				synchronized(queue){
					while(queue.isEmpty()){
						try{
							queue.wait();
						}catch(InterruptedException e){}
					}
					task = queue.poll();
				}
				
				try{
					task.run();
				}catch(RuntimeException e){
					System.out.println("Runtime exception in task "+ task);
				}
			}
		}
	}
	
	public synchronized boolean isStopped(){
		return stopped;
	}
	
	public void execute(Runnable task){
		synchronized(queue){
			queue.add(task);
			queue.notify();
		}
	}
	
	public void shutdown(){
		Thread t;
		synchronized(this){
			stopped = true;
		}
		try{
			for(int i = 0; i<size; i++){
				t = workers[i];
				t.join();
			}
		}catch(InterruptedException e){}
		System.out.println("Thread pool has shut down");
	}
}

public class CTP{
	public static void main(String[] args){
		CustomTP ctp = new CustomTP(3);
		
		for(int i = 0; i<10; i++){
			final int j = i;
			Runnable t = new Runnable(){
				public void run(){
					System.out.println("Task "+j+" running in "+Thread.currentThread().getName());
				}
			};
			
			ctp.execute(t);
		}
		
		ctp.shutdown();
	}
}