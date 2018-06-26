import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import org.pircbotx.hooks.types.GenericChannelEvent;
import org.pircbotx.hooks.types.GenericChannelUserEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AllListener extends ListenerAdapter {
    @Override
    public void onEvent(Event event) throws Exception {
        super.onEvent(event);
//        System.out.println(event.toString());
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        System.out.println(String.format(">>>>> %s %s: %s", getTime(), event.getUser().getNick(), event.getMessage()));
    }

    @Override
    public void onNotice(NoticeEvent event) {
        System.out.println(getTime() + " Notice:" + event.getNotice());
    }

    @Override
    public void onPart(PartEvent event) {
        System.out.println(getTime() + " Part: " + event.getUser().getNick());
    }

    @Override
    public void onJoin(JoinEvent event) {
        System.out.println(getTime() + " Join event: " + event.getUser().getNick());
    }

    @Override
    public void onServerResponse(ServerResponseEvent event) {
        System.out.println(getTime() + " Server response: " + event.getRawLine());
    }

    @Override
    public void onConnect(ConnectEvent event) {
        System.out.println(getTime() + " connect event!");
    }

    @Override
    public void onPing(PingEvent event) {
        Main.bot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

//    @Override
//    public void onUserList(UserListEvent event) {
//        System.out.println(String.format("%s userListEvent: %s", getTime(), event.getUsers().size()));
//    }

    @Override
    public void onUnknown(UnknownEvent event) {
        System.out.println(String.format(">>>>>>>>>>>>>>>>\n%s UnknownEvent: %s \n >>>>>>>>>>>>>>>>>>", getTime(), event.getLine()));
    }

//    @Override
//    public void onMessage(MessageEvent event) {
//        System.out.println(">>>> MSG: " + event.getUser().getNick() + ": " + event.getMessage());
//        System.out.println(">>?? TAG: " + event.getTags().toString());
//    }

    private String getTime() {
        return new SimpleDateFormat("hh:mm:ss").format(new Date());
    }
}
