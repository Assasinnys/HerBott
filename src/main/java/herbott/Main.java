package herbott;

import herbott.listeners.*;
import herbott.utils.WakeUpTimer;
import herbott.webserver.ControlFromAppConnector;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.Listener;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final String BOTNAME = "LifeBot42";
    public static final String CLIENT_ID = "qkgjxy0g275eedgwehwby8irxfrxm1";
    public static final String CLIENT_SECRET = "8l3symtvrrm7g7166mb5bidozus6cu";
    public static final String OAUTH = "oauth:mhc3e2af15fes3x9o1qeyveqrbl7mk";
    public static final String CHANNEL = "roblife42";
    public static final String CHANNEL_ID = "241801295";
    public static final String CREATOR = "assasinnys";
    public static final String CREATOR_ID = "47295543";
    public static final int VK_GROUP_ID = -169323171;
    public static final String VK_ACCESS_TOKEN = "b68c7690e8f0ba4724b8492e813ebad0b8fd1943d6f0ace8a54a58e81d13c74fcd1906932f264002a7df8";
    public static volatile boolean isActive = false;
    public static ControlFromAppConnector connector;
    public static WakeUpTimer wakeUpTimer;

    public static PircBotX bot;

    public static void main(String[] args) throws Exception {
        connector = new ControlFromAppConnector();
        connector.start();

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
                .addListeners(setupListeners())
                .buildConfiguration();

        bot = new PircBotX(config);
        bot.startBot();
    }

    private static List<Listener> setupListeners() {
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
        return listeners;
    }
}