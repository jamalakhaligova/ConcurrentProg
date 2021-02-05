import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class Stock_Market {
    private int value;

    public void addQuotation(int n) {
        this.value += n;
    }

    public synchronized int getQuotation() {
        return value;
    }
}

class Stock_Trend extends Thread {
    private String trend;
    private double risk;

    public Stock_Trend(double r) {
        this.risk = r;
        setDaemon(true);
    }

    public String getTrend() {
        return this.trend;
    }

    public void run() {
        while (true) {
            synchronized (this) {
                double t = Math.random();

                if (t > risk) {
                    trend = "increasing";
                    System.out.println("Market trend: increasing");
                } else {
                    trend = "decreasing";
                    System.out.println("Market trend: decreasing");
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notifyAll();
            }
        }
    }
}

class Company implements Callable<String> {
    private Stock_Market sm;
    private Stock_Trend st;
    private int total;

    public Company(Stock_Market psm, Stock_Trend pst) {
        this.sm = psm;
        this.st = pst;
    }

    public String call() {
        
        String result = "";
        for (int i = 0; i < 10; i++) {
            synchronized(st){
            int num = (int) (Math.random() * 1000);
            if (st.getTrend() == "increasing") {
                this.total = num;
            } else {
                this.total = -num;
            }
        
            sm.addQuotation(total);
            if (this.total > 0) {
                result = Thread.currentThread() + " gaining";
                System.out.println(result);
            } else {
                result = Thread.currentThread() + " losing";
                System.out.println(result);
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        }
        return result;
    }
}

public class Exam_3 {
    public static void main(String[] args) {
        Stock_Market sm = new Stock_Market();
        double r = Math.random();
        String riskValue = String.format("%.2f", r);
        System.out.println("Loss probability: " + riskValue);

        Stock_Trend st = new Stock_Trend(r);
        st.setDaemon(true);

        st.start();

        ArrayList<FutureTask<String>> tasks = new ArrayList<FutureTask<String>>();
        for (int i = 0; i < 4; i++) {
            FutureTask<String> fut_Task = new FutureTask<String>(new Company(sm,st));
            tasks.add(fut_Task);
        }

        for (int i = 0; i < 4; i++) {
            Thread t = new Thread(tasks.get(i) , "company - " + i);
            t.start();
        }


        try {
            for (int i = 0; i < 4; i++) {
            System.out.println("Status update:" + tasks.get(i).get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(sm.getQuotation());


	}
}