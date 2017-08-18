package SQLAccess;

import ads.Ad;
import ads.Campaign;
import ads.Utility;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zzc on 8/15/17.
 */
public class SQLAccess {
     private static final String MYSQL_HOST_PORT = "127.0.0.1:3306";
     private static final String MYSQL_DB = "searchads";
     private static final String MYSQL_ADTABLE = "ad";
     private static final String MYSQL_CAMPAIGNTABLE = "campaign";
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

     private boolean isRecordExists(Connection conn, String sqlString) {
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
         String recordCheckSQL = "select adId from " + MYSQL_DB + "." + MYSQL_ADTABLE + " where " +
                 "adId=" + ad.adId + ";";
         String insertDataSQL = "insert into " + MYSQL_DB + "." + MYSQL_ADTABLE + " values(" +
                 "?,?,?,?,?,?,?,?,?,?,?);";
         if (!isRecordExists(connection, recordCheckSQL)) {
             try {
                 PreparedStatement insertStatement = connection.prepareStatement(insertDataSQL);
                 insertStatement.setLong(1, ad.adId);
                 insertStatement.setLong(2, ad.campaignId);
                 insertStatement.setString(3, Utility.strJoin(ad.keyWords, ","));
                 insertStatement.setDouble(4, ad.bidPrice);
                 insertStatement.setDouble(5, ad.price);
                 insertStatement.setString(6, ad.thumbnail);
                 insertStatement.setString(7, ad.description);
                 insertStatement.setString(8, ad.brand);
                 insertStatement.setString(9, ad.detailUrl);
                 insertStatement.setString(10, ad.category);
                 insertStatement.setString(11, ad.title);
                 insertStatement.executeUpdate();
                 insertStatement.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     }
     public Ad getAdData(Long adId) {
         PreparedStatement queryStatement = null;
         ResultSet resultSet = null;
         Ad ad = null;
         String sql_string = "select * from " + MYSQL_DB+ "." + MYSQL_ADTABLE + " where adId=" + adId;
         try {
             queryStatement = connection.prepareStatement(sql_string);
             resultSet = queryStatement.executeQuery();
             if (resultSet.next()) {
                 long campaignId = resultSet.getLong("campaignId");
                 String keyWords = resultSet.getString("keyWords");
                 List<String> keyWordsList = Arrays.asList(keyWords.split(","));
                 double bidPrice = resultSet.getDouble("bidPrice");
                 double price = resultSet.getDouble("price");
                 String thumbnail = resultSet.getString("thumbnail");
                 String description = resultSet.getString("description");
                 String brand = resultSet.getString("brand");
                 String detail_url = resultSet.getString("detailUrl");
                 String category = resultSet.getString("category");
                 String title = resultSet.getString("title");
                 double pClick = 0.0;
//                 System.out.println(adId+" "+ campaignId+" "+ brand+" "+ price+" "+ thumbnail+" "+ title+" "+ detail_url+" "+ bidPrice+" "+ pClick+" "+ category+" "+ description+" "+ keyWordsList);
                 ad = new Ad(adId, campaignId, brand, price, thumbnail, title, detail_url, bidPrice, pClick, category, description, keyWordsList);
             }
             queryStatement.close();
             resultSet.close();
         }
         catch(SQLException e ) {
             e.printStackTrace();
         }
         return ad;
     }

     public void addCampaignData(Campaign campaign) {
         String recordCheckSQL = "select campaignId from " + MYSQL_DB + "." + MYSQL_CAMPAIGNTABLE+ " where " +
                 "campaignId=" + campaign.campaignId + ";";
         String insertDataSQL = "insert into " + MYSQL_DB + "." + MYSQL_CAMPAIGNTABLE+ " values(" +
                 "?,?);";
         if (!isRecordExists(connection, recordCheckSQL)) {
             try {
                 PreparedStatement insertStatement = connection.prepareStatement(insertDataSQL);
                 insertStatement.setLong(1, campaign.campaignId);
                 insertStatement.setDouble(2, campaign.budget);
                 insertStatement.executeUpdate();
                 insertStatement.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
     }

     public void connectionClose() {
         try {
             if (connection != null) {
                 connection.close();
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

}
