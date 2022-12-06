package pers.juumii.MindTrace;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.juumii.MindTrace.utils.Paths;
import pers.juumii.MindTrace.utils.SpringUtils;

@Configuration
@ComponentScan("pers.juumii")
@PropertySource("application.properties")
public class SpringConfig implements WebMvcConfigurer {
    //设置文件虚拟路径映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/img/**").addResourceLocations("file:"+SpringUtils.getBean(Paths.class).getImgRoot());
    }

}