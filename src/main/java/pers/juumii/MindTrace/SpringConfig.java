package pers.juumii.MindTrace;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("pers.juumii")
@PropertySource("application.properties")
public class SpringConfig implements WebMvcConfigurer {

}