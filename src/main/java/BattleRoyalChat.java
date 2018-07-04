import org.json.JSONArray;
import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BattleRoyalChat extends ListenerAdapter {

    private Map<String, Integer> playersStatus = new HashMap<>();
    private List<String> players = new ArrayList<>();
    private Random random = new Random();
    private static boolean gameStatus;
    private static boolean regStatus;
    private volatile boolean timeout;

    public static boolean isAlive() {
        return gameStatus || regStatus;
    }

    private int startBattle() {
        if (!regStatus)
            players = viewersList();
        for (String nick : players) {
            if (!Bot.bots.contains(nick))
                playersStatus.put(nick, 100);
        }
        players.clear();
        players.addAll(playersStatus.keySet());
        gameStatus = true;
        regStatus = false;
        return players.size();
    }

    private String shot(String shooter) {
        String target = shooter;
        while (target.equalsIgnoreCase(shooter)) {
            target = players.get(random.nextInt(players.size()));
        }
        int damage = random.nextInt(101);
        int newHealth = playersStatus.get(target) - damage;
        if (newHealth <= 0) {
            playersStatus.remove(target);
            players.remove(target);
            return String.format("%s и убил его! Осталось %d игроков!", target, players.size());
        } else {
            playersStatus.put(target, newHealth);
            return String.format("%s нанося %d ед. урона!", target, damage);
        }
    }

    private List<String> viewersList() {
        List<String> temp = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(JSONParser.readUrl(String.format("https://tmi.twitch.tv/group/user/%s/chatters", Main.CHANNEL)));
            JSONArray viewers = json.getJSONObject("chatters").getJSONArray("viewers");
            for (int j = 0; j < viewers.length(); j++) {
                temp.add(viewers.getString(j));
            }
            JSONArray mods = json.getJSONObject("chatters").getJSONArray("moderators");
            for (int j = 0; j < mods.length(); j++) {
                temp.add(mods.getString(j));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    private String playersList() {
        StringBuilder builder = new StringBuilder("Список активных игроков:");
        for (String nick : players) {
            builder.append(" ").append(nick);
        }
        return builder.toString();
    }

    private boolean regPlayer(String nick) {
        if (players.contains(nick)) {
            return false;
        }
        players.add(nick);
        return true;
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String message = event.getMessage().toLowerCase();
        String shooter = event.getUser().getNick();
        if (message.equalsIgnoreCase("!battle") && !gameStatus && shooter.equalsIgnoreCase("assasinnys")) {
            event.respondWith("GAME STARTED! Зарегистрировано игроков: " + startBattle());
        }
        else if (message.startsWith("!битва") && !regStatus && !gameStatus && !timeout) {
            regStatus = true;
            TimerRegistration timer = new TimerRegistration(event);
            timer.start();
            event.respondWith("Начата регистрация на чат-битву! Чтобы участвовать, пиши в чат !готов. Регистрация закончится через 1 минуту");
        }
        else if (message.equalsIgnoreCase("!готов") && regStatus) {
            if (regPlayer(shooter))
                event.respondWith(shooter + " зарегистрирован!");
        }
        else if (message.startsWith("!пиу") && gameStatus && players.contains(shooter)) {
            event.respondWith(String.format("%s попал в %s ", shooter, shot(shooter)));
            if (players.size() == 1) {
                shooter = players.get(0);
                timeout = true;
                endGame(10);
                Statistics.getStats().sendStat(shooter, Statistics.BATTLE);
                event.respondWith(shooter + " ПОБЕЖДАЕТ В МАТЧЕ!!! HSWP");
            }
        }
        else if (message.startsWith("!хп") && gameStatus && players.contains(shooter)) {
                int hp = playersStatus.get(shooter);
                event.respondWith(" " + shooter + ", твоё хп: " + hp);
        }
        else if (message.startsWith("!список") && gameStatus) {
             event.respondWith(playersList());
        }
        else if (message.startsWith("!stop") && shooter.equalsIgnoreCase(Main.CREATOR)) {
            endGame(1);
            event.respondWith("Игра остановлена");
        }
        else if (message.equalsIgnoreCase("!стат битва")) {
//            int num = Statistics.getStats().receiveStat(shooter, Statistics.BATTLE);
//            event.respondWith(String.format("%s ты занял топчик %d раз(а) PogChamp", shooter, num));
            event.respondWith("Статистика временно недоступна!");
        }
        else if (message.equalsIgnoreCase("!команды битва")) {
            event.respondWith("Особые команды битвы: !пиу - выстрел, !хп - очевидно, !список - список живых.");
        }
        else if (message.equalsIgnoreCase("!топ битва")) {
//            String s = Statistics.getStats().top(Statistics.BATTLE);
//            event.respondWith(s);
            event.respondWith("Статистика временно недоступна!");
        }
    }

    private void endGame(int delay) {
        gameStatus = false;
        regStatus = false;
        players.clear();
        playersStatus.clear();
        new TimeOuter(delay).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class TimerRegistration extends Thread {

        private GenericMessageEvent event;

        TimerRegistration(GenericMessageEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MINUTES.sleep(1);
                if (players.size() >= 3) event.respondWith("Регистрация окончена. Игроков: " + startBattle() +". Битва начинается! GOWKratos");
                else {
                    endGame(1);
                    event.respondWith("Регистрация окончена. Игроков недостаточно для начала битвы. FailFish");
                }
            } catch (InterruptedException i) {
                i.printStackTrace();
            }
        }
    }

    class TimeOuter extends Thread {

        private int delay;

        TimeOuter(int delay) {
            this.delay = delay;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MINUTES.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout = false;
        }
    }
}