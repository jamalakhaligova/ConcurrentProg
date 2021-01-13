public class PingPong2 implements Runnable{
	private String word;
	private int delay;
	
	public PingPong2(String w, int d){
		word = w;
		delay = d;
	}
	
	public void run(){
		try{
			System.out.println(Thread.currentThread().getName());
			for(int i=0; i<20; i++){
				System.out.println(word);
				Thread.sleep(delay);
			}
		}catch(InterruptedException e){return;}
	}
	
	public static void main(String[] args){
		Runnable ping = new PingPong2("PING", 30);
		Runnable pong = new PingPong2("PONG", 30);
		
		Thread t1 = new Thread(ping);
		t1.setName("T1");
		t1.start();
		
		Thread t2 = new Thread(pong);
		t2.setName("T2");
		t2.start();
	}
}