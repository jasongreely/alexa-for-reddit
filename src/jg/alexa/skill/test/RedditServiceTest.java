package jg.alexa.skill.test;

import jg.alexa.config.AppConfig;
import jg.alexa.config.ProjectProperties;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.*;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;
import net.dean.jraw.tree.CommentNode;
import net.dean.jraw.tree.RootCommentNode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jgreely on 3/16/18.
 */
class RedditServiceTest {

    private AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private ProjectProperties properties = (ProjectProperties) context.getBean("projectProperties");

    Logger log = LoggerFactory.getLogger(RedditServiceTest.class);

    private final int PAGE_LIMIT = properties.getRedditPageLimit();

    private final RedditClient reddit = auth();

    @Test
    public void testAuth(){
        SubredditReference nfl = reddit.subreddit("nfl");
        Subreddit aboutNfl = nfl.about();

        assertNotNull(aboutNfl);
    }

    @Test
    public void getFrontPage(){
        DefaultPaginator<Submission> paginator = reddit.frontPage()
                .limit(PAGE_LIMIT)
                .sorting(SubredditSort.TOP)
                .build();

        assertNotNull(paginator.next());
    }

    @Test
    public void getSubredditPage(){
        String subreddit = "nfl";

        DefaultPaginator<Submission> paginator = reddit.subreddit(subreddit)
                .posts()
                .limit(PAGE_LIMIT)
                .sorting(SubredditSort.HOT)
                .build();

        assertNotNull(paginator);
    }

    @Test
    public void getComments(){
        String commentId = "foo";

        RootCommentNode rootNode = reddit.submission(commentId).comments();

        Iterator<CommentNode<PublicContribution<?>>> comments = rootNode.walkTree().iterator();

        assertNotNull(comments);
    }

    public RedditClient auth(){
        UserAgent userAgent = new UserAgent("alexa", "jg.alexa", "1.1.0", "AlexaForReddit");

        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        UUID uuid = UUID.randomUUID();

        return OAuthHelper.automatic(adapter, Credentials.userless(properties.getRedditClientId(), properties.getRedditClientSecret(), uuid));
    }


}