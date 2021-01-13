class CustomThread extends Thread{
	public CustomThread(String n){
		super(n);
	}
	
	public void run(){
		for(int i=0; i<10; i++){
			System.out.println(getName());
		}
	}
}

public class YieldTest{
	public static void main(String[] args){
		Thread t = new CustomThread("CT");
		//t.setPriority(Thread.MAX_PRIORITY);
		t.start();
		
		try{
			t.join();
			//Thread.join(long milliseconds);
		}catch(InterruptedException e){
			System.out.println(e);
		}
		
		for(int i=0; i<10; i++){
			//Thread.yield();
			System.out.println(Thread.currentThread().getName());
		}
	}
}