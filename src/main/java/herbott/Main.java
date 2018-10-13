package herbott;

import herbott.listeners.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.Listener;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final String BOTNAME = "LifeBot42";
    public static final String OAUTH = "oauth:dp2gucojee0oarsftbgde6bjfn14re";
    public static final String REFRESH_OAUTH = "79fia2ncbscnh10z7y1m8dpuailu5dr9ti2xnzitm2w0k67wbi";
    public static final String CHANNEL = "roblife42";
    public static final String CREATOR = "assasinnys";

    public static PircBotX bot;

    public static void main(String[] args) throws Exception {
        List<Listener> listeners = new ArrayList<>();
        listeners.add(new BotListener());
        listeners.add(new BattleRoyalChat());
        listeners.add(new DuelListener());
        listeners.add(new VoteListener());
        listeners.add(new BalListener());
        listeners.add(new PhrasesListener());
        listeners.add(new UserNoticeListener());
        listeners.add(new BanCommandListener());
        listeners.add(new TotalControl());
        listeners.add(new SubLottery());

        Configuration config = new Configuration.Builder()
                .setAutoNickChange(false)
                .setOnJoinWhoEnabled(false)
                .setCapEnabled(true)
                .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                .addServer("irc.twitch.tv")
                .setName(BOTNAME)
                .setServerPassword(OAUTH)
                .addAutoJoinChannel("#" + CHANNEL)
                .setAutoReconnect(true)
                .setAutoReconnectAttempts(10)
                .setAutoReconnectDelay(2000)
                .setBotFactory(new HerBottFactory())
                .addListeners(listeners)
                .buildConfiguration();

        bot = new PircBotX(config);
        bot.startBot();
    }
}