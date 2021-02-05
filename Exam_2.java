class GeminiExecutor{
	private final Entity first;
	private final Entity second;
	private int runCounter;
	private int step;
	
	public GeminiExecutor(int s){
		first = new Entity(s, this);
		second = new Entity(s, this);
		
		for(int i = 0; i<s; i++){
			//Task t = new Task(i); first.pushTask(t); second.pushTask(t);
			first.pushTask(new Task(i));
			second.pushTask(new Task(i));
		}
	}
	
	public void activate(){
		first.start();
		second.start();
		
		System.out.println("Time 1: "+first.getTotTime()+"ms");
		System.out.println("Time 2: "+second.getTotTime()+"ms");
	}
	
	public synchronized void incrRunCounter(){
		runCounter++;
		if(runCounter%2 == 0){
			step++;
			notify(); //talk about this
		}
	}
	
	public synchronized int getStep(){
		return step;
	}
}

class Entity extends Thread{
	private final int size;
	private final Task[] queue;
	private GeminiExecutor ge;
	private boolean started = false, finished = false;
	private int counter, localStep, totalTime;
	
	public Entity(int s, GeminiExecutor ge){
		size = s;
		queue = new Task[size];
		this.ge = ge;
	}
	
	public void pushTask(Task t) throws IllegalStateException {
		if(!started){
			if(counter < size)
				queue[counter++] = t;
		}
		else
			throw new IllegalStateException();
	}
	
	public void run(){
		started = true;
		
		while(ge.getStep() < size){
			Task t = queue[localStep = ge.getStep()];
			t.run();
			totalTime += t.getTime();
			ge.incrRunCounter();
			synchronized(ge){
				while(localStep == ge.getStep()){
					try{
						ge.wait();
					}catch(InterruptedException e){}
				}
			}
		}
		
		synchronized(this){
			finished = true;
			notify();
		}
	}
	
	public synchronized int getTotTime(){
		while(!finished){
			try{
				wait();
			}catch(InterruptedException e){}
		}
		return totalTime;
	}
}

class Task implements Runnable{
	private int n, time;
	public Task(int n){
		this.n = n;
	}
	
	public int getTime(){
		return time;
	}
	
	public void run(){
		try{
			time = (int)(Math.random()*100);
			System.out.println(Thread.currentThread().getName()+" running task "+n);
			Thread.sleep(time);
		}catch(InterruptedException e){}
	}
}

// The code must compile with the provided main method
public class Exam_2{
	public static void main(String[] args){
		GeminiExecutor ge = new GeminiExecutor(4);
		ge.activate();
	}
}