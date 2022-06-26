package bot;

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
    private static final Logger logger = Logger.getLogger(Bot.class);
    public static PacMan pacMan = new PacMan();
    public static Timer timer = new Timer();
    public final static String userName = "@PacmanBotGame";

    public static void main(String[] args) throws TwitterException, InterruptedException {
        logger.info(" Starting bot");
        TwitterUtils.setTwitter(getTwitterInstance());
        logger.info(" Bot started");
        logger.info(" Deleting old tweets");
        TwitterUtils.deleteAllPreviousTweets(userName);
        logger.info(" Old tweets deleted");
        Thread.sleep(60000);
        TwitterUtils.tweet(" A new game will start soon!");
        logger.info(" Tweeted game announcement message");
        Thread.sleep(30000);
        logger.info(" Starting PacMan game");
        pacMan.start();
        logger.info(" PacMan game started");
        logger.info(" Tweeting current board");
        TwitterUtils.tweet(pacMan.getBoard());
        long latestTweetID = TwitterUtils.getLatestTweet(userName).getId();
        logger.info(" Tweeting anwer possibilities");
        TwitterUtils.replyToTweet("UP", latestTweetID);
        TwitterUtils.replyToTweet("LEFT", latestTweetID);
        TwitterUtils.replyToTweet("DOWN", latestTweetID);
        TwitterUtils.replyToTweet("RIGHT", latestTweetID);
        logger.info(" Tweet successfully sent");
        logger.info(" Starting timer for next tweet");
        timer.scheduleAtFixedRate(new TweetTask(), 60000, 60000);

    }

    private static class TweetTask extends TimerTask {
        @Override
        public void run() {
            int countUP = 0;
            int countDOWN = 0;
            int countLEFT = 0;
            int countRIGHT = 0;
            List<Status> previousStatuses = TwitterUtils.getFiveLatestTweets(userName);
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
                TwitterUtils.tweet(pacMan.getBoard());
                long latestTweetID = TwitterUtils.getLatestTweet(userName).getId();
                TwitterUtils.replyToTweet("UP", latestTweetID);
                TwitterUtils.replyToTweet("DOWN", latestTweetID);
                TwitterUtils.replyToTweet("LEFT", latestTweetID);
                TwitterUtils.replyToTweet("RIGHT", latestTweetID);
            } else if (pacMan.checkIfOver() == 1) {
                TwitterUtils.tweet(":(\n" + pacMan.getBoard());
                TwitterUtils.replyToTweet("Game Over!", TwitterUtils.getLatestTweet(userName).getId());
                timer.cancel();
            } else if (pacMan.checkIfOver() == 2) {
                TwitterUtils.tweet(":D\n" + pacMan.getBoard());
                TwitterUtils.replyToTweet("Congratulations! Player wins!", TwitterUtils.getLatestTweet(userName).getId());
                timer.cancel();
            } else {
                TwitterUtils.tweet("Error: Something went wrong.\nPlease contact @arnecools3");
                timer.cancel();
            }
        }
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
                logger.error(" config.txt file does not exist: Make sure it's in the resources directory.");
                e.printStackTrace();
            }
            return tokensStringBuilder.toString().split("\n");
        }
    }
}
