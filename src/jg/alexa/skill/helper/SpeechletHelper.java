package jg.alexa.skill.helper;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Context;
import com.amazon.speech.speechlet.SpeechletResponse;
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
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jgreely on 3/17/18.
 */
public class SpeechletHelper {

    private static final String PAGE_SLOT = "page";

    private static final Logger log = LoggerFactory.getLogger(SpeechletHelper.class);

    private DirectiveService directiveService;

    public SpeechletHelper(DirectiveService directiveService){ this.directiveService = directiveService; }

    public SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
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

    /**
     * Dispatches a progressive response.
     *
     * @param requestId
     *            the unique request identifier
     * @param text
     *            the text of the progressive response to send
     * @param systemState
     *            the SystemState object
     * @param apiEndpoint
     *            the Alexa API endpoint
     */
    public void dispatchProgressiveResponse(String requestId, String text, SystemState systemState, String apiEndpoint) {
        DirectiveEnvelopeHeader header = DirectiveEnvelopeHeader.builder().withRequestId(requestId).build();
        SpeakDirective directive = SpeakDirective.builder().withSpeech(text).build();
        DirectiveEnvelope directiveEnvelope = DirectiveEnvelope.builder()
                .withHeader(header).withDirective(directive).build();

        if(systemState.getApiAccessToken() != null && !systemState.getApiAccessToken().isEmpty()) {
            String token = systemState.getApiAccessToken();
            try {
                directiveService.enqueue(directiveEnvelope, apiEndpoint, token);
            } catch (Exception e) {
                log.error("Failed to dispatch a progressive response", e);
            }
        }
    }

    /**
     * Helper method that retrieves the system state from the request context.
     * @param context request context.
     * @return SystemState the systemState
     */
    public SystemState getSystemState(Context context) {
        return context.getState(SystemInterface.class, SystemState.class);
    }

    public String getSubredditSlot(Intent intent){
        Slot pageSlot = intent.getSlot(PAGE_SLOT);

        String subreddit = "";

        if(pageSlot != null && StringUtils.isNotBlank(pageSlot.getValue())){
            subreddit = pageSlot.getValue();
            log.info("Retrieved value from page slot: {}", subreddit);
        }

        return subreddit;
    }

    public String getSubmissionSpeech(DefaultPaginator<Submission> paginator, int pageCeiling){
        StringBuilder builder = new StringBuilder();

        for(int x = 0; x < pageCeiling; x++){
            Listing<Submission> submissions = paginator.next();
            for(Submission submission : submissions){
                int roundedScore = getRoundedScore(submission.getScore());

                builder.append("<p>");
                builder.append("From " + submission.getSubreddit() + ", ");
                builder.append(submission.getTitle() + ", " + roundedScore + " points.");
                builder.append("</p>");
            }
        }

        return builder.toString();
    }

    public int getRoundedScore(int points){
        if (points > 999 && points < 10000){
            int offset = (points >= 0) ? 50 : -50;
            return (points + offset) / 100 * 100;
        } else if (points > 9999 && points < 100000){
            int offset = (points >= 0) ? 500 : -500;
            return (points + offset) / 1000 * 1000;
        } else if (points > 99999 && points < 1000000){
            int offset = (points > 0) ? 5000 : -5000;
            return (points + offset) / 10000 * 10000;
        } else {
            return points;
        }
    }
}
