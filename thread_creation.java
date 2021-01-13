package concurpractices;

//(Conceptual example, not meant to be compiled)
//The Thread class belongs to the java.lang package

//Method 1:
class C extends Thread{
	public void run(){
		//your code...
	}
}

C c = new C();
c.start();

//Method 2:
class C implements Runnable{
	public void run(){
		//your code...
	}
}

C c = new C();
Thread t = new Thread(c);
t.start();