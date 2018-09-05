package herbott.utils;

import java.util.concurrent.TimeUnit;

public class TimeOuter extends Thread {

    private Boolean flag;
    private long sec;

    public TimeOuter(Boolean flag, long sec) {
        this.flag = flag;
        this.sec = sec;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            System.out.println("TimeOuter interrupted");
        }
        flag = false;
    }
}
