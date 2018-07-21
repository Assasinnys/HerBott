package herbott.listeners;

import herbott.Main;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class TotalControl extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) {
        if (event.getMessage().startsWith("ex") && event.getUser().getNick().equalsIgnoreCase(Main.CREATOR)) {
            event.respondWith(event.getMessage().split(":")[1]);
        }
    }
}
