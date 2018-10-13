package herbott.listeners;

import org.json.JSONArray;
import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import herbott.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class BotListener extends ListenerAdapter {

    public static List<String> bots = new ArrayList<>();
    private int guess;
    private boolean guessGame;
    private boolean timeoutTryapka;
    private boolean timeout2;
    private boolean timeout3;

    public BotListener() {
        bots.add("herbott");
        bots.add("electricallongboard");
        bots.add("rutonybot");
        bots.add("mikuia");
        bots.add("moobot");
        bots.add("nightbot");
        bots.add("electricalskateboard");
        bots.add("commanderroot");
    }

    private String oneOfAllChat() {
        List<String> temp = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(JSONParser.readUrl(String.format("https://tmi.twitch.tv/group/user/%s/chatters", "roboher42")));
            JSONArray viewers = json.getJSONObject("chatters").getJSONArray("viewers");
            for (int j = 0; j < viewers.length(); j++) {
                temp.add(viewers.getString(j));
            }
            JSONArray mods = json.getJSONObject("chatters").getJSONArray("moderators");
            for (int j = 0; j < mods.length(); j++) {
                temp.add(mods.getString(j));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Random r = new Random();
        return temp.get(r.nextInt(temp.size()));
    }

    public static List<String> viewersList() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    private String randomViewer(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = event.getMessage();
        String user = Objects.requireNonNull(event.getUser()).getNick();

        if (Statistics.getStats().getBanlist().contains(user)) return;

        System.out.println(">>>> " + user + ": " + message);
        if (message.equalsIgnoreCase("бот")) {
            event.respondWith("@" + user + " Bot online! Ready to work");
        } else if (message.equalsIgnoreCase("!best")) {
            event.respondWith("Лучший в этом чате - " + oneOfAllChat() + " Kappa");
        } else if (message.startsWith("!random")) {
            event.respondWith("Твоё число: " + randomNumber(message));
        } else if (message.equalsIgnoreCase("!users")) {
            JSONObject json = new JSONObject(JSONParser.readUrl(String.format("https://tmi.twitch.tv/group/user/%s/chatters", "roboher42")));
            event.respondWith("Зрителей: " + json.getString("chatter_count"));
        } else if (message.equalsIgnoreCase("всем привет")) {
            event.respondWith(user + ", привет!");
        } else if (message.equalsIgnoreCase("!тряпка") && !timeoutTryapka) {
            timeoutTryapka = true;
            new TimeOuter().start();
            List<String> list = viewersList();
            while (true) {
                String nick = randomViewer(list);
                if (!bots.contains(nick)) {
                    event.respondWith(user + " кинул cцаную тряпку в " + nick + " WutFace");
                    if (nick.equalsIgnoreCase(Main.CHANNEL) || nick.equalsIgnoreCase("svezhyi_rulet")) {
                        event.respondWith("и получил мутом по губам LUL");
                        event.respondWith(String.format("/timeout %s %d", user, 120));
                    }
                    Statistics.getStats().sendStat(nick, DBHelper.TRYAPKA);
                    break;
                }
            }
        } else if (message.equalsIgnoreCase("!угадать") && !guessGame) {
            guessGame = true;
            guess = new Random().nextInt(10) + 1;
            System.out.println("Число = " + guess);
            event.respondWith("Я загадал число от 1 до 10. Угадайте! Отвечать в виде: !число <ваш_ответ>");
        } else if (message.startsWith("!число") && guessGame) {
            event.respondWith("@" + user + " " + guess(message));
        } else if (message.equalsIgnoreCase("!стат тряпка")) {
            int num = Statistics.getStats().receiveStat(user, DBHelper.TRYAPKA);
            event.respondWith(String.format("%s , по тебе попали %d раз(а) сцаной тряпкой. DansGame", user, num));
//            event.respondWith("Статистика временно недоступна!");
        } else if (message.equalsIgnoreCase("!топ тряпка")) {
            String s = Statistics.getStats().top(Statistics.TRAP);
            event.respondWith(s);
//            event.respondWith("Статистика временно недоступна!");
        } else if (message.equalsIgnoreCase("!команды")) {
            event.respondWith("Основные команды: !best, !users, !random <число>, !тряпка, !угадать, !стат тряпка, !стат битва, !стат дуэль," +
                    " !команды битва, !битва, !топ битва, !топ тряпка, !топ дуэль, !суицид, !цалуй, !ВАБАНК, !группа, !тэг, !дс, !трек");
        } else if (message.equalsIgnoreCase("!суицид")) {
            event.respondWith(String.format("Суицид так суицид! Это твой выбор! Прощай, %s riPepperonis", user));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException i) {
                i.printStackTrace();
            }
            if (ThreadLocalRandom.current().nextInt(10) < 7) {
                event.respondWith(String.format("/timeout %s %d", user, 60));
            } else event.respondChannel("Ебать ты лох, даже суициднуться не смог! LUL LUL LUL");
        } else if (message.equalsIgnoreCase("!цалуй") && !timeout2) {
            timeout2 = true;
            new TimeOuter2().start();
            List<String> list = viewersList();
            while (true) {
                String nick = randomViewer(list);
                if (!bots.contains(nick)) {
                    event.respondWith(String.format("%s решил слиться в страстном поцелуе с %s KappaPride", user, nick));
                    break;
                }
            }
        } else if (message.equals("!ВАБАНК") && !timeout3) {
            timeout3 = true;
            new TimeOuter3().start();
            event.respondChannel(String.format("%s прожимает ВАБАНК и уходит с %s в тайную комнату PogChamp", user, randomViewer(viewersList())));
        } else if (message.equalsIgnoreCase("!тэг")) {
            event.respondChannel("Тэг роба: roblife42#2537");
        } else if (message.equalsIgnoreCase("!группа")) {
            event.respondChannel("Наша группа в вк: https://vk.com/roblife42");
        } else if (message.equalsIgnoreCase("!дс")) {
            event.respondChannel("Наш дискорд: https://discord.gg/pTGw77c");
        } else if (message.equals("!трек")) {
            if (event.getTags().get("badges").matches("(.*)subscriber(.*)")) {
                event.respondChannel("Заказывай, бро: https://twitch-dj.ru/c/RobLife42");
            } else {
                event.respondChannel("А ты кто такой? А ну-ка подпишись сначала: https://www.twitch.tv/products/roblife42 MrDestructoid");
            }
        }
    }

    private String guess(String message) {
        String[] s = message.split(" ");
        try {
            int answer = Integer.parseInt(s[1]);
            if (answer == guess) {
                guessGame = false;
                return "Ты угадал";
            }
            return "Не угадал";
        } catch (ArrayIndexOutOfBoundsException a) {
            return "Команда без числа";
        }
    }

    private int randomNumber(String message) {
        Random random = new Random();
        String[] s = message.split(" ");
        return random.nextInt(Integer.parseInt(s[1])) + 1;
    }

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(PingEvent event) {
        Main.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    class TimeOuter extends Thread {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeoutTryapka = false;
        }
    }

    class TimeOuter2 extends Thread {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout2 = false;
        }
    }

    class TimeOuter3 extends Thread {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout3 = false;
        }
    }
}