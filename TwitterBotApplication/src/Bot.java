import model.PacMan;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;

/**
 * Arne Cools
 * 06/11/2021
 */
public class Bot {
    public static Twitter twitter = getTwitterInstance();
    public static PacMan pacMan = new PacMan();
    public static Timer timer = new Timer();

    public static void main(String[] args) throws TwitterException, InterruptedException {
        deleteAllPreviousTweets();

        Thread.sleep(60000);
        twitter.updateStatus("A new game will start soon!");
        Thread.sleep(30000);
        pacMan.start();

        twitter.updateStatus(pacMan.getBoard());
        long latestTweetID = getLatestTweet().getId();
        replyToTweet("UP", latestTweetID);
        replyToTweet("LEFT", latestTweetID);
        replyToTweet("DOWN", latestTweetID);
        replyToTweet("RIGHT", latestTweetID);

        timer.scheduleAtFixedRate(new TweetTask(), 60000, 60000);

    }

    private static class TweetTask extends TimerTask {
        @Override
        public void run() {
            try {
                int countUP = 0;
                int countDOWN = 0;
                int countLEFT = 0;
                int countRIGHT = 0;
                List<Status> previousStatuses = getFiveLatestTweets();
                for (Status status : previousStatuses) {
                    switch (status.getText()) {
                        case "LEFT":
                            countLEFT = status.getFavoriteCount();
                            break;
                        case "RIGHT":
                            countRIGHT = status.getFavoriteCount();
                            break;
                        case "UP":
                            countUP = status.getFavoriteCount();
                            break;
                        case "DOWN":
                            countDOWN = status.getFavoriteCount();
                            break;
                    }
                }
                int highestVoted = Math.max(countDOWN, Math.max(countUP, Math.max(countLEFT, countRIGHT)));
                char nextMove = 'w';
                if (highestVoted == countUP) nextMove = 'w';
                else if (highestVoted == countLEFT) nextMove = 'a';
                else if (highestVoted == countDOWN) nextMove = 's';
                else nextMove = 'd';

                pacMan.movePieces(nextMove);

                if (pacMan.checkIfOver() == 0) {
                    twitter.updateStatus(pacMan.getBoard());
                    long latestTweetID = getLatestTweet().getId();
                    replyToTweet("UP", latestTweetID);
                    replyToTweet("DOWN", latestTweetID);
                    replyToTweet("LEFT", latestTweetID);
                    replyToTweet("RIGHT", latestTweetID);
                } else if (pacMan.checkIfOver() == 1){
                    twitter.updateStatus(":(\n" + pacMan.getBoard());
                    replyToTweet("Game Over!", getLatestTweet().getId());
                    timer.cancel();
                } else if (pacMan.checkIfOver() == 2){
                    twitter.updateStatus(":D\n" + pacMan.getBoard());
                    replyToTweet("Congratulations! Player wins!", getLatestTweet().getId());
                    timer.cancel();
                } else {
                    twitter.updateStatus("Error: Something went wrong.\nPlease contact @arnecools3");
                    timer.cancel();
                }

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    private static Status getLatestTweet() {
        List<Status> statusList = new LinkedList<>();
        try {
            statusList = twitter.getUserTimeline("@PacmanBotGame");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return statusList.get(0);
    }

    private static void deleteAllPreviousTweets(){
        List<Status> statusList = new LinkedList<>();
        try {
            statusList = twitter.getUserTimeline("@PacmanBotGame");

            for (Status status : statusList) {
                System.out.println(status.getText());
                twitter.destroyStatus(status.getId());
            }
            System.out.println("All tweets deleted");

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private static List<Status> getFiveLatestTweets() {
        List<Status> statusList = new LinkedList<>();
        try {
            statusList = twitter.getUserTimeline("@PacmanBotGame");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        List<Status> lastFive = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            lastFive.add(statusList.get(i));
        }
        return lastFive;
    }

    private static void replyToTweet(String message, long ID) throws TwitterException {
        StatusUpdate statusUpdate = new StatusUpdate(message);
        statusUpdate.inReplyToStatusId(ID);
        twitter.updateStatus(statusUpdate);
    }

    private static Twitter getTwitterInstance() {
        FileReader fileReader = new FileReader();
        String[] tokensArray = fileReader.readFile();


        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(tokensArray[0])            //first line in config file
                .setOAuthConsumerSecret(tokensArray[1])         //second line in config file
                .setOAuthAccessToken(tokensArray[2])            //third line in config file
                .setOAuthAccessTokenSecret(tokensArray[3]);     //fourth line in config file
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    public static class FileReader {
        public String[] readFile() {
            StringBuilder tokensStringBuilder = new StringBuilder();
            try {
                Path path = Path.of(Objects.requireNonNull(this.getClass().getResource("/config.txt")).toURI());
                File configuration = new File(path.toString());
                Scanner sc = new Scanner(configuration);
                while (sc.hasNextLine()) {
                    tokensStringBuilder.append(sc.nextLine()).append("\n");
                }
                sc.close();
            } catch (FileNotFoundException | URISyntaxException e) {
                System.out.println("config.txt file does not exist: Make sure it's in the resources directory.");
                e.printStackTrace();
            }
            return tokensStringBuilder.toString().split("\n");
        }
    }
}
