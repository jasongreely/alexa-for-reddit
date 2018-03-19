package jg.alexa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by jgreely on 3/16/18.
 */
@Configuration
@PropertySource("classpath:project.properties")
public class AppConfig {

    @Autowired
    Environment env;

    @Bean
    ProjectProperties projectProperties(){
        ProjectProperties bean = new ProjectProperties();

        bean.setVersion(env.getProperty("version"));

        //Reddit Credentials
        bean.setRedditClientId(env.getProperty("reddit.client_id"));
        bean.setRedditClientSecret(env.getProperty("reddit.client_secret"));

        //RedditService Settings
        bean.setRedditPageLimit(Integer.valueOf(env.getProperty("reddit.page.limit")));
        bean.setRedditPageCeiling(Integer.valueOf(env.getProperty("reddit.page.ceiling")));

        //Alexa Speechlet Messages
        bean.setAlexaHelpRepeat(env.getProperty("alexa.help.repeat"));
        bean.setAlexaMisunderstand(env.getProperty("alexa.misunderstand"));
        bean.setAlexaWelcome(env.getProperty("alexa.welcome"));
        bean.setAlexaExplainIntents(env.getProperty("alexa.explain.intents"));
        bean.setAlexaConnectionError(env.getProperty("alexa.connection.error"));

        return bean;
    }
}
