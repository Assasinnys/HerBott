import java.io.*;
import java.util.*;

public class Statistics {

    public static final int BATTLE = 0;
    public static final int TRAP = 1;
    public static final int DUEL = 2;

    private static Statistics stats = new Statistics();
    private File fileT;
    private File fileB;
    private File fileD;

    private Map<String, Integer> statB;
    private Map<String, Integer> statT;
    private Map<String, Integer> statD;

    public static Statistics getStats() {
        return stats;
    }

    private Statistics() {
        fileB = new File("statsB.txt");
        fileT = new File("statsT.txt");
        fileD = new File("statsD.txt");
        statB = new HashMap<>();
        statT = new HashMap<>();
        statD = new HashMap<>();
        if (!fileB.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                fileB.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!fileT.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                fileT.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!fileD.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                fileD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        readFromFile(fileB, statB);
        readFromFile(fileT, statT);
        readFromFile(fileD, statD);
    }

    public String top(int flag) {
        String result = "";
        if (flag == BATTLE) {
            result = find(statB, "Топ битв: ");
        }
        else if (flag == TRAP) {
            result = find(statT, "Топ тряпок: ");
        }
        else if (flag == DUEL) {
            result = find(statD, "Топ дуэлей: ");
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

    public void sendStat(String nick, int flag) {
        if (flag == BATTLE) {
            if (statB.containsKey(nick)) {
                int value = statB.get(nick) + 1;
                statB.put(nick, value);
                DBHelper.updateData(nick, "battle");
            } else {
                statB.put(nick, 1);
                DBHelper.addNewData(nick, "battle");
            }
//            writeToFile(fileB, statB);
        }
        else if (flag == TRAP) {
            if (statT.containsKey(nick)) {
                int value = statT.get(nick) + 1;
                statT.put(nick, value);
            } else statT.put(nick, 1);
            writeToFile(fileT, statT);
        }
        else if (flag == DUEL) {
            if (statD.containsKey(nick)) {
                int value = statD.get(nick) + 1;
                statD.put(nick, value);
            } else statD.put(nick, 1);
            writeToFile(fileD, statD);
        }
    }

    public int receiveStat(String nick, int flag) {
        int result = 0;
        if (flag == BATTLE) {
            result = statB.getOrDefault(nick, 0);
        }
        else if (flag == TRAP) {
            result = statT.getOrDefault(nick, 0);
        }
        else if (flag == DUEL) {
            result = statD.getOrDefault(nick, 0);
        }
        return result;
    }

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

    private void readFromFile(File file, Map<String, Integer> map) {
        String[] buffer;
        String line;
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                buffer = line.split(" ");
                map.put(buffer[0], Integer.parseInt(buffer[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
