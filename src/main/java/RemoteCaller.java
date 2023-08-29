

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RemoteCaller {

    private final String url = "jdbc:postgresql://192.168.1.85/remotedb"; //Insert IPAddress to remote database
    private final String user = "postgres";
    private final String password = "admin";
    public ResultSet fetchRecords() throws SQLException {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement state = conn.createStatement();
            ResultSet resultSet = state.executeQuery("SELECT * FROM Towns");
            return resultSet;
    }

}
