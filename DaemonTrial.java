//Thread t
//t.setDaemon(true)
//t.start()

class Daemon extends Thread{
	private int i=0;
	public Daemon(){
		setDaemon(true);
	}
	
	public synchronized int getI(){
		return i;
	}
	
	public void run(){
		while(true){
			synchronized(this){
				++i;
				if(i > 10000)
					i = 0;
			}
		}
	}
}

class D extends Thread{
	Daemon dm = new Daemon();
	int j = 0;
	public void run(){
		dm.start();
		while(j<100){
			System.out.println("Daemon value: "+dm.getI()); //service offered by the daemon
			j++;
		}
	}
}

public class DaemonTrial{
	public static void main(String[] args){
		D d = new D();
		d.start();
	}
}