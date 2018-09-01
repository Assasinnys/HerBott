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

    public SubLottery() {
        factor = DBHelper.getActivityMap();
    }

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        final double add = 0.01d;
        final int limit = 7;
        UpdateExecutor executor = new UpdateExecutor();
        String message = event.getMessage();
        String username = Objects.requireNonNull(event.getUser()).getNick();
        if (username.equalsIgnoreCase(Main.CHANNEL) || username.equalsIgnoreCase(Main.CREATOR)) {
            if (message.equalsIgnoreCase("!саб 1")) {
                event.respondChannel("И победителем стааал [барабанная дробь] ...");
                TimeUnit.SECONDS.sleep(1);
                event.respondChannel(randomizeAll().toUpperCase() + " PogChamp PogChamp PogChamp");
            } else if (message.equalsIgnoreCase("!саб 2")) {
                event.respondChannel("Розыгрывается саб среди 3х самых активных присутствующих в чате...");
                TimeUnit.MILLISECONDS.sleep(500);
                List<String> finalists = getTop3InChat();
                Iterator<String> iterator = finalists.iterator();
                StringBuilder builder = new StringBuilder("Это: ");
                while (iterator.hasNext()) {
                    builder.append(iterator.next()).append(" ");
                }
                event.respondChannel(builder.toString().trim());
                TimeUnit.SECONDS.sleep(2);
                event.respondChannel("И победителем становится... " + randomizeTop3(finalists) + " HSWP CoolCat");
            }
        } else if (message.length() >= limit) {
            if (factor.containsKey(username)) {
                factor.put(username, factor.get(username) + add);
            } else {
                factor.put(username, add);
            }
            if (!executor.isAlive()) {
                executor = new UpdateExecutor();
                executor.start();
            }
            System.out.println("Taking message to activity");
        } else {
            System.out.println("test");
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
                TimeUnit.MINUTES.sleep(1);
                System.out.println("Updating Activity...");
                DBHelper.updateActivity(factor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
