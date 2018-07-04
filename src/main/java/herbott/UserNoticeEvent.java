<<<<<<< HEAD
package herbott;

import com.google.common.collect.ImmutableMap;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;

public class UserNoticeEvent extends Event {

    private ImmutableMap<String, String> tags;
    private PircBotX bot;
    private String message;

    public UserNoticeEvent(PircBotX bot, ImmutableMap<String, String> tags, String message) {
        super(bot);
        this.tags = tags;
        this.bot = bot;
        this.message = message;
    }

    @Override
    public void respond(String response) {

    }

    public ImmutableMap<String, String> getTags() {
        return tags;
    }

    public String getMessage() {
        return message;
    }
}
=======
package herbott;

import com.google.common.collect.ImmutableMap;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;

public class UserNoticeEvent extends Event {

    private ImmutableMap<String, String> tags;
    private PircBotX bot;
    private String message;

    public UserNoticeEvent(PircBotX bot, ImmutableMap<String, String> tags, String message) {
        super(bot);
        this.tags = tags;
        this.bot = bot;
        this.message = message;
    }

    @Override
    public void respond(String response) {

    }

    public ImmutableMap<String, String> getTags() {
        return tags;
    }

    public String getMessage() {
        return message;
    }
}
>>>>>>> 29a7e92ec4d72b8641f1052c96614bb058c9be9e
