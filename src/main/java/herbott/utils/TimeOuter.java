package herbott.utils;

import java.util.concurrent.TimeUnit;

public class TimeOuter extends Thread {

    private String id;
    private long sec;

    public TimeOuter(String id, long sec) {
        this.id = id;
        this.sec = sec;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            System.out.println("TimeOuter interrupted");
        }

    }
}
