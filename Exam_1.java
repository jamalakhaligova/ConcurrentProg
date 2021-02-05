import java.util.concurrent.LinkedBlockingQueue;

class HarmonicThreadPool{
	static int COUNT = 0;
	private final int size; // size of the thread pool
	private final Worker[] workers; // internal list of threads
	private final LinkedBlockingQueue<Runnable> queue; // queue of tasks
	private boolean stopped = false;
	private final long base_tick;
	private final long max_time;
	private long time;
	private int turn, id;
	
	public HarmonicThreadPool(int size, long base_tick, long max_time){
		this.id = COUNT;
		COUNT++;
		this.size = size;
		this.base_tick = base_tick;
		this.max_time = max_time;
		workers = new Worker[size];
		queue = new LinkedBlockingQueue<Runnable>();
		time = base_tick*2;
		
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
					long startTime = System.currentTimeMillis();
					task.run();
					long duration = System.currentTimeMillis()-startTime;
					update(duration);
					Thread.sleep(time-duration);
				}catch(InterruptedException|RuntimeException e){
					System.err.println(e);
					System.out.println("Time: "+time);
					System.out.println("Runtime exception in task "+ task);
				}
			}
		}
		
		public String toString(){
			return getPoolName()+"-"+getName();
		}
	}
	
	public String getPoolName(){
		return "pool-"+id;
	}
	
	public synchronized void update(long nTime){
		if(nTime > max_time){
			System.err.println("Max time exceeded! Shutting down the thread pool");
			shutdown();
		}
		else{
			if(nTime > time){
				time = (int)(((nTime/base_tick)+1)*base_tick);
			}
			turn++;
			if(turn>=base_tick){
				turn = 0;
				//time = base_tick * 2; //exam version
				time = (int)(((nTime/base_tick)+1)*base_tick); //better version
			}
		}
	}
	
	public synchronized boolean isStopped(){
		return stopped;
	}
	
	public void execute(Runnable task){
		if(!isStopped()){
			synchronized(queue){
				queue.add(task);
				queue.notify();
			}
		}
	}
	
	public void shutdown(){
		if(!stopped){
			Thread t;
			synchronized(this){
				stopped = true;
			}
			try{
				for(int i = 0; i<size; i++){
					t = workers[i];
					t.join();
					System.out.println(t+" has stopped");
				}
			}catch(InterruptedException e){}
			System.out.println("Thread pool "+id+" has shut down");
		}
	}
}

class Task implements Runnable{
	public void run(){
		try{
			int t = (int)(Math.random()*100);
			System.out.println(Thread.currentThread()+" running a task for time "+t);
			Thread.sleep(t);
		}catch(InterruptedException e){}
	}
}

public class Exam_1{
	public static void main(String[] args){
		HarmonicThreadPool htp = new HarmonicThreadPool(3, 4, 500);
		
		for(int i = 0; i<10; i++){
			Runnable t = new Task();
			htp.execute(t);
		}
		
		htp.shutdown();
	}
}