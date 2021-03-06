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

    public static final String ACTIVITY_TABLE = "activity";
    private static final String ACT = "act";

    public static final String TOKENS_TABLE = "tokens";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String EXPIRE = "expire";

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
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewData(String nick, String column) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO stats (nick, tryapka, battle, duel) VALUES ('%s', 0, 0, 0);", nick));
            st.close();
            connection.close();
            updateData(nick, column);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewBan(String nick) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO %s (%s) VALUES ('%s');", BANLIST_TABLE, NICK, nick));
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteBan(String nick) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("DELETE FROM %s WHERE %s = '%s';", BANLIST_TABLE, NICK, nick));
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readNickArray(String tableName) {
        ArrayList<String> nicks = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT %s FROM %s", NICK, tableName));
            while (rs.next()) {
                nicks.add(rs.getString(NICK));
            }
            st.close();
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
            st.close();
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
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Double> getActivityMap() {
        Map<String, Double> map = new HashMap<>();
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * from %s;", ACTIVITY_TABLE));
            while (rs.next()) {
                map.putIfAbsent(rs.getString(NICK), rs.getDouble(ACT));
            }
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void updateActivity(Map<String, Double> map) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            Map<String, Double> db = getActivityMap();
            for (String nick : map.keySet()) {
                if (db.containsKey(nick)) {
                    st.executeUpdate(String.format("UPDATE %s SET %s = %f WHERE nick = '%s'",
                            ACTIVITY_TABLE, ACT, map.get(nick), nick));
                } else {
                    st.executeUpdate(String.format("INSERT INTO %s (%s, %s) VALUES ('%s', %f)",
                            ACTIVITY_TABLE, NICK, ACT, nick, map.get(nick)));
                }
            }
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetTable(String tableName) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.execute("DELETE from " + tableName);
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTokenToDB(String nick, String accessToken, String refreshToken) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO %s (%s, %s, %s) VALUES ('%s', '%s', '%s');",
                    TOKENS_TABLE, NICK, ACCESS_TOKEN, REFRESH_TOKEN, nick, accessToken, refreshToken));
            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRefreshToken(String nick) {
        String refreshToken = "";
        try {
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM %s WHERE nick = '%s'", TOKENS_TABLE, nick));
            rs.next();
            refreshToken = rs.getString(REFRESH_TOKEN);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return refreshToken;
    }

    public static void updateAccessToken(String nick, String accessToken, String refreshToken) {
        try {
            Statement st = getConnection().createStatement();
            st.executeUpdate(String.format("UPDATE %s SET %s = '%s', %s = '%s' WHERE %s = '%s';",
                    TOKENS_TABLE, ACCESS_TOKEN, accessToken, REFRESH_TOKEN, refreshToken, NICK, nick));
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getBearerToken(String nick) {
        String token = "";
        try {
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(String.format("SELECT * FROM %s WHERE nick = '%s'", TOKENS_TABLE, nick));
            rs.next();
            token = rs.getString(ACCESS_TOKEN);
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }
}