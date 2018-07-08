package herbott.listeners;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Objects;

public class PhrasesListener extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) {
        String nick = Objects.requireNonNull(event.getUser()).getNick();
        String message = event.getMessage().toLowerCase();

        if (message.matches("(.*)кусь(.*)")) {
            event.respondChannel("КУСЬ SMOrc КУСЬ SMOrc КУСЬ SMOrc КУСЬ");
        } else if (message.matches("(.*)фыр(.*)")) {
            event.respondChannel("ФЫР-ФЫР-ФЫР-ФЫР!!!");
        }
    }
}