import model.PacMan;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
    public static void main(String[] args) throws TwitterException {

        pacMan.start();

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TweetTask(), 15000, 15000 );

        twitter.updateStatus(pacMan.getBoard());

    }

    private static Twitter getTwitterInstance(){
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

    private static class TweetTask extends TimerTask{
        @Override
        public void run() {
            try {
                twitter.updateStatus(pacMan.getBoard());

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    private static Status getLatestTweet(){
        List<Status> statusList = null;
        try {
            statusList = twitter.getUserTimeline("@PacmanBotGame");
        } catch (TwitterException e){
            e.printStackTrace();
        }

        for (Status status : statusList) {
            System.out.println("status = " + status);
        }
        return statusList.get(0);
    }

    public static class FileReader{
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
