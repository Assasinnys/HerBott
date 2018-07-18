package herbott;

import herbott.listeners.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;

public class Main {

	public static final String BOTNAME = "HerBott";
	public static final String OAUTH = "oauth:ey9jn438m1cxaw9dencqqnx5jorq49";
	public static final String CHANNEL = "roboher42";
	public static final String CREATOR = "assasinnys";

	public static PircBotX bot;

	public static void main(String[] args) throws Exception {
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
												.addListener(new BotListener())
												.addListener(new BattleRoyalChat())
												.addListener(new DuelListener())
                                                .addListener(new VoteListener())
                                                .addListener(new BalListener())
												.addListener(new PhrasesListener())
                                                .addListener(new UserNoticeListener())
												.addListener(new ActivityListener())
												.addListener(new BanCommandListener())
												.addListener(new TotalControl())
												.buildConfiguration();

		bot = new PircBotX(config);
		bot.startBot();
	}
}