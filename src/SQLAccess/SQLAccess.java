package SQLAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zzc on 8/15/17.
 */
public class SQLAccess {
     private Connection connection = null;
     private String userName;
     private String passWord;
     private String serverHostAndPort;
     private String dbName;

     public SQLAccess(String userName, String passWord, String serverHostAndPort, String dbName) {
          this.userName = userName;
          this.passWord = passWord;
          this.serverHostAndPort = serverHostAndPort;
          this.dbName = dbName;
          setConnection();
     }

     private void setConnection() {
          try {
              Class.forName("com.mysql.jdbc.Driver");
              String conn = "jdbc:mysql://" + serverHostAndPort + "/" + dbName + "?user=" +
                      userName + "&password=" + passWord;
               System.out.println("Start to connect to database" + conn);
               connection = DriverManager.getConnection(conn);
              System.out.println("Connect to database successfully");
          } catch (ClassNotFoundException e) {
              e.printStackTrace();
          } catch (SQLException e) {
              e.printStackTrace();
          }
     }

}
