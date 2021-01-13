package concurpractices;

//import java.util.concurrent.locks;

public interface Lock{}

public class ReentrantLock implements Lock{}

Lock l = new ReentrantLock(); //lock creation
l.lock(); //trying to acquire the lock
try{
//critical section
}catch(Exception e){
	
}
finally{
	l.unlock(); //release lock
}

boolean tryLock(){}

l.tryLock(); //returns false

while(!l.tryLock()){}