package pers.juumii.MindTrace.model.service.structure;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.service.KTree;
import pers.juumii.MindTrace.model.service.KNode;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class KTreeTest {

    @Test
    void getRoot() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        KNode root = kTree.getRoot();
        System.out.println(root);
    }

    @Test
    void getJson() throws IOException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        kTree.setRoot(JsonUtils.readJson(FileUtils.readFileToString(new ClassPathResource("/static/json/data/temp.json").getFile(), StandardCharsets.UTF_8),KNode.class));
        System.out.println(JsonUtils.toJson(kTree.getRoot()));
    }

}