import java.util.concurrent.*;

public class MainConcurrency {
    private static CountDownLatch startGate = new CountDownLatch(1);
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(5);
    private static  GenerationManager manager = new GenerationManager(100);

    public static void main(String[] args) {
        int numProducer = 7;
        int numConsumer = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numProducer+numConsumer);
        for (int i=0; i< numProducer; i++){
            executor.execute(new NumberProducer(startGate, queue, manager));
        }
        for (int i=0; i< numConsumer; i++){
            executor.execute(new NumberConsumer(startGate, queue, manager));
        }
        startGate.countDown();
        try {
            executor.shutdown();
            if (!executor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                System.out.println("Still waiting ...");
                System.exit(0);
            }
        }catch (InterruptedException ex){
            System.out.println("Main thread was interrupted");
        }
    }
}
