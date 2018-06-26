package herbott;

import com.google.common.collect.ImmutableMap;
import org.pircbotx.*;

import java.io.IOException;
import java.util.List;

public class HerInputParser extends InputParser {

    protected PircBotX bot;
    protected Configuration configuration;

    public HerInputParser(PircBotX bot) {
        super(bot);
        this.bot = bot;
        this.configuration = bot.getConfiguration();
    }

    @Override
    public void processCommand(String target, UserHostmask source, String command, String line, List<String> parsedLine, ImmutableMap<String, String> tags) throws IOException {
        super.processCommand(target, source, command, line, parsedLine, tags);
        String message = parsedLine.size() >= 2 ? parsedLine.get(1) : "";
        if (command.equals("USERNOTICE")) {
            configuration.getListenerManager().onEvent(new UserNoticeEvent(bot, tags, message));
        }
    }
}
