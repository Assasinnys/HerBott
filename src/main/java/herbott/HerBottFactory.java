package herbott;

import org.pircbotx.Configuration;
import org.pircbotx.InputParser;
import org.pircbotx.PircBotX;

public class HerBottFactory extends Configuration.BotFactory {

    @Override
    public InputParser createInputParser(PircBotX bot) {
        return new HerInputParser(bot);
    }
}
