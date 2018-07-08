package herbott.listeners;

import herbott.Main;
import herbott.Statistics;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.*;

public class ActivityListener extends ListenerAdapter {

    private Map<String, Integer> activity = new HashMap<>();

    @Override
    public void onMessage(MessageEvent event) {
        String nick = Objects.requireNonNull(event.getUser()).getNick();
        String message = event.getMessage();

        if (!(message.length() < 2))
            activity.put(nick, activity.getOrDefault(nick, 0) + 1);

        if (message.equalsIgnoreCase("!топ активных") && (nick.equalsIgnoreCase(Main.CREATOR))) {
            event.respondChannel(Statistics.getStats().find(activity, "Топ активных в чате: "));
        }
    }
}
