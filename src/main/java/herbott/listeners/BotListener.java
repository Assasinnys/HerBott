package herbott.listeners;

import herbott.utils.TimeParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import herbott.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class BotListener extends ListenerAdapter {

    public static List<String> bots = new ArrayList<>();
    private int guess;
    private boolean guessGame;
    private long timeTryapka = 0;
    private long timeKiss = 0;
    private long timeAllIn = 0;
    private long timeFollow = 0;
    private static final int DELAY = 30000;

    public BotListener() {
        bots.add("lifebott42");
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
        } else if (message.equalsIgnoreCase("!тряпка")
                && (System.currentTimeMillis() - timeTryapka) > DELAY) {
            timeTryapka = System.currentTimeMillis();
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
            guess = ThreadLocalRandom.current().nextInt(10) + 1;
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
                    " !команды битва, !битва, !топ битва, !топ тряпка, !топ дуэль, !суицид, !цалуй, !ВАБАНК, !группа, !тэг, !дс, !трек, !follow");
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
        } else if (message.equalsIgnoreCase("!цалуй")
                && (System.currentTimeMillis() - timeKiss) > DELAY) {
            timeKiss = System.currentTimeMillis();
            List<String> list = viewersList();
            while (true) {
                String nick = randomViewer(list);
                if (!bots.contains(nick)) {
                    event.respondWith(String.format("%s решил слиться в страстном поцелуе с %s KappaPride", user, nick));
                    break;
                }
            }
        } else if (message.equals("!ВАБАНК") && (System.currentTimeMillis() - timeAllIn) > DELAY) {
            timeAllIn = System.currentTimeMillis();
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
                event.respondChannel("А ты кто такой? Подпишись, тогда и поговорим: https://www.twitch.tv/products/roblife42 MrDestructoid");
            }
        } else if (message.equalsIgnoreCase("!follow") && (System.currentTimeMillis() - timeFollow) > DELAY) {
            timeFollow = System.currentTimeMillis();
            String result = getFollowTime(getUserId(user));
            if (!result.equals(""))
                event.respondChannel(String.format("%s , ты подписан на Роба уже %s DxCat", user, result));
            else
                event.respondChannel("Ах ты ж даже не фоловер! SMOrc");
        } else if (message.equalsIgnoreCase("!серж")) {
            event.respondChannel("Смотри на здоровье: https://www.youtube.com/watch?v=wcy-fMbn2ps");
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

    private String getUserId(String nickname) throws Exception {
        String url = "https://api.twitch.tv/helix/users?login=" + nickname;
        JSONObject json = new JSONObject(JSONParser.readUrlAuth(url));
        return json.getJSONArray("data").getJSONObject(0).getString("id");
    }

    private String getFollowTime(String userId) {
        String url = "https://api.twitch.tv/helix/users/follows?from_id=" + userId;
        String s = "";
        try {
            JSONObject json = new JSONObject(JSONParser.readUrlAuth(url));
            JSONArray data = json.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                if (data.getJSONObject(i).getString("to_name").equalsIgnoreCase(Main.CHANNEL)) {
                    s = data.getJSONObject(i).getString("followed_at");
                    break;
                }
            }
            s = s.replace("T", " ").replace("Z", "");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            s = TimeParser.parse(sdf.parse(s).getTime(), System.currentTimeMillis());
        } catch (ParseException e) {
            e.printStackTrace();
            s = "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s;
    }
}