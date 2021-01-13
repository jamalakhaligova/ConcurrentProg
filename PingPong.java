public class PingPong extends Thread{
	private String word;
	private int delay;
	
	public PingPong(String n, String w, int d){
		super(n); //Thread(String s)
		word = w;
		delay = d;
	}
	
	public void run(){
		try{
			System.out.println(getName());
			for(int i=0; i<200; i++){
				System.out.println(word+" "+i);
				Thread.sleep(delay);
			}
		}catch(InterruptedException e){return;}
	}
	
	public static void main(String[] args){
		//Thread t1 = new PingPong("T1", "PING", 30);
		//t1.start();
		new PingPong("T1", "PING", 30).start();
		
		Thread t2 = new PingPong("T2", "PONG", 30);
		t2.start();
	}
}