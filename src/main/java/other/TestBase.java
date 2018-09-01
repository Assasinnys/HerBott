package other;

import herbott.DBHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Scanner;

public class TestBase {
    private static String testUri = "postgres://mnsoxvtbrehooy:e9a599094f305fe3aa11597b3f0fe41585b841696e3d4d806564ec590d8b01f3@ec2-23-23-245-89.compute-1.amazonaws.com:5432/dfrmmh0htm27sa";
    private static volatile boolean b = true;
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Statement st = getConnection().createStatement();
        ResultSet rs;

        while(true) {
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("stop")) break;
            if (command.split(" ")[0].equalsIgnoreCase("select")) {
                rs = st.executeQuery(command);
                int counter = 1;
                while (rs.next()) {
                    System.out.print(counter++ + ". " + rs.getString(DBHelper.NICK) + " - ");
//                    System.out.print(rs.getInt(DBHelper.TRYAPKA) + " - ");
//                    System.out.print(rs.getInt(DBHelper.BATTLE) + " - ");
//                    System.out.println(rs.getInt(DBHelper.DUEL));
                    System.out.println(rs.getDouble("act"));
                }
            } else
                System.out.println("Выполнение запроса: " + st.execute(command));
        }
        st.close();
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        System.out.println("Starting to create connection!");
        URI dbUri = new URI(testUri);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?sslmode=require";

        System.out.println("Connection ready!");

        return DriverManager.getConnection(dbUrl, username, password);
    }
}
