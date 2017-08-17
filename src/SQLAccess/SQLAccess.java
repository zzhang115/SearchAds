package SQLAccess;

import ads.Ad;

import java.sql.*;

/**
 * Created by zzc on 8/15/17.
 */
public class SQLAccess {
     private static final String MYSQL_HOST_PORT = "127.0.0.1:3306";
     private static final String MYSQL_DB = "searchads";
     private static final String MYSQL_TABLE = "ad";
     private static final String MYSQL_USER = "root";
     private static final String MYSQL_PASS = "238604";
     private Connection connection = null;

     public SQLAccess() {
          setConnection();
     }

     private void setConnection() {
          try {
              Class.forName("com.mysql.jdbc.Driver");
              String conn = "jdbc:mysql://" + MYSQL_HOST_PORT+ "/" + MYSQL_DB + "?user=" +
                      MYSQL_USER+ "&password=" + MYSQL_PASS;
               System.out.println("Start to connect to database" + conn);
               connection = DriverManager.getConnection(conn);
              System.out.println("Connect to database successfully");
          } catch (ClassNotFoundException e) {
              e.printStackTrace();
          } catch (SQLException e) {
              e.printStackTrace();
          }
     }

     private boolean RecordExists(Connection conn, String sqlString) {
         boolean isExists = false;
         PreparedStatement existStatement = null;
         try {
             existStatement = conn.prepareStatement(sqlString);
             ResultSet resultSet = existStatement.executeQuery();
             isExists = resultSet.next();
             existStatement.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return isExists;
     }
     public void addAdData(Ad ad) {
         String recordCheck = "select adId from " + MYSQL_DB + "." + MYSQL_TABLE + " where " +
                 "adId=" + ad.adId;

//         try {
//
//         }
     }

}
