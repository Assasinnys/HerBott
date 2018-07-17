package herbott;

import java.util.*;

public class Statistics {

    public static final int BATTLE = 0;
    public static final int TRAP = 1;
    public static final int DUEL = 2;

    private static Statistics stats = new Statistics();

    private ArrayList<String> nicks;

    private ArrayList<String> banlist;

    public static Statistics getStats() {
        return stats;
    }

    private Statistics() {
        nicks = DBHelper.readNickMap(DBHelper.STATS_TABLE);
        banlist = DBHelper.readNickMap(DBHelper.BANLIST_TABLE);
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

    public String find(Map<String, Integer> map, String start) {
//        HashMap<String, Integer> temp = new HashMap<>(map);
        StringBuilder builder = new StringBuilder(start);
//        ValueComparator bvs = new ValueComparator(temp);
//        TreeMap<String, Integer> sort = new TreeMap<>(bvs);
//        sort.putAll(map);
        int counter = 0;
        for (String nick : /*sort.navigableKeySet()*/ map.keySet()) {
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
        if (nicks.contains(nick))
            DBHelper.updateData(nick, column);
        else {
            DBHelper.addNewData(nick, column);
            nicks.add(nick);
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
            return String.format("/me разрешил команды для %s", nick);
        }
        return String.format("/me не обнаружил %s в банлисте.", nick);
    }

    public int receiveStat(String nick, String column) {
        return nicks.contains(nick)? DBHelper.receiveData(nick, column) : 0;
    }

    public ArrayList<String> getBanlist() {
        return banlist;
    }

//    class ValueComparator implements Comparator<String> {
//        Map<String, Integer> base;
//
//        ValueComparator(Map<String, Integer> base) {
//            this.base = base;
//        }
//
//        // Note: this comparator imposes orderings that are inconsistent with
//        // equals.
//        @SuppressWarnings("ComparatorMethodParameterNotUsed")
//        public int compare(String a, String b) {
//            if (base.get(a) >= base.get(b)) {
//                return -1;
//            } else {
//                return 1;
//            } // returning 0 would merge keys
//        }
//    }
}

