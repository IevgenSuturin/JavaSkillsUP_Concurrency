import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class NumberProducer implements Runnable{
    private static Random random = new Random();
    private static int count = 0;

    private CountDownLatch startGate;

    private BlockingQueue<Integer> queue;
    private GenerationManager manager;
    private int id = count++;

    NumberProducer(CountDownLatch startGate, BlockingQueue<Integer> queue,
                   GenerationManager manager) {
        this.startGate = startGate;
        this.queue = queue;
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Producer" + id;
    }

    int putNumber() throws IllegalStateException{
        int num = 1 + random.nextInt(5);
        queue.add(num);
        return num;
    }

    BlockingQueue<Integer> getQueue(){
        return queue;
    }

    public void run() {
        try {
            startGate.await();
            while (!manager.generateProduction(this)) {
                Thread.sleep(2);
            }
        }catch (InterruptedException ex1){
             System.out.println("Num Producer" + id + " was interrupted");
        }
    }
}
