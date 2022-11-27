package pers.juumii.MindTrace.model.service.general;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;

import java.util.HashMap;


class SettingsTest {

    @Test
    public void synchronize(){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        Settings settings = ctx.getBean(Settings.class);
        HashMap<String, Object> kvPairs = new HashMap<>();
        kvPairs.put("TEST", 100);
        settings.setKvPairs(kvPairs);
        settings.synchronize();
    }

    @Test
    public void load(){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        Settings settings = ctx.getBean(Settings.class);
        settings.load();
        System.out.println(settings.getKvPairs());
    }

}