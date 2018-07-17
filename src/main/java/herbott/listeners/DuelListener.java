package herbott.listeners;

import herbott.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DuelListener extends ListenerAdapter {

    private String firstPlayer = "";
    private String secondPlayer = "";
    private boolean gameStatus;
    private volatile boolean timeout;

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        String message = event.getMessage();
        String nickname = event.getUser().getNick();

        if (BotListener.banlist.contains(nickname)) return;

        if (message.equalsIgnoreCase("!дуэль") && !gameStatus && !BattleRoyalChat.isAlive() && !timeout) {
            if (firstPlayer.equalsIgnoreCase("")) {
                firstPlayer = nickname;
                event.respondWith(String.format("Пользователь %s вызывает чат на дуэль! SMOrc", nickname));
            } else if (secondPlayer.equalsIgnoreCase("") && !nickname.equalsIgnoreCase(firstPlayer)) {
                gameStatus = true;
                secondPlayer = nickname;
                event.respondWith(String.format("%s принял дуэль", nickname));
                game(event);
            }
        }
        else if (message.equalsIgnoreCase("!дуэль отмена") && nickname.equalsIgnoreCase(Main.CREATOR)) {
            firstPlayer = "";
            secondPlayer = "";
            gameStatus = false;
            event.respondWith("Дуэль отменена, участники забанены! (шутка) BibleThump");
        }
        else if (message.equalsIgnoreCase("!стат дуэль")) {
            int value = Statistics.getStats().receiveStat(nickname, DBHelper.DUEL);
            event.respondWith(String.format("%s победил в дуэлях %d раз(а) PogChamp", nickname, value));
//            event.respondWith("Статистика временно недоступна!");
        }
        else if (message.equalsIgnoreCase("!топ дуэль")) {
            String s = Statistics.getStats().top(Statistics.DUEL);
            event.respondWith(s);
//            event.respondWith("Статистика временно недоступна!");
        }
    }

    private void game(GenericMessageEvent event) {
        Random random = new Random();
        int f = random.nextInt(11);
        int s = random.nextInt(11);
        event.respondWith(String.format("%s выбрасывает %d очков VS %d очков %s", firstPlayer, f, s, secondPlayer));
        delay();
        if (f > s) {
            Statistics.getStats().sendStat(firstPlayer, DBHelper.DUEL);
            event.respondWith(String.format("%s побеждает в дуэли, а для %s дуэль оказалась последней! BloodTrail", firstPlayer, secondPlayer));
            delay();
            event.respondWith(String.format("/timeout %s %d", secondPlayer, 35));
        } else if (s > f) {
            Statistics.getStats().sendStat(secondPlayer, DBHelper.DUEL);
            event.respondWith(String.format("%s побеждает в дуэли, а для %s дуэль оказалась последней! BloodTrail", secondPlayer, firstPlayer));
            delay();
            event.respondWith(String.format("/timeout %s %d", firstPlayer, 35));
        } else event.respondWith("В дуэли ничья! FBtouchdown");
        firstPlayer = "";
        secondPlayer = "";
        gameStatus = false;
        timeout = true;
        new TimeOuter().start();
    }

    private void delay() {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException i) {
            i.printStackTrace();
        }
    }

    class TimeOuter extends Thread {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout = false;
        }
    }
}