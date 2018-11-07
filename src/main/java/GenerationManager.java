class GenerationManager {

    private int maxSumValue;

    GenerationManager(int maxSumValue) {
        this.maxSumValue = maxSumValue;
    }

    private boolean isGenerationStopped = false;

    synchronized boolean generateProduction(NumberProducer producer)
    {
        if(!isGenerationStopped) {
            try {
                int num = producer.putNumber();
                System.out.println(producer + " puts: num=" + num + "; queue=" + producer.getQueue());
            } catch (IllegalStateException ex) {
                System.out.println(producer + " was not able to put a value");
            }
        }
        return isGenerationStopped;
    }
    synchronized boolean generateConsumption(NumberConsumer consumer){
        try {
            if (!isGenerationStopped) {
                int num = consumer.getQueueNumber();
                System.out.println("\t\t\t\t\t\t\t\t"+consumer + " gets: num=" + num + " sum="+consumer.incSum(0)+"; queue=" + consumer.getQueue());
                if (consumer.incSum(num) >= maxSumValue) {
                    isGenerationStopped = true;
                    System.out.println(consumer+" got a result");
                }
            }
        }catch (IllegalStateException ex){
            System.out.println(consumer+" was not able to get a value");
        }
        return isGenerationStopped;
    }
}
