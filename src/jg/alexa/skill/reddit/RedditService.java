package jg.alexa.skill.reddit;

import jg.alexa.config.AppConfig;
import jg.alexa.config.ProjectProperties;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.PublicContribution;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.tree.CommentNode;
import net.dean.jraw.tree.RootCommentNode;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Iterator;
import java.util.UUID;

/**
 * Created by jgreely on 3/16/18.
 */
public class RedditService {

    private AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private ProjectProperties properties = (ProjectProperties) context.getBean("projectProperties");

    private final RedditClient reddit;

    private final int PAGE_LIMIT = properties.getRedditPageLimit();

    public RedditService(){
        UserAgent userAgent = new UserAgent("alexa", "jg.alexa", "1.1.0", "Crum_Bum");

        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        UUID uuid = UUID.randomUUID();

        reddit = OAuthHelper.automatic(adapter, Credentials.userlessApp(properties.getRedditClientId(), uuid));
    }

    public DefaultPaginator<Submission> getFrontPage(){
        DefaultPaginator<Submission> paginator = reddit.frontPage()
                .limit(PAGE_LIMIT)
                .sorting(SubredditSort.HOT)
                .build();

        return paginator;
    }

    public DefaultPaginator<Submission> getSubredditPage(String subreddit){
        DefaultPaginator<Submission> paginator = reddit.subreddit(subreddit)
                .posts()
                .limit(PAGE_LIMIT)
                .sorting(SubredditSort.HOT)
                .build();

        return paginator;
    }

    public Iterator<CommentNode<PublicContribution<?>>> getComments(Submission submission){
        String commentId = submission.getId();

        RootCommentNode rootNode = reddit.submission(commentId).comments();

        Iterator<CommentNode<PublicContribution<?>>> comments = rootNode.walkTree().iterator();

        return comments;
    }


}
