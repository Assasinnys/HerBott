package herbott.listeners;

import herbott.retrofit.ApiManager;
import herbott.retrofit.model.FollowJsonModel;
import herbott.utils.TimeParser;
import herbott.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import herbott.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static herbott.utils.Utils.sendSubscribeRequest;

public class BotListener extends ListenerAdapter {

    public static List<String> bots = new ArrayList<>();
    private int guess;
    private boolean guessGame;
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
        bots.add("p0sitivitybot");
        bots.add("rutonybot");
    }

    // retrofit
    private String oneOfAllChat() {
        List<String> temp = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(JsonUtils.readUrl(String.format("https://tmi.twitch.tv/group/user/%s/chatters", Main.CHANNEL)));
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

    // retrofit
    public static List<String> viewersList() {
        List<String> temp = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(JsonUtils.readUrl(String.format("https://tmi.twitch.tv/group/user/%s/chatters", Main.CHANNEL)));
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

        if (Statistics.getStats().getBanList().contains(user)) return;

        System.out.println(">>>> " + user + ": " + message);
        if (message.equalsIgnoreCase("бот")) {
            event.respondWith("@" + user + " Bot online! Ready to work");
        } else if (message.equalsIgnoreCase("всем привет")) {
            event.respondWith(user + ", привет!");
        } else if (message.equalsIgnoreCase("!угадать") && !guessGame) {
            guessGame = true;
            guess = ThreadLocalRandom.current().nextInt(10) + 1;
            System.out.println("Число = " + guess);
            event.respondWith("Я загадал число от 1 до 10. Угадайте! Отвечать в виде: !число <ваш_ответ>");
        } else if (message.startsWith("!число") && guessGame) {
            event.respondWith("@" + user + " " + guess(message));
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
        } else if (message.equalsIgnoreCase("!тэг")) {
            event.respondChannel("Тэг роба: roblife42#2537");
        } else if (message.equalsIgnoreCase("!группа")) {
            event.respondChannel("Наша группа в вк: https://vk.com/roblife42");
        } else if (message.equalsIgnoreCase("!дс")) {
            event.respondChannel("Наш дискорд: https://discord.gg/d5KHdQM");
        } else if (message.equals("!трек")) {
            if (event.getTags().get("badges").matches("(.*)subscriber(.*)")) {
                event.respondChannel("Заказывай, бро: https://twitch-dj.ru/c/RobLife42");
            } else {
                event.respondChannel("А ты кто такой? Подпишись, тогда и поговорим: https://www.twitch.tv/products/roblife42 MrDestructoid");
            }
        } else if (message.equalsIgnoreCase("!follow")) {
            String result = getFollowTime(getUserId(user));
            if (!result.equals(""))
                event.respondChannel(String.format("%s , ты подписан на Роба уже %s DxCat", user, result));
            else
                event.respondChannel("Ах ты ж даже не фоловер! SMOrc");
        } else if (message.equalsIgnoreCase("!sub_stream_notice") && user.equalsIgnoreCase(Main.CREATOR)) {
            sendSubscribeRequest();
        } else if (message.equalsIgnoreCase("!main_stop") && user.equalsIgnoreCase(Main.CREATOR)) {
            if (mainStop()) {
                event.respondChannel("BOT: MAIN STOP - isActive = " + Main.isActive);
            }
        } else if (message.equalsIgnoreCase("!main_start") && user.equalsIgnoreCase(Main.CREATOR)) {
            if (mainStart()) {
                event.respondChannel("BOT: MAIN START - isActive = " + Main.isActive);
            }
        }
    }

    private boolean mainStop() {
        Utils.inactiveBot();
        return true;
    }

    private boolean mainStart() {
        Utils.activeBot();
        return true;
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

    /**
     * We MUST respond to this or else we will get kicked
     */
    @Override
    public void onPing(PingEvent event) {
        Main.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    // retrofit
    private String getUserId(String nickname) throws Exception {
        String url = "https://api.twitch.tv/helix/users?login=" + nickname;
        JSONObject json = new JSONObject(JsonUtils.readUrlAuth(url));
        return json.getJSONArray("data").getJSONObject(0).getString("id");
    }

    private String getFollowTime(String userId) {
        String answer = "";
        try {
            FollowJsonModel response = ApiManager.getApiManager()
                    .getHelixApi()
                    .getFollowData(userId, 100)
                    .execute()
                    .body();
            if (response != null) {
                List<FollowJsonModel.Data> followData = response.data;
                for (FollowJsonModel.Data data : followData) {
                    System.out.println(data.toString());
                    if (data.toName.equalsIgnoreCase(Main.CHANNEL)) {
                        answer = data.followedAt;
                        break;
                    }
                }
                System.out.println("follow count = " + followData.size());
                if (!answer.equalsIgnoreCase("")) {
                    answer = answer.replace("T", " ").replace("Z", "");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    answer = TimeParser.parse(sdf.parse(answer).getTime(), System.currentTimeMillis());
                } else {
                    answer = "Ах ты даже не фолловер! SMOrc";
                }
            } else {
                answer = "Что-то пошло не так BibleThump";
            }
        } catch (IOException | ParseException io) {
            io.printStackTrace();
            answer = "Произошла ошибка при получении информации с twitch.tv BibleThump";
        }
        return answer;
    }
}