import java.util.concurrent.Semaphore;

class Shared{
	public static int count = 0;
}

abstract class Effector extends Thread{
	protected Semaphore sem;
	Effector(String n){
		super(n);
	}
}

class Increment extends Effector{
	//Shared s = new Shared();
	public Increment(String n, Semaphore s){
		super(n);
		sem = s;
	}
	
	public void run(){
		try{
			sem.acquire();
			for(int i=0; i<10; i++){
				Shared.count++;
				Thread.sleep(20);
			}
			System.out.println(getName() + " " + Shared.count);
			sem.release();
			
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
}

class Decrement extends Effector{
	//Shared s = new Shared();
	public Decrement(String n, Semaphore s){
		super(n);
		sem = s;
	}
	
	public void run(){
		try{
			sem.acquire();
			for(int i=0; i<10; i++){
				Shared.count--;
				Thread.sleep(20);
			}
			System.out.println(getName() + " " + Shared.count);
			sem.release();
		
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
}

public class SharedDemo{
	public static void main(String[] args) throws InterruptedException {
		Semaphore sem = new Semaphore(1);
		
		Thread t1 = new Increment("Increment", sem);
		Thread t2 = new Decrement("Decrement", sem);
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		
		System.out.println("Finished. Shared value: " + Shared.count);
	}
}