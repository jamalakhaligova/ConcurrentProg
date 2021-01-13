import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cappuccino{
	private static String grindCoffee(String s) throws InterruptedException{
		System.out.println("Grinder started");
		Thread.sleep((int)Math.random()*100);
		System.out.println("Grains grinded");
		if(s.equals("coffee"))
			return "grinded coffee";
		else
			return "bad coffee";
	}
	
	private static Integer boil() throws InterruptedException{
		System.out.println("Warming up water");
		Thread.sleep((int)Math.random()*160);
		System.out.println("Water is boiling");
		return 100;
	}
	
	private static String makeCoffee(String g, Integer b) throws InterruptedException{
		System.out.println("Starting coffee making");
		Thread.sleep((int)Math.random()*100);
		System.out.println("Making the coffee with "+g+" and water at "+b+" degrees");
		return "coffee";
	}
	
	private static String makeFoam(String t) throws InterruptedException{
		System.out.println("Starting foam making");
		Thread.sleep((int)Math.random()*160);
		System.out.println("Made "+t+" foam");
		return "foam";
	}
	
	private static String mix(String c, String f) throws InterruptedException{
		Thread.sleep((int)Math.random()*160);
		System.out.println("Added "+f+" on top of "+c);
		return "Cappuccino";
	}
	
	
	
	
	private static Callable<String> grindCoffeeC(final String s){
		return new Callable<String>(){
			public String call(){
				try{
					return grindCoffee(s);
				}catch(InterruptedException e){}
				return "";
			}
		};
	}
	
	private static Callable<Integer> boilC(){
		return new Callable<Integer>(){
			public Integer call(){
				try{
					return boil();
				}catch(InterruptedException e){}
				return 0;
			}
		};
	}
	
	private static Callable<String> makeCoffeeC(final Future<String> g, final Future<Integer> b){
		return new Callable<String>(){
			public String call(){
				try{
					return makeCoffee(g.get(), b.get());
				}catch(InterruptedException | ExecutionException e){}
				return "";
			}
		};
	}
	
	private static Callable<String> makeFoamC(final String t){
		return new Callable<String>(){
			public String call(){
				try{
					return makeFoam(t);
				}catch(InterruptedException e){}
				return "";
			}
		};
	}
	
	
	
	
	static void seqCappuccino() throws InterruptedException{
		String g = grindCoffee("coffee");
		int b = boil();
		String c = makeCoffee(g, b);
		String f = makeFoam("normal milk");
		String capp = mix(c, f);
		System.out.println(capp + " is ready");
	}
	
	static void parCappuccino() throws ExecutionException, InterruptedException{
		ExecutorService pool = Executors.newCachedThreadPool();
		
		Future<String> g = pool.submit(grindCoffeeC("coffee"));
		Future<Integer> b = pool.submit(boilC());
		Future<String> c = pool.submit(makeCoffeeC(g, b));
		Future<String> f = pool.submit(makeFoamC("normal milk"));
		String capp = mix(c.get(), f.get());
		System.out.println(capp + " is ready");
		
		pool.shutdown();
	}
	
	public static void main(String[] args) throws ExecutionException, InterruptedException{
		seqCappuccino();
		System.out.println("\n-------------------------------------------\n");
		parCappuccino();
	}
}