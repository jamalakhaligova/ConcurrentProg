import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ExecutionException;

public class Test{
	static int max(int[] a){
		int i = a[0];
		for(int j = 0; j<a.length; j++){
			if(a[j]>i)
			i = a[j];
		}
		return i;
	}
	
	static class Max implements Callable<Integer>{
		private int[] a;
		Max(int[] a){
			this.a = a;
		}
		
		public Integer call(){
			return max(a);
		}
	}
	
	public static void main(String[] args){
		int[] a1 = {20, 34, 123, 84, 94, 300};
		int[] a2 = {48, 87, 294, 76, 9};
		try{
			FutureTask<Integer> ft = new FutureTask<Integer>(new Max(a1));
			Thread t = new Thread(ft);
			t.start();
			int m = max(a2);
			if(m < ft.get())
				m = ft.get();
			System.out.println("max is " + m);
		}catch(ExecutionException | InterruptedException e){}
	}
}