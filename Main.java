//1 - both threads are instances of the same class (they do the exact same thing)
//2 - the run method should contain separete printing instructions
//3 - the sequence of prints at every execution must be alternated
//Client side synchronization (implicit)

class T extends Thread{
	Object l; //lock object (defined as an attribute)
	public T(Object o){
		l = o;
	}
	
	public void run(){
		synchronized(l){
			System.out.println(getName()+" Something 1");
			for(int i=0; i<10000; i++){}
			System.out.println(getName()+" Something 2");
		}
	}
}

public class Main{
	public static void main(String[] args){
		Object lock = new Object();
		T t1 = new T(lock);
		T t2 = new T(lock);
		
		t1.start();
		t2.start();
	}
}