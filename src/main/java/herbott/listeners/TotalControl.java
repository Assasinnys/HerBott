package herbott.listeners;

import herbott.Main;
import herbott.utils.Utils;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Objects;

public class TotalControl extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) {
        if (!Objects.requireNonNull(event.getUser()).getNick().equalsIgnoreCase(Main.CREATOR))
            return;

        if (event.getMessage().startsWith("ex")) {
            event.respondChannel(event.getMessage().split(":")[1]);
        }
        else if (event.getMessage().equalsIgnoreCase("!test_vk_post")) {
            event.respondChannel("Starting test...");

            if (Utils.createWallPost("test")) {
                event.respondChannel("post successfully created");
            } else {
                event.respondChannel("Error: post not created");
            }
        }
    }
}
