//T1 and T2 should verify the following conditions:
// 1 - T2 would print a string (e.g. "T2 printing")
// 2 - T1 increases a counter from 0 to 10000
// 3 - when counter is 2000 T1 "suspends" T2. When counter is 6000 T1 "awakes" T2. T2 should not print while counter is in [2000, 6000]

class T1 extends Thread{
	private int i;
	T2 t2;
	
	public T1(T2 t){
		t2 = t;
	}
	
	public void run(){
		while(i < 10000){
			synchronized(t2){
				i++;
				if(i == 2000){
					System.out.println("Counter: " + i);
					t2.suspend = true;
				}
				if(i == 6000){
					System.out.println("Counter: " + i);
					t2.suspend = false;
					t2.notify();
				}
			}
			try{
				Thread.sleep((int)Math.random()*100);
			}catch(InterruptedException e){}
		}
	}
}

class T2 extends Thread{
	public boolean suspend = false;
	private int i;
	
	public void run(){
		while(i < 10500){ //main loop
			i++;
			synchronized(this){
				//i++;
				while(suspend){ //condition verification
					try{
						wait(); //this.wait();
					}catch(InterruptedException e){}
				}
				System.out.println("T2 printing. Suspended: " + suspend + " " + i);
			}// end of critical section
			try{
				Thread.sleep((int)Math.random()*100);
			}catch(InterruptedException e){}
		}
	}
}

public class WaitNotif{
	public static void main(String[] args){
		T2 t2 = new T2();
		T1 t1 = new T1(t2);
		
		t1.start();
		t2.start();
	}
}