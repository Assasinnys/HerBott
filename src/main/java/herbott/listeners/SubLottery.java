package herbott.listeners;

import herbott.DBHelper;
import herbott.Main;
import herbott.utils.DoubleValueComparator;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class SubLottery extends ListenerAdapter {

    private Map<String, Double> factor;
    private UpdateExecutor executor = new UpdateExecutor();
    private WinnerTimer timer = new WinnerTimer();
    private volatile boolean chance = false;
    private String winnerName = "";

    public SubLottery() {
        factor = DBHelper.getActivityMap();
    }

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        final double add = 0.01d;
        final int limit = 7;
        String message = event.getMessage();
        String username = Objects.requireNonNull(event.getUser()).getNick();

        if (chance && username.equalsIgnoreCase(winnerName)) {
            event.respondChannel("@" + Main.CHANNEL + " , победитель подтверждён!");
            chance = false;
            winnerName = "";
            timer.interrupt();
        }

        if (username.equalsIgnoreCase(Main.CHANNEL) || username.equalsIgnoreCase(Main.CREATOR)) {
            if (message.equalsIgnoreCase("!опа")) {
                event.respondChannel("И победителем становится [барабанная дробь] ...");
                TimeUnit.SECONDS.sleep(5);
                winnerName = randomizeAll();
                event.respondChannel( winnerName + " PogChamp PogChamp PogChamp . У тебя есть минута, чтобы появиться в чате");
                chance = true;
                timer = new WinnerTimer();
                timer.start();
            } else if (message.equalsIgnoreCase("!саб")) {
                List<String> finalists = getTop3InChat();
                event.respondChannel("Розыгрывается саб среди " + finalists.size() + "х самых активных и присутствующих в чате...");
                TimeUnit.MILLISECONDS.sleep(500);
                Iterator<String> iterator = finalists.iterator();
                StringBuilder builder = new StringBuilder("Эти счастливчики: ");
                while (iterator.hasNext()) {
                    builder.append(iterator.next()).append(" | ");
                }
                event.respondChannel(builder.toString().trim());
                TimeUnit.SECONDS.sleep(2);
                winnerName = randomizeTop3(finalists);
                event.respondChannel("И победителем становится... " + winnerName + " HSWP CoolCat");
                event.respondChannel("Подтверди своё присутствие в течении минуты...");
                chance = true;
                timer = new WinnerTimer();
                timer.start();
            }
        }

        if (message.length() >= limit) {
            if (factor.containsKey(username)) {
                factor.put(username, factor.get(username) + add);
            } else {
                factor.put(username, add);
            }
            if (!executor.isAlive()) {
                System.out.println("Starting thread = " + !executor.isAlive());
                executor = new UpdateExecutor();
                executor.start();
            }
            System.out.println("Taking message to activity");
        } else {
            System.out.println("Not taking message");
        }
    }

    private String randomizeAll() {
        List<String> viewers = BotListener.viewersList();
        return viewers.get(ThreadLocalRandom.current().nextInt(viewers.size()));
    }

    private String randomizeTop3(List<String> finalists) {
        int counter = finalists.size();
        return (counter >= 2)? finalists.get(ThreadLocalRandom.current().nextInt(finalists.size())) :
                (counter != 0)? finalists.get(0) : "HerBott девиант =)";
    }

    private List<String> getTop3InChat() {
        List<String> viewers = BotListener.viewersList();
        List<String> finalists = new ArrayList<>();
        TreeMap<String, Double> top = getTopActivity();
        int counter = 0;
        for (String nick : top.navigableKeySet()) {
            if (counter > 2) break;
            if (viewers.contains(nick)) {
                finalists.add(nick);
                counter++;
            }
        }
        return finalists;
    }

    private TreeMap<String, Double> getTopActivity() {
        HashMap<String, Double> temp = new HashMap<>(factor);
        TreeMap<String, Double> sort = new TreeMap<>(new DoubleValueComparator(temp));
        sort.putAll(factor);
        return sort;
    }

    private class UpdateExecutor extends Thread {

        @Override
        public void run() {
            try {
                TimeUnit.MINUTES.sleep(2);
                System.out.println("Updating Activity...");
                DBHelper.updateActivity(factor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class WinnerTimer extends Thread {

        @Override
        public void run() {
            try {
                TimeUnit.MINUTES.sleep(3);
                chance = false;
                winnerName = "";
                TimeUnit.SECONDS.sleep(1);
                Main.bot.sendIRC().message("#" + Main.CHANNEL, "Победитель не ответил! BibleThump BibleThump BibleThump");
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }
}
