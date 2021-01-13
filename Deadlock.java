import java.util.concurrent.Semaphore;

class ResourceHandler implements Runnable{
	Semaphore first;
	Semaphore second;
	
	public ResourceHandler(Semaphore f, Semaphore s){
		first = f;
		second = s;
	}
	
	public void run(){
		try{
			Thread t = Thread.currentThread();
			
			first.acquire();
			System.out.println(t + " acquired " + first);
			
			Thread.sleep(100);
			
			second.acquire();
			System.out.println(t + " acquired " + second);
			
			second.release();
			System.out.println(t + " released " + second);
			
			first.release();
			System.out.println(t + " released " + first);
			
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
}

public class Deadlock{
	public static void main(String[] args) throws InterruptedException {
		Semaphore sem1 = new Semaphore(1);
		Semaphore sem2 = new Semaphore(1);
		
		Thread t1 = new Thread(new ResourceHandler(sem1, sem2), "T1");
		Thread t2 = new Thread(new ResourceHandler(sem2, sem1), "T2");
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		
		System.out.println("We managed to finish!!");
	}
}