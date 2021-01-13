//waiting on a shared object, for some condition

obj.wait() -> //current thread gets suspended (not runnable) and put in a list of threads waiting on obj

obj.notify() -> //wakes up (set back to runnable) one thread suspended on obj
obj.notifyAll() -> //wakes up (set back to runnable) all threads suspended on obj

//T1
synchronized(obj){
	while(!condition)
		obj.wait();
	
	//instructions to execute when the condition is fulfilled (true)
}

//T2
synchronized(obj){
	//instructions that change the condition
	
	obj.notifyAll();
}