import org.json.JSONArray;
import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VoteListener extends ListenerAdapter {

    private ArrayList<String> list = new ArrayList<>();
    private HashMap<String, Integer> voteMap = new HashMap<>();
    private ArrayList<String> voters = new ArrayList<>();
    private volatile boolean voteStatus;

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String message = event.getMessage().toLowerCase();
        String user = event.getUser().getNick();

        if (message.startsWith("!vote:") && !voteStatus) {
            if ((user.equalsIgnoreCase(Main.CREATOR) || getMods().contains(user)) && (voteStatus = configVote(message))) {
                event.respondWith(String.format("Голосование начато! Голосать надо словом. %s", voteParser()));
                new VoteTimer(event).start();
            } else {
                list.clear();
                voteMap.clear();
                event.respondWith("Голосование не запущено");
            }
        }
        else if (list.contains(message) && !voters.contains(user) && voteStatus) {
            int i = voteMap.get(message);
            voteMap.put(message, ++i);
            voters.add(user);
            System.out.println("Отдан голос за " + message + ". Голосов за него: " + i);
        }
        else if (message.equalsIgnoreCase("!vote cancel")) {
            if ( user.equalsIgnoreCase(Main.CREATOR) || getMods().contains(user)) {
                voteStatus = false;
                voters.clear();
                voteMap.clear();
                list.clear();
                event.respondWith("Голосование отменено!");
            } else event.respondWith("Вы не можете отменить голосование...");
        }
        else if (message.equalsIgnoreCase("!vote stop") && voteStatus) {
            if (getMods().contains(user) || user.equalsIgnoreCase(Main.CREATOR)) {
                event.respondWith("Голосование остановлено. " + endVote());
            }
        }
        else if (message.equalsIgnoreCase("!vote") && !voteStatus) {
            event.respondWith("Синтаксис: !vote: <варианты, рзделённые \", \" (запятой с пробелом)>");
        }
    }

    private boolean configVote(String message) {
        String[] vars = message.split(":");
        vars = vars[1].trim().split(", ");
        for (int i = 0; i < vars.length; i++) {
            vars[i] = vars[i].trim().toLowerCase();
        }
        list.addAll(Arrays.asList(vars));
        for (String s : list) {
            voteMap.put(s, 0);
        }
        return list.size() >= 2;
    }

    private String voteParser() {
        int counter = 0;
        StringBuilder builder = new StringBuilder("Варианты: ");
        for (String var : list) {
            builder.append(++counter)
                    .append(". ")
                    .append(var)
                    .append("; ");
        }
        return builder.toString();
    }

    private String endVote() {
        voteStatus = false;
        String sResult = "";
        int max = 0;
        for (String s : voteMap.keySet()) {
            if (max < voteMap.get(s)) {
                sResult = s;
                max = voteMap.get(s);
            }
        }
        voters.clear();
        voteMap.clear();
        list.clear();
        return String.format("Побеждает вариант \"%s\" набравший %d голосов.", sResult, max);
    }

    private List<String> getMods() {
        List<String> temp = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(JSONParser.readUrl(String.format(
                    "https://tmi.twitch.tv/group/user/%s/chatters", Main.CHANNEL)));
            JSONArray mods = json.getJSONObject("chatters").getJSONArray("moderators");
            for (int j = 0; j < mods.length(); j++) {
                temp.add(mods.getString(j));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    class VoteTimer extends Thread {

        private GenericMessageEvent event;

        VoteTimer(GenericMessageEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MINUTES.sleep(1);
                if (voteStatus) event.respondWith(endVote());
            } catch (InterruptedException i) {
                i.printStackTrace();
            }
        }
    }
}
