package pers.juumii.MindTrace.model.service.general;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pers.juumii.MindTrace.SpringConfig;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownEditorTest {

    @Test
    void open() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        MarkdownEditor editor = ctx.getBean(MarkdownEditor.class);
        editor.open("$EXTERNAL$hello markdown!");
    }

    @Test
    void close() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        MarkdownEditor editor = ctx.getBean(MarkdownEditor.class);
        System.out.println(editor.close());
    }
}