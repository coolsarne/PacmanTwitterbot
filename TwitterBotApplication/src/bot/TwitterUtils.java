package bot;

import twitter4j.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Arne Cools
 * 24/06/2022
 */
public class TwitterUtils {
    private static final Logger logger = Logger.getLogger(Bot.class);
    private static Twitter twitter;

    public static void setTwitter(Twitter twitter) {
        TwitterUtils.twitter = twitter;
    }

    public static Twitter getTwitter() {
        return twitter;
    }

    public static List<Status> getUserTimeLine(String userName) {
        List<Status> statuses = null;
        try {
            statuses = twitter.getUserTimeline(userName);
        } catch (NullPointerException e) {
            logger.error(" Twitter is null");
            logger.error(" Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        } catch (TwitterException e) {
            logger.error(" Error getting user timeline");
            logger.error(" Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        }
        return statuses;
    }

    public static Status getLatestTweet(String userName) {
        List<Status> statusList = new LinkedList<>();
        statusList = getUserTimeLine(userName);
        return statusList.get(0);
    }

    public static void tweet(String message) {
        try {
            StatusUpdate status = new StatusUpdate(message);
            twitter.updateStatus(status);
        } catch (NullPointerException e) {
            logger.error(" Twitter is not set");
            logger.error(" Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        } catch (TwitterException e) {
            logger.error(" Error while tweeting");
            logger.error(" Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        }

    }

    public static void replyToTweet (String message,long ID){
        StatusUpdate statusUpdate = new StatusUpdate(message);
        statusUpdate.inReplyToStatusId(ID);
        try {
            twitter.updateStatus(statusUpdate);
        } catch (NullPointerException e) {
            logger.error(" Twitter is not set");
            logger.error(" Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        } catch (TwitterException e) {
            logger.error(" Error while tweeting");
            logger.error(" Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void deleteAllPreviousTweets(String userName) {
        List<Status> statusList = new LinkedList<>();
        statusList = getUserTimeLine(userName);

        for (Status status : statusList) {
            logger.warn((" Deleting tweet: \"" + status.getText() + "\"").replaceAll("\n", " "));
            destroyStatus(status.getId());
        }

    }

    public static List<Status> getFiveLatestTweets(String userName) {
        List<Status> statusList = new LinkedList<>();
        statusList = TwitterUtils.getUserTimeLine(userName);

        List<Status> lastFive = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            lastFive.add(statusList.get(i));
        }
        return lastFive;
    }


    public static void destroyStatus(long id) {
        try {
            twitter.destroyStatus(id);
        } catch (TwitterException e) {
            logger.error(" Error while destroying status");
            logger.error(" Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        }
    }
}