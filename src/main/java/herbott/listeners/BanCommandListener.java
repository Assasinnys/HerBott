package herbott.listeners;

import herbott.Main;
import herbott.Statistics;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Objects;

public class BanCommandListener extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) {
        String nick = Objects.requireNonNull(event.getUser()).getNick();
        String message = event.getMessage();

        if (message.startsWith("!ban") && (nick.equalsIgnoreCase(Main.CREATOR) ||
                event.getTags().get("user-type").equalsIgnoreCase("mod"))) {
            try {
                event.respondChannel(Statistics.getStats().addBan(message.split(" ")[1]));
            } catch (ArrayIndexOutOfBoundsException e) {
                event.respondChannel("Неверный синтаксис команды");
            }
        } else if (message.startsWith("!unban") && nick.equalsIgnoreCase(Main.CREATOR)) {
            try {
                event.respondChannel(Statistics.getStats().deleteBan(message.split(" ")[1]));
            } catch (ArrayIndexOutOfBoundsException e) {
                event.respondChannel("Неверный синтаксис команды");
            }
        }

    }
}
