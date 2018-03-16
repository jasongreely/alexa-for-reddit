package jg.alexa.skill.test;

import jg.alexa.config.AppConfig;
import jg.alexa.config.ProjectProperties;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jgreely on 3/16/18.
 */
class RedditServiceTest {

    private AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    private ProjectProperties properties = (ProjectProperties) context.getBean("projectProperties");

    Logger log = LoggerFactory.getLogger(RedditServiceTest.class);

    @Test
    public void testAuth(){
        System.out.println(properties.getRedditUsername());
    }
}