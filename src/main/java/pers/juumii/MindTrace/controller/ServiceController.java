package pers.juumii.MindTrace.controller;


import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.service.general.LinkOpener;
import pers.juumii.MindTrace.model.service.general.MarkdownEditor;

import java.io.IOException;

@CrossOrigin
@RestController
public class ServiceController {

    private final LinkOpener linkOpener;
    private final MarkdownEditor markdownEditor;

    public ServiceController(LinkOpener linkOpener, MarkdownEditor markdownEditor) {
        this.linkOpener = linkOpener;
        this.markdownEditor = markdownEditor;
    }

    @PostMapping("/utils/openLink")
    public void openLink(@RequestBody String rawUrl) throws IOException {
        linkOpener.openLink(rawUrl);
    }

    @PostMapping("/utils/markdown/open")
    public void openMarkdown(@RequestBody String text){
        markdownEditor.open(text);
    }

    @GetMapping("/utils/markdown/close")
    public String closeMarkdown(){
        return markdownEditor.close();
    }
}
