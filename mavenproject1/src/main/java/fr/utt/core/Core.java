/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utt.core;

import fr.utt.dataset.utils.CSVUtils;
import fr.utt.entities.Scorer;
import fr.utt.entities.UserFeatures;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.swing.JFrame;
import org.math.plot.Plot3DPanel;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 *
 * @author kevin
 */
public class Core {

    private static List<Scorer> scorers = new ArrayList<>();
    private static final int MYTHREADS = 700;
    private String OAuthConsumerKey, OAuthConsumerSecret, OAuthAccessToken, OAuthAccessTokenSecret;

    public Core(String OAuthConsumerKey, String OAuthConsumerSecret, String OAuthAccessToken, String OAuthAccessTokenSecret) {
        this.OAuthConsumerKey = OAuthConsumerKey;
        this.OAuthConsumerSecret = OAuthConsumerSecret;
        this.OAuthAccessToken = OAuthAccessToken;
        this.OAuthAccessTokenSecret = OAuthAccessTokenSecret;
    }

    public List<Scorer> Collect(int timer) throws InterruptedException, IOException {
//         //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//          java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Dashboard().setVisible(true);
//            }
//        });
        ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(this.OAuthConsumerKey)
                .setOAuthConsumerSecret(this.OAuthConsumerSecret)
                .setOAuthAccessToken(this.OAuthAccessToken)
                .setOAuthAccessTokenSecret(this.OAuthAccessTokenSecret);
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        Twitter twitter = new TwitterFactory().getInstance(twitterStream.getAuthorization());

        String csvFile = "C:\\Users\\kevin\\Documents\\NetBeansProjects\\mavenproject1\\src\\main\\java\\fr\\utt\\dataset\\users_analysed.csv";
        String line = "";
        String cvsSplitBy = ",";
        String[] users = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                users = line.split(cvsSplitBy);
                System.out.println("" + users[0]);
                Runnable worker = new MyRunnable(users[0], twitterStream, twitter, timer);
                executor.execute(worker);
            }
            executor.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Wait until all threads are finish
        while (!executor.isTerminated()) {

        }
        System.out.println("\nFinished all threads");
        String scoreFile = "C:\\Users\\kevin\\Documents\\NetBeansProjects\\mavenproject1\\src\\main\\java\\fr\\utt\\dataset\\scores.csv";
        FileWriter writer = null;
        try {
            writer = new FileWriter(scoreFile);

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
        int i = 0;
        double[] x = new double[scorers.size()];
        double[] y = new double[scorers.size()];
        double[] z = new double[scorers.size()];
        for (Scorer sc : scorers) {
            List<String> list = new ArrayList<>();
            list.add("" + sc.getDangerosity());
            list.add("" + sc.getAggressivity());
            list.add("" + sc.getVisibilty());
//            x[i] = sc.getDangerosity();
//            y[i] = sc.getAggressivity();
//            z[i] = sc.getVisibilty();
//            i++;
            CSVUtils.writeLine(writer, list);

//            System.out.println(sc.getDangerosity() + "-" + sc.getAggressivity() + "-" + sc.getVisibilty());

        }
//        // create your PlotPanel (you can use it as a JPanel)
//        Plot3DPanel plot = new Plot3DPanel();
//
//        // define the legend position
//        plot.addLegend("SOUTH");
//
//        // add a line plot to the PlotPanel
//        plot.addScatterPlot("my plot", x, y, z);
//        plot.setAxisLabel(0, "Dangerosité");
//        plot.setAxisLabel(1, "Agressivité");
//        plot.setAxisLabel(2, "Visibilité");

//        // put the PlotPanel in a JFrame like a JPanel
//        JFrame frame = new JFrame("a plot panel");
//        frame.setSize(600, 600);
//        frame.setContentPane(plot);
//        frame.setVisible(true);
        return scorers;
    }

    public static void processing(String user, TwitterStream twitterStream, Twitter twitter) {

        int pageno = 1;
        int nbFriends = 0;
        int nbTweets = 0;
        List statuses = new ArrayList();

//        while (true) {
        try {
            User usera = twitter.showUser(user);
            int size = statuses.size();
            Paging page = new Paging(pageno, 30);
            statuses.addAll(twitter.getUserTimeline(user, page));
            TimeUnit.SECONDS.sleep(1);
            User userb = twitter.showUser(user);
            nbFriends = userb.getFriendsCount() - usera.getFriendsCount();
            nbTweets = userb.getStatusesCount() - usera.getStatusesCount();

        } catch (TwitterException e) {
            if (e.getStatusCode() == 404) {
            }
            e.printStackTrace();
        } catch (InterruptedException ex) {
//            java.util.logging.Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
        }
//        }
        System.out.println("Total: " + statuses.size());
        UserFeatures userFeatures = new UserFeatures();
        int nbHashtag = 0;
        int mention = 0;
        int maliciousUrl = 0;
        for (Object status : statuses) {
            userFeatures.setId(((Status) status).getUser().getId());
            userFeatures.setNbfriends(nbFriends);
            userFeatures.setNbtweets(nbTweets);

            nbHashtag += ((Status) status).getHashtagEntities().length;
            mention += ((Status) status).getUserMentionEntities().length;
            if (((Status) status).getURLEntities().length > 0) {
                for (URLEntity entity : ((Status) status).getURLEntities()) {
                    String url = entity.getURL();
                    try {
                        URL urld = new URL(url);
                        URLConnection conn = urld.openConnection();
                        conn.connect();
                    } catch (MalformedURLException e) {
                        maliciousUrl++;
                    } catch (IOException e) {
                        // the connection couldn't be established
                    }
                }
            };

        }

        userFeatures.setMaliciousEntity(maliciousUrl);
        if (statuses.size() != 0) {
            userFeatures.setNbhashtags(nbHashtag / statuses.size());
            userFeatures.setNbmentionEntities(mention / statuses.size());
        } else {
            userFeatures.setNbhashtags(0);
            userFeatures.setNbmentionEntities(0);
        }
        Scorer scorer = new Scorer(userFeatures);
        scorer.setDangerosity(statuses.size());
        scorers.add(scorer);

    }

    public UserFeatures getFeatures(Status status) {
        User user = status.getUser();
        UserFeatures userFeatures = new UserFeatures();

        try {
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

            if (status.getHashtagEntities() != null && status.getHashtagEntities().length > 0) {
                userFeatures.setNbhashtags(status.getHashtagEntities().length);

            }

            if (status.getContributors() != null && status.getContributors().length > 0) {
                userFeatures.setNbcontributors(status.getContributors().length);

            }
            if (status.getMediaEntities() != null && status.getMediaEntities().length > 0) {
                userFeatures.setNbmediaEntities(status.getMediaEntities().length);

            }

            if (status.getSymbolEntities() != null && status.getSymbolEntities().length > 0) {
                userFeatures.setNbsymbolEntities(status.getSymbolEntities().length);

            }
            if (status.getURLEntities() != null && status.getURLEntities().length > 0) {
                userFeatures.setMaliciousEntity(status.getURLEntities().length);

            }
            if (status.getUserMentionEntities() != null && status.getUserMentionEntities().length > 0) {
                userFeatures.setNbmentionEntities(status.getUserMentionEntities().length);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userFeatures;
    }
    ;
    private static final UserStreamListener listener = new UserStreamListener() {
        @Override
        public void onStatus(Status status) {
            System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
        }

        @Override
        public void onDeletionNotice(long directMessageId, long userId) {
            System.out.println("Got a direct message deletion notice id:" + directMessageId);
        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            System.out.println("Got a track limitation notice:" + numberOfLimitedStatuses);
        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {
            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        }

        @Override
        public void onStallWarning(StallWarning warning) {
            System.out.println("Got stall warning:" + warning);
        }

        @Override
        public void onFriendList(long[] friendIds) {
            System.out.print("onFriendList");
            for (long friendId : friendIds) {
                System.out.print(" " + friendId);
            }
            System.out.println();
        }

        @Override
        public void onFavorite(User source, User target, Status favoritedStatus) {
            System.out.println("onFavorite source:@"
                    + source.getScreenName() + " target:@"
                    + target.getScreenName() + " @"
                    + favoritedStatus.getUser().getScreenName() + " - "
                    + favoritedStatus.getText());
        }

        @Override
        public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
            System.out.println("onUnFavorite source:@"
                    + source.getScreenName() + " target:@"
                    + target.getScreenName() + " @"
                    + unfavoritedStatus.getUser().getScreenName()
                    + " - " + unfavoritedStatus.getText());
        }

        @Override
        public void onFollow(User source, User followedUser) {
            System.out.println("onFollow source:@"
                    + source.getScreenName() + " target:@"
                    + followedUser.getScreenName());
        }

        @Override
        public void onUnfollow(User source, User followedUser) {
            System.out.println("onFollow source:@"
                    + source.getScreenName() + " target:@"
                    + followedUser.getScreenName());
        }

        @Override
        public void onDirectMessage(DirectMessage directMessage) {
            System.out.println("onDirectMessage text:"
                    + directMessage.getText());
        }

        @Override
        public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
            System.out.println("onUserListMemberAddition added member:@"
                    + addedMember.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
            System.out.println("onUserListMemberDeleted deleted member:@"
                    + deletedMember.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
            System.out.println("onUserListSubscribed subscriber:@"
                    + subscriber.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
            System.out.println("onUserListUnsubscribed subscriber:@"
                    + subscriber.getScreenName()
                    + " listOwner:@" + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListCreation(User listOwner, UserList list) {
            System.out.println("onUserListCreated  listOwner:@"
                    + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListUpdate(User listOwner, UserList list) {
            System.out.println("onUserListUpdated  listOwner:@"
                    + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserListDeletion(User listOwner, UserList list) {
            System.out.println("onUserListDestroyed  listOwner:@"
                    + listOwner.getScreenName()
                    + " list:" + list.getName());
        }

        @Override
        public void onUserProfileUpdate(User updatedUser) {
            System.out.println("onUserProfileUpdated user:@" + updatedUser.getScreenName());
        }

        @Override
        public void onUserDeletion(long deletedUser) {
            System.out.println("onUserDeletion user:@" + deletedUser);
        }

        @Override
        public void onUserSuspension(long suspendedUser) {
            System.out.println("onUserSuspension user:@" + suspendedUser);
        }

        @Override
        public void onBlock(User source, User blockedUser) {
            System.out.println("onBlock source:@" + source.getScreenName()
                    + " target:@" + blockedUser.getScreenName());
        }

        @Override
        public void onUnblock(User source, User unblockedUser) {
            System.out.println("onUnblock source:@" + source.getScreenName()
                    + " target:@" + unblockedUser.getScreenName());
        }

        @Override
        public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
            System.out.println("onRetweetedRetweet source:@" + source.getScreenName()
                    + " target:@" + target.getScreenName()
                    + retweetedStatus.getUser().getScreenName()
                    + " - " + retweetedStatus.getText());
        }

        @Override
        public void onFavoritedRetweet(User source, User target, Status favoritedRetweet) {
            System.out.println("onFavroitedRetweet source:@" + source.getScreenName()
                    + " target:@" + target.getScreenName()
                    + favoritedRetweet.getUser().getScreenName()
                    + " - " + favoritedRetweet.getText());
        }

        @Override
        public void onQuotedTweet(User source, User target, Status quotingTweet) {
            System.out.println("onQuotedTweet" + source.getScreenName()
                    + " target:@" + target.getScreenName()
                    + quotingTweet.getUser().getScreenName()
                    + " - " + quotingTweet.getText());
        }

        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();
            System.out.println("onException:" + ex.getMessage());
        }
    };

    public static class MyRunnable implements Runnable {

        private String user;
        private TwitterStream twitterStream;
        private Twitter twitter;
        private int timer;

        MyRunnable(String user, TwitterStream twitterStream, Twitter twitter, int timer) {
            this.user = user;
            this.twitterStream = twitterStream;
            this.twitter = twitter;
            this.timer = timer;
        }

        @Override
        public void run() {

            int pageno = 1;
            int nbFriends = 0;
            int totalFriends = 0;
            int nbTweets = 0;
            List statuses = new ArrayList();

//        while (true) {
            try {
                User usera = twitter.showUser(user);
                int size = statuses.size();
                Paging page = new Paging(pageno, 30);
                statuses.addAll(twitter.getUserTimeline(user));
                TimeUnit.SECONDS.sleep(this.timer * 60);
                User userb = twitter.showUser(user);
                nbFriends = userb.getFriendsCount() - usera.getFriendsCount();
                totalFriends = userb.getFriendsCount();
                nbTweets = userb.getStatusesCount() - usera.getStatusesCount();

            } catch (TwitterException e) {
                if (e.getStatusCode() == 404) {
                }
                e.printStackTrace();
            } catch (InterruptedException ex) {
//            java.util.logging.Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            }
//        }
            System.out.println("Total: " + statuses.size());
            UserFeatures userFeatures = new UserFeatures();
            int nbHashtag = 0;
            int mention = 0;
            int maliciousUrl = 0;
            for (Object status : statuses) {
                userFeatures.setId(((Status) status).getUser().getId());
                userFeatures.setNbfriends(nbFriends);
                userFeatures.setNbtweets(nbTweets);

                nbHashtag += ((Status) status).getHashtagEntities().length;
                mention += ((Status) status).getUserMentionEntities().length;
                if (((Status) status).getURLEntities().length > 0) {
                    for (URLEntity entity : ((Status) status).getURLEntities()) {
                        String url = entity.getURL();
                        try {
                            URL urld = new URL(url);
                            URLConnection conn = urld.openConnection();
                            conn.connect();
                        } catch (MalformedURLException e) {
                            maliciousUrl++;
                        } catch (IOException e) {
                            // the connection couldn't be established
                        }
                    }
                };

            }

            userFeatures.setMaliciousEntity(maliciousUrl);
            if (statuses.size() != 0) {
                userFeatures.setNbhashtags(nbHashtag / statuses.size());
                userFeatures.setNbmentionEntities(mention / statuses.size());
            } else {
                userFeatures.setNbhashtags(0);
                userFeatures.setNbmentionEntities(0);
            }
            userFeatures.setNbfriends(totalFriends);
            Scorer scorer = new Scorer(userFeatures);
            scorer.setDangerosity(20 * 10);
            scorers.add(scorer);
        }
    }
}
