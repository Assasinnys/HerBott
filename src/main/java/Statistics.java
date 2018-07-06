import java.io.*;
import java.util.*;

public class Statistics {

    public static final int BATTLE = 0;
    public static final int TRAP = 1;
    public static final int DUEL = 2;

    private static Statistics stats = new Statistics();

    private ArrayList<String> nicks;

    public static Statistics getStats() {
        return stats;
    }

    private Statistics() {
        nicks = DBHelper.readNicks();
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
        ValueComparator bvs = new ValueComparator(temp);
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
        if (nicks.contains(nick))
            DBHelper.updateData(nick, column);
        else {
            DBHelper.addNewData(nick, column);
            nicks.add(nick);
        }
    }

    public int receiveStat(String nick, String column) {
        return nicks.contains(nick)? DBHelper.receiveData(nick, column) : 0;
    }

    @Deprecated
    private void writeToFile(File file, Map<String, Integer> map) {
        StringBuilder builder = new StringBuilder();
        for (String nick : map.keySet()) {
            builder.append(nick)
                    .append(" ")
                    .append(map.get(nick))
                    .append("\n");
        }
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    class ValueComparator implements Comparator<String> {
        Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}

