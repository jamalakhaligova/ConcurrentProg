package concurpractices;

import java.util.concurrent.Semaphore;

class SharedBuffer{
	int c = 0;
	
	Semaphore cs = new Semaphore(0);
	Semaphore ps = new Semaphore(1);
	
	public void put(int i){
		try{
			ps.acquire(); //producer semaphore taken
		}catch(InterruptedException e){
			System.out.println(e);
		}
		c = i;
		System.out.println(Thread.currentThread().getName() + " putting: " + i);
		cs.release(); //release consumer
	}
	
	public int get(){
		try{
			cs.acquire(); //consumer semaphore
		}catch(InterruptedException e){
			System.out.println(e);
		}
		System.out.println(Thread.currentThread().getName() + " getting: " + c);
		ps.release(); //release producer
		return c;
	}
}

class Producer implements Runnable{
	SharedBuffer s;
	
	public Producer(SharedBuffer s){
		this.s = s;
	}
	
	public void run(){
		for(int i = 0; i<5 ; i++){
			s.put(i);
		}
	}
}

class Consumer implements Runnable{
	SharedBuffer s;
	
	public Consumer(SharedBuffer s){
		this.s = s;
	}
	
	public void run(){
		for(int i = 0; i<5 ; i++){
			s.get();
		}
	}
}

public class ConProd{
	public static void main(String[] args){
		SharedBuffer sb = new SharedBuffer();
		
		Thread prod = new Thread(new Producer(sb), "Prod");
		Thread cons = new Thread(new Consumer(sb), "Cons");
		
		prod.start();
		cons.start();
	}
}