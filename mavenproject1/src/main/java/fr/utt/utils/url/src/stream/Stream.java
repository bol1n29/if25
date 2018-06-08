package fr.utt.utils.url.src.stream;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import twitter4j.FilterQuery;
import twitter4j.HashtagEntity;
import twitter4j.PagableResponseList;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;

public class Stream {

    static Query query = new Query("screen_name =");
    static TwitterStream twitter;
    static Connection con;
    static TwitterFactory tf;
    static String ConsumerKey = "6pFDyGerdcGlDPqmFyBaX3GtE";
    static String ConsumerSecret = "ysvAstva9zO1CAGUYWa1oObQaZ6DHGz6VcQd0IHFKS3ddI4Ofq";
    static String oauth_token = "825802692744314880-c70kUWGse7Sc5BszwOeJvCTYwQhcqhO";
    static String oauth_token_secret = "QHQd6wBZQaqZz0zCXzG6Uam1XQD2m6Eh4IwIyLlbpeegt";

    public static void main(String args[]) throws IOException {
        Connection con = mySQLInit("root", "");
        executeRequest(con);
        //exit(con);
    }

    public static void executeRequest(Connection con) throws IOException {
        // ***********************************************SET CONNECTION******************************************

        try {
            ConfigurationBuilder cb = new ConfigurationBuilder(); //ConfigurationBuilder declared cb is a new instance of this
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(ConsumerKey) //sets the Consumer Key String
                    .setOAuthConsumerSecret(ConsumerSecret) //sets the Consumer Secret String
                    .setOAuthAccessToken(oauth_token)
                    .setOAuthAccessTokenSecret(oauth_token_secret);
            cb.setJSONStoreEnabled(true);

            twitter = new TwitterStreamFactory(cb.build()).getInstance();

        } catch (Exception e) {

            System.out.println("could not set consumer cred:" + e.getMessage());
            e.printStackTrace();
            System.exit(0);

        }
        PagableResponseList<User> result = null;

        StatusListener listener;
        listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatus(Status arg0) {
                // TODO Auto-generated method stub
                System.out.println(arg0);
                addUser(con, arg0);

            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }

        };

        FilterQuery filter = new FilterQuery();
        String[] kwds = {"playstation", "xbox"};
        filter.track(kwds);

        twitter.addListener(listener);

        twitter.filter(filter);
        //QueryResult search = twitter.search(new Query("obama"));

        //int sl = 0;
        /*	if(result.getRateLimitStatus().getRemaining()<1)
				try{
			Thread.sleep(15*60*1000);
			sl++;
			System.out.println("sleep: "+sl);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}*/
    }

    public static Connection mySQLInit(String username, String pass) {
        String SQLURL = "com.mysql.jdbc.Driver";
        Connection con = null;
        Statement st = null;
        try {
            Class.forName(SQLURL);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String connectionString = "jdbc:mysql://localhost?character_set_server=utf8mb4&characterSetResults=utf8&&characterEncoding=utf-8";

        try {
            con = DriverManager.getConnection(connectionString, username, pass);
            st = con.createStatement();
            st.execute("use stream;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

///------------------------------------------------------------------------------------------------------
    public static void exit(Connection con) {

        try {

            con.close();

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

    }

    private static void addUser(Connection con, Status status) {
        User user = status.getUser();
        try {

            Date currentDate = new Date();
            java.text.SimpleDateFormat sdf
                    = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateString = sdf.format(currentDate);
            long currentTime = currentDate.getTime();
            String userDate = sdf.format(user.getCreatedAt());
            Date d = new Date();
            Timestamp ts = new Timestamp(d.getTime());
            String toReturn = "insert into user_instance values (DEFAULT," + user.getAccessLevel() + ","
                    + "'" + entirelyInBasicMultilingualPlane(user.getDescription()) + "'," + user.getFavouritesCount() + "," + user.getFollowersCount() + ","
                    + user.getFriendsCount() + "," + user.getId() + ",'" + entirelyInBasicMultilingualPlane(user.getLang()) + "'," + user.getListedCount() + ",'"
                    + entirelyInBasicMultilingualPlane(user.getLocation()) + "','" + entirelyInBasicMultilingualPlane(user.getName()) + "','"
                    + entirelyInBasicMultilingualPlane(user.getProfileLinkColor()) + "','" + entirelyInBasicMultilingualPlane(user.getProfileSidebarBorderColor()) + "','" + entirelyInBasicMultilingualPlane(user.getProfileSidebarBorderColor()) + "','"
                    + entirelyInBasicMultilingualPlane(user.getProfileTextColor()) + "','" + entirelyInBasicMultilingualPlane(user.getScreenName()) + "','"
                    + user.getStatusesCount() + "','" + entirelyInBasicMultilingualPlane(user.getTimeZone()) + "'," + user.getUtcOffset() + "," + user.isContributorsEnabled() + ","
                    + user.isDefaultProfile() + "," + user.isDefaultProfileImage() + "," + user.isFollowRequestSent() + "," + user.isGeoEnabled() + "," + user.isProfileBackgroundTiled() + ","
                    + user.isProfileUseBackgroundImage() + ", " + user.isProtected() + "," + user.isShowAllInlineMedia() + "," + user.isTranslator() + ","
                    + user.isVerified() + ",'" + userDate + "'," + user.getCreatedAt().getTime() + ",0,'" + currentDateString + "'," + currentTime + ",NULL,0);\n";

            Statement st = con.createStatement();
            st.executeUpdate(toReturn);

            long idsCount = status.getUser().getFollowersCount();
            String longitude = null;
            String latitude = null;
            try {
                longitude = String.valueOf(status.getGeoLocation().getLongitude());
                latitude = String.valueOf(status.getGeoLocation().getLatitude());
            } catch (Exception ex1) {
            }
            long retweetedFrom = -1;
            try {
                retweetedFrom = status.getRetweetedStatus().getId();

            } catch (Exception tw) {
            }
            long originalTweeterid = -1;
            String originalTweeterScr = null;
            String originalTweeterName = null;
            try {
                originalTweeterid = status.getRetweetedStatus().getUser().getId();
                originalTweeterScr = status.getRetweetedStatus().getUser().getScreenName();
                originalTweeterName = status.getRetweetedStatus().getUser().getName();
            } catch (Exception tw2) {
            }
            String InReplyToUserId = null;
            try {
                InReplyToUserId = String.valueOf(status.getInReplyToUserId());
            } catch (Exception tw2) {
            }
            long twitterTime = status.getCreatedAt().getTime();
            String twitterDateString = sdf.format(status.getCreatedAt());

            int nbhashtags = 0;
            int nbcontributors = 0;
            int nbmediaEntities = 0;
            int nbextendedMediaEntities = 0;
            int nbsymbolEntities = 0;
            int nbURLEntities = 0;
            int nbmentionEntities = 0;
            if (status.getHashtagEntities() != null && status.getHashtagEntities().length > 0) {
                nbhashtags = status.getHashtagEntities().length;

            }

            if (status.getContributors() != null && status.getContributors().length > 0) {
                nbcontributors = status.getContributors().length;

            }
            if (status.getMediaEntities() != null && status.getMediaEntities().length > 0) {
                nbmediaEntities = status.getMediaEntities().length;

            }

            if (status.getSymbolEntities() != null && status.getSymbolEntities().length > 0) {
                nbsymbolEntities = status.getSymbolEntities().length;

            }
            if (status.getURLEntities() != null && status.getURLEntities().length > 0) {
                nbURLEntities = status.getURLEntities().length;

            }
            if (status.getUserMentionEntities() != null && status.getUserMentionEntities().length > 0) {
                nbmentionEntities = status.getUserMentionEntities().length;

            }
            st = con.createStatement();
            st.executeUpdate(addTweet(status, nbhashtags, nbcontributors, nbmediaEntities, nbextendedMediaEntities, nbsymbolEntities, nbURLEntities, nbmentionEntities, retweetedFrom, originalTweeterid, originalTweeterScr, originalTweeterName, currentDateString, currentTime, twitterDateString, twitterTime));
            if (status.getHashtagEntities().length > 0) {
                String h = addHashtag(user.getName(), user.getScreenName(), status.getId(), status.getHashtagEntities(), status.getUser().getId(), currentDateString, currentTime, twitterDateString, twitterTime);
                st = con.createStatement();
                st.execute("insert into hashtag values " + h.substring(0, h.length() - 1) + ";");

            }
            if (status.getUserMentionEntities().length > 0) {
                String m = addMentionEntity(status.getUser().getName(), status.getUser().getScreenName(), status.getId(), status.getUserMentionEntities(),
                        status.getUser().getId(), currentDateString, currentTime, twitterDateString, twitterTime);
                st = con.createStatement();
                st.execute("insert into mentionentity values " + m.substring(0, m.length() - 1) + ";");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String addHashtag(String username, String userScr, long tweetid, HashtagEntity[] hashtagEntities,
            long userid, String currentDate, long currentTime, String twitterDate, long twitterTime) {
        // TODO Auto-generated method stub
        String toReturn = "";
        for (HashtagEntity he : hashtagEntities) {

            toReturn = toReturn.concat("(DEFAULT,'" + entirelyInBasicMultilingualPlane(username) + "','" + entirelyInBasicMultilingualPlane(userScr) + "','" + entirelyInBasicMultilingualPlane(he.getText()) + "'," + he.getStart() + "," + he.getEnd() + "," + tweetid + "," + userid + ",'" + currentDate + "'," + currentTime + ",'" + twitterDate + "'," + twitterTime + "),");
        }
        return toReturn;

    }

    public static String addTweet(Status status, int nbhashtags, int nbcontributors, int nbmediaEntities, int nbextendedMediaEntities, int nbsymbolEntities,
            int nbURLEntities, int nbmentionEntities, long retweetedFrom, long originalTweeterId, String originalTweeterName, String originalTweeterscr, String currentDate, long currentTime, String twitterDate, long twitterTime) {
        String toReturn = "";
        String longitude = null;
        String lattitude = null;

        java.text.SimpleDateFormat sdf
                = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            longitude = String.valueOf(status.getGeoLocation().getLongitude());
            lattitude = String.valueOf(status.getGeoLocation().getLatitude());
        } catch (Exception ex) {

        }

        toReturn = toReturn.concat("insert into tweet values (DEFAULT, " + status.getCurrentUserRetweetId() + ","
                + status.getFavoriteCount() + ",'" + longitude + "','" + lattitude + "'," + status.getId() + ",'" + entirelyInBasicMultilingualPlane(status.getInReplyToScreenName()) + "',"
                + status.getInReplyToStatusId() + "," + status.getInReplyToUserId() + ",'" + entirelyInBasicMultilingualPlane(status.getLang()) + "'," + status.getRetweetCount() + ",'"
                + entirelyInBasicMultilingualPlane(status.getSource()) + "','" + entirelyInBasicMultilingualPlane(status.getText()) + "'," + status.isFavorited() + "," + status.isPossiblySensitive() + "," + status.isRetweet() + ","
                + status.isRetweeted() + "," + status.isTruncated() + "," + nbhashtags + "," + nbcontributors + "," + nbmediaEntities + "," + nbextendedMediaEntities + ","
                + nbsymbolEntities + "," + nbURLEntities + "," + nbmentionEntities + "," + retweetedFrom + "," + originalTweeterId + "," + "'" + entirelyInBasicMultilingualPlane(originalTweeterscr) + "','" + entirelyInBasicMultilingualPlane(originalTweeterName) + "'," + status.getUser().getId() + ",'" + entirelyInBasicMultilingualPlane(status.getUser().getScreenName()) + "','" + entirelyInBasicMultilingualPlane(status.getUser().getName()) + "','" + currentDate + "'," + currentTime + ",'" + twitterDate + "'," + twitterTime + ");\n");
        return toReturn;

    }

    public static String addMentionEntity(String username, String userScr, long tweetid,
            UserMentionEntity[] userMentionEntities, long userid,
            String currentDate, long currentTime, String twitterDate, long twitterTime) {
        String toReturn = "";
        for (UserMentionEntity mention : userMentionEntities) {

            toReturn = toReturn.concat("(DEFAULT," + tweetid + "," + userid + ",'" + entirelyInBasicMultilingualPlane(username) + "','" + entirelyInBasicMultilingualPlane(userScr) + "'," + mention.getEnd() + "," + mention.getId() + ",'" + entirelyInBasicMultilingualPlane(mention.getName()) + "','"
                    + entirelyInBasicMultilingualPlane(mention.getScreenName()) + "'," + mention.getStart() + ",'" + entirelyInBasicMultilingualPlane(mention.getText()) + "'"
                    + ",'" + currentDate + "'," + currentTime + ",'" + twitterDate + "'," + twitterTime + "),");

        }

        return toReturn;

    }

    public static String entirelyInBasicMultilingualPlane(String text) {
        if (text != null) {
            String toReturn = "";
            text = text.replace("\\", "");
            text = text.replace("'", "");
            text = StringEscapeUtils.escapeSql(text);
            for (int i = 0; i < text.length(); i++) {
                if (Character.isSurrogate(text.charAt(i))) {
                    toReturn = toReturn.concat(" ");
                } else {
                    toReturn = toReturn + text.charAt(i);
                }
            }
            return toReturn;
        }
        return "";
    }

}
