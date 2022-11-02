package pers.juumii.MindTrace.model.service.structure;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.utils.JsonUtils;

import static org.junit.jupiter.api.Assertions.*;

class XNodeTest {

    @Test
    void toJson() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        XNode<Integer> root = kTree.getIdTreeRoot();
        System.out.println(JsonUtils.toJson(root));
    }
}