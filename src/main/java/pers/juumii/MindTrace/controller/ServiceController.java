package pers.juumii.MindTrace.controller;


import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.service.general.LinkOpener;
import pers.juumii.MindTrace.model.service.general.MarkdownEditor;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.ktree.KTreeLoader;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.MathUtils;

import java.io.IOException;

@CrossOrigin
@RestController
public class ServiceController {

    private final KTree kTree;
    private final LinkOpener linkOpener;
    private final MarkdownEditor markdownEditor;
    private final KTreeLoader kTreeLoader;

    public ServiceController(KTree kTree, LinkOpener linkOpener, MarkdownEditor markdownEditor) {
        this.kTree = kTree;
        this.linkOpener = linkOpener;
        this.markdownEditor = markdownEditor;
        this.kTreeLoader = kTree.getLoader();
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

    @GetMapping("/utils/data/check/all")
    public String checkKTreeNames(){
        return JsonUtils.toJson(kTreeLoader.getResourceNames());
    }

    @GetMapping("/utils/data/check/current")
    public String checkCurrentName(){
        return JsonUtils.toJson(kTreeLoader.getCurrent());
    }

    @GetMapping("/utils/data/alterName")
    public void alterName(String newName){ kTreeLoader.alterCurrentName(newName);}

    @GetMapping("utils/data/create")
    public void createKTree(){
        String name = MathUtils.getRandomString(10);
        kTreeLoader.create(name);
        useKTree(name);
    }

    @PostMapping("utils/data/use")
    public void useKTree(@RequestBody String name){
        //在换库之前，先把已经产生的修改同步到持久层
        kTreeLoader.synchronize(kTree);
        kTreeLoader.use(name, kTree);
    }

    @PostMapping("utils/data/delete")
    public void deleteKTree(@RequestBody String name){
        kTreeLoader.delete(name);
    }

    @GetMapping("utils/data/save")
    public void save(){
        kTree.synchronize();
    }
}
