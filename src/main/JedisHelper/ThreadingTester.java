package JedisHelper;

/**
 * Created by kartik.k on 9/3/2014.
 */
class ThreadingTester implements Runnable{
    private final String message;
    private final long interval;
    public ThreadingTester(String msg, long interval) {
        this.message = msg;
        this.interval = interval;
    }
    public void run() {
        try {
            while (true) {
                System.out.println(message);
                Thread.sleep(interval);
            }
        } catch (InterruptedException iex) {
            System.out.println("exiting..");
        }
    }
}
