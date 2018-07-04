import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.LinkedHashMap;

public class DBHelper {

//    private static String testUri = "postgres://mnsoxvtbrehooy:e9a599094f305fe3aa11597b3f0fe41585b841696e3d4d806564ec590d8b01f3@ec2-23-23-245-89.compute-1.amazonaws.com:5432/dfrmmh0htm27sa";

    private static Connection getConnection() throws URISyntaxException, SQLException {
        System.out.println("Starting to create connection!");
//        URI dbUri = new URI(testUri);
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath()+ "?sslmode=require";

        System.out.println("Connection ready!");

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void updateData(String nick, String column) {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("UPDATE stats SET %s = %s + 1 WHERE nick = '%s'", column, column, nick));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addNewData(String nick, String column) {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(String.format("INSERT INTO stats (nick, %s) VALUES ('%s', 1);", column, nick));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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