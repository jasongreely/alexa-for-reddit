package jg.alexa.skill.reddit;

import jg.alexa.config.AppConfig;
import jg.alexa.config.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by jgreely on 3/16/18.
 */
public class RedditService {

    private AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private ProjectProperties properties = (ProjectProperties) context.getBean("projectProperties");

    public RedditService(){

    }


}
