package concurpractices;

//java.util.concurrent.Executor
//Executors
//newFixedThreadPool(int n) -> creates a fixed-size thread pool of n threads
//newSingleThreadExecutor() -> pool made of one single thread
//newCachedThreadPool() -> dinamically changing (in size) thread pool

//execute(Runnable r)

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Task implements Runnable{
	public void doSomething(){
		try{
			for(int i = 0; i<5; i++){
				System.out.println(Thread.currentThread().getName()+" printing " + i);
				Thread.sleep((int)Math.random()*100);
			}
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
	
	public void run(){
		System.out.println(Thread.currentThread().getName()+" Started");
		doSomething();
		System.out.println(Thread.currentThread().getName()+" Ended");
	}
}

public class ThreadPoolTest{
	public static void main(String[] args){
		ExecutorService executor = Executors.newFixedThreadPool(5); //new pool of 5 threads
		for(int i=0; i<10; i++){
			Runnable task = new Task();
			executor.execute(task); //starting our task
		}
		executor.shutdown();
		
		while(!executor.isTerminated()){}
		
		System.out.println("All threads terminated");
	}
}