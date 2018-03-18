package jg.alexa.skill.speechlets;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.amazon.speech.speechlet.services.DirectiveServiceClient;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jgreely on 3/16/18.
 */
public class RedditSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    private static final Set<String> supportedApplicationIds;

    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.3e0e1781-88f9-4723-a035-f4466ccbf2eb");
    }

    public RedditSpeechletRequestStreamHandler() {
        super(new RedditSpeechlet(new DirectiveServiceClient()), supportedApplicationIds);
    }

    public RedditSpeechletRequestStreamHandler(Speechlet speechlet,
                                               Set<String> supportedApplicationIds) {
        super(speechlet, supportedApplicationIds);
    }
}
