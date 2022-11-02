package pers.juumii.MindTrace.model.service.structure;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;
import pers.juumii.MindTrace.model.data.Knowledge;

class KTreeTest {

    @Test
    void getRoot() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        XNode<Knowledge> root = kTree.getRoot();
        System.out.println(root);
    }

    @Test
    void getDescTreeRoot() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        XNode<String> root = kTree.getDescTreeRoot();
        System.out.println(root);
    }

    @Test
    void getIdTreeRoot() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        KTree kTree = ctx.getBean(KTree.class);
        XNode<Integer> root = kTree.getIdTreeRoot();
        System.out.println(root);
    }
}