package herbott.listeners;

import herbott.Statistics;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Objects;

public class PhrasesListener extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) {
        String nick = Objects.requireNonNull(event.getUser()).getNick();
        String message = event.getMessage().toLowerCase();

        if (Statistics.getStats().getBanList().contains(nick)) return;

        if (message.matches("(.*) кусь (.*)")) {
            event.respondChannel("КУСЬ SMOrc КУСЬ SMOrc КУСЬ SMOrc КУСЬ");
        }
        else if (message.matches("(.*) фыр (.*)")) {
            event.respondChannel("ФЫР-ФЫР-ФЫР-ФЫР!!!");
        }
        else if (message.matches("(.*) орда (.*)")) {
            event.respondChannel("ОРДА СОСЕД! ЗА АЛЬЯНС!!!");
        }
        else if (message.matches("(.*) умён (.*)") && !message.matches("(.*)я(.*)")) {
            event.respondChannel("Конечно умён, не то, что ты! LUL");
        } else if (message.matches("(.*)привет(.*)") || message.matches("(.*)здравствуй(.*)") ||
                message.matches("(.*)здарова(.*)") || message.matches("(.*) ку (.*)")) {
            if (event.getTags().get("badges").matches("(.*)subscriber(.*)"))
                event.respondWith(nick + ", привет!");
        }
    }
}