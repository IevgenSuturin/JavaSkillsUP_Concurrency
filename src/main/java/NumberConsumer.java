import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class NumberConsumer implements Runnable {
    private static int count = 0;
    private int id = count ++;

    private final CountDownLatch startGate;

    private BlockingQueue<Integer> queue;
    private GenerationManager manager;
    private int sum;

    NumberConsumer(CountDownLatch startGate, BlockingQueue<Integer> queue,
                   GenerationManager manager) {
        this.startGate = startGate;
        this.queue = queue;
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Consumer"+id;
    }

    int getQueueNumber() throws IllegalStateException{
        return queue.remove();
    }

    int incSum(int num){
        return sum+=num;
    }

    BlockingQueue<Integer> getQueue(){
        return queue;
    }

    public void run() {
        try {
            startGate.await();
            while (!manager.generateConsumption(this)) {
                Thread.sleep(2);
            }
        }catch (InterruptedException ignored){
            System.out.println("\t\t\t\tNum Consumer" + id + " was interrupted");
        }
    }
}

