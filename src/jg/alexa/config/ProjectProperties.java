package jg.alexa.config;

/**
 * Created by jgreely on 3/16/18.
 */

public class ProjectProperties {

    private String version;

    //Reddit Credentials
    private String redditClientId;
    private String redditClientSecret;

    //RedditService settings
    private int redditPageLimit;
    private int redditPageCeiling;

    //Alexa Skill Configuration
    private String alexaSkillId;

    //Alexa Speechlet Messages
    private String alexaHelpRepeat;
    private String alexaMisunderstand;
    private String alexaWelcome;
    private String alexaExplainIntents;
    private String alexaConnectionError;

    //Reddit Credentials
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

    //RedditService Settings
    public int getRedditPageLimit() {
        return redditPageLimit;
    }

    public void setRedditPageLimit(int redditPageLimit) {
        this.redditPageLimit = redditPageLimit;
    }

    //Alexa Speechlet Messages
    public String getAlexaHelpRepeat(){
        return alexaHelpRepeat;
    }

    public void setAlexaHelpRepeat(String alexaHelpRepeat){
        this.alexaHelpRepeat = alexaHelpRepeat;

    }

    public String getAlexaMisunderstand(){
        return alexaMisunderstand;
    }

    public void setAlexaMisunderstand(String alexaMisunderstand){
        this.alexaMisunderstand = alexaMisunderstand;
    }

    public String getAlexaWelcome(){
        return alexaWelcome;
    }

    public void setAlexaWelcome(String alexaWelcome){
        this.alexaWelcome = alexaWelcome;
    }

    public String getAlexaExplainIntents(){
        return alexaExplainIntents;
    }

    public void setAlexaExplainIntents(String alexaExplainIntents){
        this.alexaExplainIntents = alexaExplainIntents;
    }

    public int getRedditPageCeiling(){
        return redditPageCeiling;
    }

    public void setRedditPageCeiling(int redditPageCeiling){
        this.redditPageCeiling = redditPageCeiling;
    }

    public String getAlexaConnectionError(){
        return alexaConnectionError;
    }

    public void setAlexaConnectionError(String alexaConnectionError){
        this.alexaConnectionError = alexaConnectionError;
    }

    public String getVersion(){ return version; }

    public void setVersion(String version) { this.version = version; }
}
