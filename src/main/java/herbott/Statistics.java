package herbott;

import herbott.utils.IntValueComparator;

import java.util.*;

public class Statistics {

    public static final int BATTLE = 0;
    public static final int TRAP = 1;
    public static final int DUEL = 2;

    private static Statistics stats = new Statistics();

    private ArrayList<String> nicksForStats;

    private ArrayList<String> banlist;

    private ArrayList<String> nicksForTokens;

    public static Statistics getStats() {
        return stats;
    }

    private Statistics() {
        nicksForStats = DBHelper.readNickArray(DBHelper.STATS_TABLE);
        banlist = DBHelper.readNickArray(DBHelper.BANLIST_TABLE);
        nicksForTokens = DBHelper.readNickArray(DBHelper.TOKENS_TABLE);
    }

    public String top(int flag) {
        String result = "";
        if (flag == BATTLE) {
            result = find(DBHelper.getTopMap(DBHelper.BATTLE), "Топ битвы: ");
        }
        else if (flag == TRAP) {
            result = find(DBHelper.getTopMap(DBHelper.TRYAPKA), "Топ тряпок: ");
        }
        else if (flag == DUEL) {
            result = find(DBHelper.getTopMap(DBHelper.DUEL), "Топ дуэлей: ");
        }
        return result;
    }

    private String find(Map<String, Integer> map, String start) {
        HashMap<String, Integer> temp = new HashMap<>(map);
        StringBuilder builder = new StringBuilder(start);
        IntValueComparator bvs = new IntValueComparator(temp);
        TreeMap<String, Integer> sort = new TreeMap<>(bvs);
        sort.putAll(map);
        int counter = 0;
        for (String nick : sort.navigableKeySet()) {
            builder.append(++counter)
                    .append(". ")
                    .append(nick)
                    .append(" - ")
                    .append(map.get(nick))
                    .append(" | ");
            if (counter >= 10) break;
        }
        return builder.toString();
    }

    public void sendStat(String nick, String column) {
        if (nicksForStats.contains(nick))
            DBHelper.updateData(nick, column);
        else {
            DBHelper.addNewData(nick, column);
            nicksForStats.add(nick);
        }
    }

    public String addBan(String nick) {
        if (banlist.contains(nick))
            return "Пользователь уже заблокирован.";
        DBHelper.addNewBan(nick);
        banlist.add(nick);
        return String.format("/me заблокировал команды для пользователя %s", nick);
    }

    public String deleteBan(String nick) {
        if (banlist.contains(nick)) {
            DBHelper.deleteBan(nick);
            banlist.remove(nick);
            return String.format("/me разрешил команды для пользователя %s", nick);
        }
        return String.format("/me не обнаружил %s в банлисте.", nick);
    }

    public int receiveStat(String nick, String column) {
        return nicksForStats.contains(nick)? DBHelper.receiveData(nick, column) : 0;
    }

    public ArrayList<String> getBanList() {
        return banlist;
    }

    public synchronized void addUserAccessToken(String nick, String accessToken, String refreshToken) {
        if (!nicksForTokens.contains(nick)) {
            DBHelper.addTokenToDB(nick, accessToken, refreshToken);
            nicksForTokens.add(nick);
            System.out.println("User added to token base.");
        } else {
            DBHelper.updateAccessToken(nick, accessToken, refreshToken);
            System.out.println("User already exist in token base. Updating...");
        }
    }

    public String getRefreshToken(String nick) {
        if (nicksForTokens.contains(nick)) {
            return DBHelper.getRefreshToken(nick);
        } else {
            return "";
        }
    }
}