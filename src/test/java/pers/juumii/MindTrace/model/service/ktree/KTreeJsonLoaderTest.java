package pers.juumii.MindTrace.model.service.ktree;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;


class KTreeJsonLoaderTest {

    @Test
    public void create(){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTreeJsonLoader loader = ctx.getBean(KTreeJsonLoader.class);
    }

}