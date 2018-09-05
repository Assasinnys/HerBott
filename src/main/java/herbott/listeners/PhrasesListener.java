package herbott.listeners;

import herbott.Statistics;
import herbott.utils.TimeOuter;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Objects;

public class PhrasesListener extends ListenerAdapter {

    private Boolean flag = Boolean.FALSE;

    @Override
    public void onMessage(MessageEvent event) {
        String nick = Objects.requireNonNull(event.getUser()).getNick();
        String message = event.getMessage().toLowerCase();

        if (Statistics.getStats().getBanlist().contains(nick) || flag) return;

        if (message.matches("(.*)кусь(.*)")) {
            event.respondChannel("КУСЬ SMOrc КУСЬ SMOrc КУСЬ SMOrc КУСЬ");
            new TimeOuter(Boolean.TRUE, 30);
        }
        else if (message.matches("(.*)фыр(.*)")) {
            event.respondChannel("ФЫР-ФЫР-ФЫР-ФЫР!!!");
            new TimeOuter(Boolean.TRUE, 30);
        }
        else if (message.matches("(.*)орда(.*)")) {
            event.respondChannel("ОРДА СОСЕД! ЗА АЛЬЯНС!!!");
            new TimeOuter(Boolean.TRUE, 30);
        }
        else if (message.matches("(.*)умён(.*)") && !message.matches("(.*)я(.*)")) {
            event.respondChannel("Конечно умён, не то, что ты! LUL");
            new TimeOuter(Boolean.TRUE, 30);
        }
    }
}