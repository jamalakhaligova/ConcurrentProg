/*
ExecutorService executor = Executors.newFixedThreadPool(5);
// Receives several tasks
// Operations occur (thread pool might have not finished)
// You need to shutdown the thread pool by a known deadline

executor.shutdown(); //not necessary if you know "how long to wait"
try{
	if(executor.awaitTermination(1500, TimeUnit.MILLISECONDS)){
		executor.shutdownNow();
	}
}catch(InterruptedException e){
	System.err.println(e);
	executor.shutdownNow();
}
*/

/*
ScheduledThreadPoolExecutor
schedule(Runnable task, long delay, TimeUnit unit); -> task runs after the specified delay
schedule(Callable task, long delay, TimeUnit unit); -> task runs after the specified delay
scheduleAtFixedRate(Runnable task, long initialDelay, long delay, TimeUnit unit); -> task runs after the specified initial delay, with an interval (delay) between executions
scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit);
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Date;

class Task implements Runnable{
	private String name;
	
	public Task(String name){
		this.name = name;
		System.out.println(getName()+" created at " + new Date());
	}
	
	public String getName(){
		return name;
	}
	
	public void run(){
		System.out.println(getName()+" started at " + new Date() + " by " + Thread.currentThread().getName());
	}
}

public class SchedTPExample{
	public static void main(String[] args){
		ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
		Task t = new Task("Task");
		
		executor.scheduleWithFixedDelay(t, 2, 1, TimeUnit.SECONDS);
	}
}
