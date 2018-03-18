package jg.alexa.skill.speechlets;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.interfaces.system.SystemInterface;
import com.amazon.speech.speechlet.interfaces.system.SystemState;
import com.amazon.speech.speechlet.services.DirectiveEnvelope;
import com.amazon.speech.speechlet.services.DirectiveEnvelopeHeader;
import com.amazon.speech.speechlet.services.DirectiveService;
import com.amazon.speech.speechlet.services.SpeakDirective;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;
import jg.alexa.config.AppConfig;
import jg.alexa.config.ProjectProperties;
import jg.alexa.skill.helper.SpeechletHelper;
import jg.alexa.skill.reddit.RedditService;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;
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
    private final SpeechletHelper speechletHelper;
    private final RedditService redditService;

    public RedditSpeechlet(DirectiveService directiveService){
        this.directiveService = directiveService;
        speechletHelper = new SpeechletHelper(directiveService);
        redditService = new RedditService();
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        SessionStartedRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();

        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        LaunchRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();

        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        log.info("onIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
                requestEnvelope.getSession().getSessionId());

        String intentName = requestEnvelope.getRequest().getIntent().getName();

        if (StringUtils.equalsIgnoreCase(FRONT_PAGE_INTENT, intentName)) {
            //return SpeechletResponse for front page posts
            return handleFrontPageRequest(requestEnvelope);
        } else if (StringUtils.equalsIgnoreCase(HELP_INTENT, intentName)) {
            // Create the plain text output.
            String speechOutput =
                    "With Alexa for Reddit, you can either get the front page, "
                    + "or get a subreddit.";

            String repromptText = properties.getAlexaHelpRepeat();

            return speechletHelper.newAskResponse(speechOutput, false, repromptText, false);
        } else if (StringUtils.equalsIgnoreCase(STOP_INTENT, intentName) || StringUtils.equalsIgnoreCase(CANCEL_INTENT, intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");

            return SpeechletResponse.newTellResponse(outputSpeech);
        } else {
            String outputSpeech = properties.getAlexaMisunderstand();
            String repromptText = properties.getAlexaHelpRepeat();

            return speechletHelper.newAskResponse(outputSpeech, true, repromptText, true);
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        SessionEndedRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();

        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any session cleanup logic would go here
    }

    private SpeechletResponse handleFrontPageRequest(SpeechletRequestEnvelope<IntentRequest> requestEnvelope){
        IntentRequest request = requestEnvelope.getRequest();
        Session session = requestEnvelope.getSession();
        SystemState systemState = speechletHelper.getSystemState(requestEnvelope.getContext());
        String apiEndpoint = systemState.getApiEndpoint();

        String speechPrefix = "<p>Here are the top hot posts from the front page-</p>";

        // Dispatch a progressive response to engage the user while fetching events
        speechletHelper.dispatchProgressiveResponse(request.getRequestId(), "Searching", systemState, apiEndpoint);
        DefaultPaginator<Submission> frontPaginator = redditService.getFrontPage();

        StringBuilder speechBuilder = new StringBuilder();
        if(frontPaginator != null){
            speechBuilder.append(speechPrefix);

            for(int x = 0; x < properties.getRedditPageCeiling(); x++){
                Listing<Submission> submissions = frontPaginator.next();
                for(Submission submission : submissions){
                    speechBuilder.append("<p>");
                    speechBuilder.append("From " + submission.getSubredditFullName() + ", ");
                    speechBuilder.append(submission.getTitle() + ", " + submission.getScore() + " points.");
                    speechBuilder.append("</p>");
                }
            }
        } else {
            speechBuilder.append(properties.getAlexaConnectionError());
        }

        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setSsml("<speak>" + speechBuilder.toString() + "</speak>");

        return SpeechletResponse.newTellResponse(outputSpeech);
    }

    private SpeechletResponse getWelcomeResponse() {
        String speechOutput = properties.getAlexaWelcome();
        // If the user either does not reply to the welcome message or says something that is not
        // understood, they will be prompted again with this text.
        String repromptText = properties.getAlexaExplainIntents();

        return speechletHelper.newAskResponse(speechOutput, false, repromptText, false);
    }
}
