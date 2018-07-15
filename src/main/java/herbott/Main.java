package herbott;

import herbott.listeners.*;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;

import java.io.File;

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
												.buildConfiguration();

		bot = new PircBotX(config);
		new StartServer().start();
		bot.startBot();
	}

	static class StartServer extends Thread {

		@Override
		public void run() {
			try {
				String webappDirLocation = "src/main/webapp/";
				Tomcat tomcat = new Tomcat();

				//The port that we should run on can be set into an environment variable
				//Look for that variable and default to 8080 if it isn't there.
				String webPort = System.getenv("PORT");
				if (webPort == null || webPort.isEmpty()) {
					webPort = "8080";
				}

				tomcat.setPort(Integer.valueOf(webPort));

				StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
				System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

				// Declare an alternative location for your "WEB-INF/classes" dir
				// Servlet 3.0 annotation will work
				File additionWebInfClasses = new File("target/classes");
				WebResourceRoot resources = new StandardRoot(ctx);
				resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
						additionWebInfClasses.getAbsolutePath(), "/"));
				ctx.setResources(resources);

				tomcat.start();
				tomcat.getServer().await();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}