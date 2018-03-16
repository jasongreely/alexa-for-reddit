package jg.alexa.config;

/**
 * Created by jgreely on 3/16/18.
 */

public class ProjectProperties {

    private String redditUsername;
    private String redditPassword;
    private String redditClientId;
    private String redditClientSecret;

    private int redditPageLimit;

    private String alexaHelpRepeat;

    public String getRedditUsername() {
        return redditUsername;
    }

    public void setRedditUsername(String redditUsername) {
        this.redditUsername = redditUsername;
    }

    public String getRedditPassword() {
        return redditPassword;
    }

    public void setRedditPassword(String redditPassword) {
        this.redditPassword = redditPassword;
    }

    public String getRedditClientId() {
        return redditClientId;
    }

    public void setRedditClientId(String redditClientId) {
        this.redditClientId = redditClientId;
    }

    public String getRedditClientSecret() {
        return redditClientSecret;
    }

    public void setRedditClientSecret(String redditClientSecret) {
        this.redditClientSecret = redditClientSecret;
    }

    public int getRedditPageLimit() {
        return redditPageLimit;
    }

    public void setRedditPageLimit(int redditPageLimit) {
        this.redditPageLimit = redditPageLimit;
    }

    public String getAlexaHelpRepeat(){ return alexaHelpRepeat; }

    public void setAlexaHelpRepeat(String alexaHelpRepeat){ this.alexaHelpRepeat = alexaHelpRepeat; }
}
