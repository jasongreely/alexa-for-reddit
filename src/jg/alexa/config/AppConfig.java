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
    Environment environment;

    @Bean
    ProjectProperties projectProperties(){
        ProjectProperties bean = new ProjectProperties();

        bean.setRedditUsername(environment.getProperty("reddit.username"));
        bean.setRedditPassword(environment.getProperty("reddit.password"));
        bean.setRedditClientId(environment.getProperty("reddit.client_id"));
        bean.setRedditClientSecret(environment.getProperty("reddit.client_secret"));

        return bean;
    }
}
