package jg.alexa.skill.speechlets;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.services.DirectiveService;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;
import jg.alexa.config.AppConfig;
import jg.alexa.config.ProjectProperties;
import jg.alexa.skill.reddit.RedditService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by jgreely on 3/16/18.
 */
public class RedditSpeechlet implements SpeechletV2 {

    private AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private ProjectProperties properties = (ProjectProperties) context.getBean("projectProperties");

    private static final Logger log = LoggerFactory.getLogger(RedditSpeechlet.class);

    private static final String FRONT_PAGE_INTENT = "GetFrontPageIntent";
    private static final String SUBREDDIT_PAGE_INTENT = "GetSubredditPageIntent";
    private static final String HELP_INTENT = "AMAZON.HelpIntent";
    private static final String STOP_INTENT = "AMAZON.StopIntent";
    private static final String CANCEL_INTENT = "AMAZON.CancelIntent";

    private DirectiveService directiveService;
    private final RedditService redditService = new RedditService();

    public RedditSpeechlet(DirectiveService directiveService){ this.directiveService = directiveService; }

    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        SessionStartedRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();

        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        LaunchRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();

        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        return getWelcomeResponse();
    }

    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        log.info("onIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
                requestEnvelope.getSession().getSessionId());

        String intentName = requestEnvelope.getRequest().getIntent().getName();

        if (StringUtils.equalsIgnoreCase(FRONT_PAGE_INTENT, intentName)) {
            //return SpeechletResponse for front page posts
            return null;
        } else if (StringUtils.equalsIgnoreCase(SUBREDDIT_PAGE_INTENT, intentName)) {
            //return SpeechletResponse for subreddit pages
            return null;
        } else if (StringUtils.equalsIgnoreCase(HELP_INTENT, intentName)) {
            // Create the plain text output.
            String speechOutput =
                    "With Alexa for Reddit, you can either get the front page, "
                    + "or get a subreddit.";

            String repromptText = properties.getAlexaHelpRepeat();

            return newAskResponse(speechOutput, false, repromptText, false);
        } else if (StringUtils.equalsIgnoreCase(STOP_INTENT, intentName) || StringUtils.equalsIgnoreCase(CANCEL_INTENT, intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else {
            String outputSpeech = "Sorry, I didn't get that.";
            String repromptText = properties.getAlexaHelpRepeat();

            return newAskResponse(outputSpeech, true, repromptText, true);
        }
    }


    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        SessionEndedRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();

        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any session cleanup logic would go here
    }



    private SpeechletResponse getWelcomeResponse() {
        String speechOutput = "Welcome! Would you like the front page, or a subreddit?";
        // If the user either does not reply to the welcome message or says something that is not
        // understood, they will be prompted again with this text.
        String repromptText =
                "With Alexa for Reddit, you can either get the front page, or a subreddit. Which would you like?";

        return newAskResponse(speechOutput, false, repromptText, false);
    }

    private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
                                             String repromptText, boolean isRepromptSsml) {
        OutputSpeech outputSpeech, repromptOutputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }

        if (isRepromptSsml) {
            repromptOutputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }
}
