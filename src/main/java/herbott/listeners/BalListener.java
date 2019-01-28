package herbott.listeners;

import herbott.Statistics;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BalListener extends ListenerAdapter {

    private String[] answers = {"Бесспорно", "Мне кажется — «да»", "Пока не ясно, попробуй снова", "Даже не думай",
            "Предрешено", "Вероятнее всего", "Спроси позже", "Мой ответ — «нет»", "Никаких сомнений", "Хорошие перспективы",
            "Лучше не рассказывать", "По моим данным — «нет»", "Определённо да", "Знаки говорят — «да»",
            "Сейчас нельзя предсказать", "Перспективы не очень хорошие", "Можешь быть уверен в этом", "Да",
            "Сконцентрируйся и спроси опять", "Весьма сомнительно"};
    private volatile boolean timeout;

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = event.getMessage().toLowerCase();
        String user = event.getUser().getNick();

        if (Statistics.getStats().getBanList().contains(user)) return;

        if (message.startsWith("бот, стоит ли") || message.startsWith("бот стоит ли") || message.startsWith("!бот")) {
            if (timeout || BattleRoyalChat.isAlive()) return;
            timeout = true;
            new TimeOuter().start();
            Random random = new Random();
            event.respondChannel(user + " " + answers[random.nextInt(answers.length)]);
        }
    }

    class TimeOuter extends Thread {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                timeout = false;
            }
        }
    }
}
