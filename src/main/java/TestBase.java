import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class TestBase {
    private static String testUri = "postgres://mnsoxvtbrehooy:e9a599094f305fe3aa11597b3f0fe41585b841696e3d4d806564ec590d8b01f3@ec2-23-23-245-89.compute-1.amazonaws.com:5432/dfrmmh0htm27sa";

    public static void main(String[] args) throws Exception {
        Statement st = getConnection().createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM stats");
        while (rs.next()) {
            System.out.print(rs.getString(DBHelper.NICK) + " ");
            System.out.print(rs.getInt(DBHelper.BATTLE) + " ");
            System.out.print(rs.getInt(DBHelper.DUEL) + " ");
            System.out.println(rs.getInt(DBHelper.TRYAPKA));
        }
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        System.out.println("Starting to create connection!");
        URI dbUri = new URI(testUri);
//        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?sslmode=require";

        System.out.println("Connection ready!");

        return DriverManager.getConnection(dbUrl, username, password);
    }
}
