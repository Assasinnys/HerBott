<<<<<<< HEAD
package herbott;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;

public class UserNoticeListener implements Listener {

    private final String DISPLAY_NAME = "display-name";

    @Override
    public void onEvent(Event event) throws Exception {
        if (event instanceof UserNoticeEvent) {
            UserNoticeEvent noticeEvent = (UserNoticeEvent) event;
            if (noticeEvent.getTags().get("msg-id").equals("sub") || noticeEvent.getTags().get("msg-id").equals("resub") ||
                    noticeEvent.getTags().get("msg-id").equals("subgift")) {
                System.out.println(String.format(">>>>> NoticeEvent: %s ; message= %s", noticeEvent.getTags().get(DISPLAY_NAME),
                        noticeEvent.getMessage()));
                sendMessage("Спасибо за подписку, " + noticeEvent.getTags().get(DISPLAY_NAME) + " !", event.getBot());
            }
        }
    }

    private void sendMessage(String message, PircBotX bot) {
        bot.sendIRC().message("#roboher42", message);
    }
}
=======
package herbott;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;

public class UserNoticeListener implements Listener {

    private final String DISPLAY_NAME = "display-name";

    @Override
    public void onEvent(Event event) throws Exception {
        if (event instanceof UserNoticeEvent) {
            UserNoticeEvent noticeEvent = (UserNoticeEvent) event;
            if (noticeEvent.getTags().get("msg-id").equals("sub") || noticeEvent.getTags().get("msg-id").equals("resub") ||
                    noticeEvent.getTags().get("msg-id").equals("subgift")) {
                System.out.println(String.format(">>>>> NoticeEvent: %s ; message= %s", noticeEvent.getTags().get(DISPLAY_NAME),
                        noticeEvent.getMessage()));
                sendMessage("Спасибо за подписку, " + noticeEvent.getTags().get(DISPLAY_NAME) + " !", event.getBot());
            }
        }
    }

    private void sendMessage(String message, PircBotX bot) {
        bot.sendIRC().message("#roboher42", message);
    }
}
>>>>>>> 29a7e92ec4d72b8641f1052c96614bb058c9be9e
