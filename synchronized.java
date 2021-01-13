package concurpractices;

synchronized

public void m(){
	synchronized(this){ //sycnchronized(obj)
		// critical section
	}
}

public synchronized static void m(){
	// critical section
}

Class

public class MyStack{
	int top = 0;
	char[] data = new char[10];
	
	public void push(char c){
		synchronized(this){
			data[top] = c;
			top++;
		}
	}
	
	public synchronized char pop(){
		top--;
		return data[top+1];
	}
}

MyStack s = new MyStack();

s.push('c'); //t1
s.pop(); //t2