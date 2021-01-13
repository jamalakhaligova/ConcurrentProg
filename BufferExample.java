//Bounded container (predefined size), but also a circular buffer
//Thread safe
//Server-side synchronization

class BoundedBuffer<T>{
	private final Object[] items = new Object[100]; //Change to Object array and always cast back to T later
	private int count;
	private int putptr, takeptr;
	
	public synchronized void put(T o) throws InterruptedException{ //server-side synchronization: actual synchronization on shared object
		while(count == items.length){
			System.out.println("Full buffer");
			wait();
		}
		
		items[putptr] = o;
		putptr++;
		
		if(putptr == items.length)
			putptr = 0;
		
		count++;
		notify();
	}
	
	public synchronized T take() throws InterruptedException{
		while(count == 0){
			System.out.println("Empty buffer");
			wait();
		}
		
		@SuppressWarnings("unchecked")
		T temp = (T)items[takeptr];
		takeptr++;
		
		if(takeptr == items.length)
			takeptr = 0;
		
		count--;
		notify();
		return temp;
	}
}

class Producer extends Thread{
	private BoundedBuffer<String> b;
	public Producer(BoundedBuffer<String> b){
		this.b = b;
	}
	
	public void run(){
		int i = 0;
		
		while(i<200){
			synchronized(b){ //synchronization meant for printing purposes (preserving consintency)
				try{
					b.put("Car");
					System.out.println("Element " + i + " added");
					i++;
				}catch(InterruptedException e){
					System.out.println(e);
				}
			}
			
			try{
				Thread.sleep((int)Math.random()*100);
			}catch(InterruptedException e){}
		}
	}
}

class Consumer extends Thread{
	private BoundedBuffer<String> b;
	public Consumer(BoundedBuffer<String> b){
		this.b = b;
	}
	
	public void run(){
		int i = 0;
		
		while(i<200){
			synchronized(b){ //synchronization meant for printing purposes (preserving consintency)
				try{
					System.out.println("Element " + b.take() + " at position " + i + " taken");
					i++;
				}catch(InterruptedException e){
					System.out.println(e);
				}
			}
			
			try{
				Thread.sleep((int)Math.random()*100);
			}catch(InterruptedException e){}
		}
	}
}

public class BufferExample{
	public static void main(String[] args){
		BoundedBuffer<String> buff = new BoundedBuffer<String>();
		Producer prod = new Producer(buff);
		Consumer cons = new Consumer(buff);
		
		prod.start();
		cons.start();
	}
}