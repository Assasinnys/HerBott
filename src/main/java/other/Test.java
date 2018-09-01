package other;

import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                for (int i = 0; i < 15; i++) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    System.out.println("t = " + t.isAlive());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.start();
    }
}
