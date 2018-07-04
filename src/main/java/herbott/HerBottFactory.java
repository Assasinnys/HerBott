<<<<<<< HEAD
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
=======
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
>>>>>>> 29a7e92ec4d72b8641f1052c96614bb058c9be9e
