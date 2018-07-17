package herbott;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHelper {

//    private static String testUri = "postgres://mnsoxvtbrehooy:e9a599094f305fe3aa11597b3f0fe41585b841696e3d4d806564ec590d8b01f3@ec2-23-23-245-89.compute-1.amazonaws.com:5432/dfrmmh0htm27sa";

    public static final String BATTLE = "battle";
    public static final String DUEL = "duel";
    public static final String TRYAPKA = "tryapka";
    public static final String NICK = "nick";

    public static final String BANLIST_TABLE = "banlist";
    public static final String STATS_TABLE = "stats";

    private static Connection getConnection() throws URISyntaxException, SQLException {
        System.out.println("Starting to create connection!");
//        URI dbUri = new URI(testUri);
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?sslmode=require";

        System.out.println("Connection ready!");

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void updateData(String nick, String column) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("UPDATE stats SET %s = %s + 1 WHERE nick = '%s'", column, column, nick));
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewData(String nick, String column) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO stats (nick, %s) VALUES ('%s', 1);", column, nick));
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewBan(String nick) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO %s (%s) VALUES ('%s');",BANLIST_TABLE, NICK, nick));
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteBan(String nick) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("DELETE FROM %s WHERE %s = '%s';",BANLIST_TABLE, NICK, nick));
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readNickMap(String tableName) {
        ArrayList<String> nicks = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT nick FROM %s", tableName));
            while (rs.next()) {
                nicks.add(rs.getString("nick"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nicks;
    }

    public static int receiveData(String nick, String column) {
        int result = 0;
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT %s FROM stats WHERE nick = '%s'", column, nick));
            rs.next();
            result = rs.getInt(column);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, Integer> getTopMap(String column) {
        Map<String, Integer> map = new HashMap<>();
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT nick, %s FROM stats WHERE %s > 0 ORDER BY %s DESC LIMIT 10",
                    column, column, column));
            while (rs.next()) {
                if (rs.getInt(column) != 0) map.put(rs.getString(DBHelper.NICK), rs.getInt(column));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /*public static void readDB() throws Exception {
        Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM stats");
        while (rs.next()) {
            System.out.println("Read nick: " + rs.getString("nick"));
            System.out.println("Read from DB: " + rs.getString("duel"));
            System.out.println("Read tryapka: " + rs.getString("tryapka"));
        }
        statement.executeUpdate("UPDATE stats SET duel = duel + 1, tryapka = 1 WHERE nick = 'assasinnys';");
        System.out.println("And again!");
        rs = statement.executeQuery("SELECT * FROM stats");
        while (rs.next()) {
            System.out.println("Read nick: " + rs.getString("nick"));
            System.out.println("Read from DB: " + rs.getString("duel"));
            System.out.println("Read tryapka: " + rs.getString("tryapka"));
        }
    }*/

}