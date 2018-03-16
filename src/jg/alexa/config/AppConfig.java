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

        bean.setRedditUsername(env.getProperty("reddit.username"));
        bean.setRedditPassword(env.getProperty("reddit.password"));
        bean.setRedditClientId(env.getProperty("reddit.client_id"));
        bean.setRedditClientSecret(env.getProperty("reddit.client_secret"));

        bean.setRedditPageLimit(Integer.valueOf(env.getProperty("reddit.page.limit")));

        bean.setAlexaHelpRepeat(env.getProperty("alexa.help.repeat"));

        return bean;
    }
}
