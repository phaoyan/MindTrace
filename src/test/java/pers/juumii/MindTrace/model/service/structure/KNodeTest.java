package pers.juumii.MindTrace.model.service.structure;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.ktree.KNode;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.util.List;

class KNodeTest {

    @Test
    void toJson() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        KNode root = kTree.getRoot();
        System.out.println(JsonUtils.toJson(root));
    }

    @Test
    void getXNodeBeneath() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        List<KNode> beneath = kTree.getRoot().queryKNodeBeneath();
    }
}