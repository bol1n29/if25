package fr.utt.utils.url.src.dbHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.Statement;

public class TestDB {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = " ";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/**
	 * The name of the database we are testing with (this default is installed
	 * with MySQL)
	 */
	private final String dbName = "stream";

	private Connection con;

	public TestDB() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		System.out.println("trying to get connection!! ");
		conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName
				+ ":" + this.portNumber + "/" + this.dbName, connectionProps);
		System.out.println(" Connection achieved!! ");
		con = conn;
	}

	public boolean executeUpdate(Connection conn, String command)
			throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(command); // This will throw a SQLException if it
											// fails
			return true;
		} finally {

			// This will run whether we throw an exception or not
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void add(String tweetBy, String tweetText, String tweetLink,
			String geoLocation, int retweet, int favourite, boolean isRetweet,
			String date) {

		try {

			String input = "INSERT INTO tweets(tweetText,tweetlink,tweetgeolocation,tweetby,retweetcount,favouritecount,isretweet,createdat) "
					+ "VALUES ('"
					+ tweetText
					+ "','"
					+ tweetLink
					+ "', "
					+ geoLocation
					+ ",'"
					+ tweetBy
					+ "',"
					+ retweet
					+ ","
					+ favourite + ",'" + isRetweet + "','" + date + "')";
			this.executeUpdate(con, input);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public boolean blacklist(String url) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String sql = "select blacklisturl from urls.blacklist where concat('http://www.',blacklisturl)="
					+ url;
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("hello");
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean test() {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String sql = "select * from urls.resultsystem";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int score=0;
				
				if(rs.getString("isBlacklisted").equals("0")){
					if(rs.getString("Lifeline").equals("1")){
						score+=40;
					}
					if(rs.getString("isFullCircle").equals("1")){
						score+=40;
					}
					if(rs.getString("isASNSame").equals("1")){
						score+=10;
					}
					if(rs.getString("isCountryMalicious").equals("1")){
						score+=10;
					}
				}else{
					score=100;
				}
				//if(rs.getString("ManualResult").equals("N"))
					if(score>50)
				System.out.println(rs.getString("idresultSystem")+", Score: "+score+" , Manual: "+rs.getString("ManualResult"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public ArrayList<String> getMostTweetedLinks() {
		ArrayList<String> links = new ArrayList<String>();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			String sql = "select  tweetlink from urls.tweets where tweetlink not like 'https://twitter%'group by tweetlink order by count(tweetlink) desc LIMIT 50";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("hello");
			while (rs.next()) {
				String link = rs.getString("tweetlink");
				links.add(link);
				// System.out.println(link);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return links;
	}
	public void addResult(String url,String isBlacklisted,String isFullCircle,String isASNSame,String IsCountry,String lifeline) {

		try {
		  
			String input="INSERT INTO resultsystem(URL,isBlacklisted,isFullCircle,isASNSame,isCountryMalicious,Lifeline) "
			+"VALUES ('"+url+"','"+isBlacklisted+"', "+isFullCircle+",'"+isASNSame+"','"+IsCountry+"','"+lifeline+"')";
			this.executeUpdate(con, input);
	    } catch (Exception e) {
			e.printStackTrace();
			return;
		}	
	}
}
