package pers.juumii.MindTrace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.ktree.KTreeConfigs;
import pers.juumii.MindTrace.model.service.ktree.KTreeLoader;
import pers.juumii.MindTrace.model.service.quiz.generator.QuizGenerator;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.text.ParseException;
import java.time.LocalDateTime;

@CrossOrigin
@RestController
public class KTreeController {
    private final KTree kTree;
    private final KTreeLoader kTreeLoader;

    @Autowired
    public KTreeController(KTree kTree){
        this.kTree = kTree;
        this.kTreeLoader = kTree.getLoader();
    }

    @GetMapping("/utils/data/config/load")
    public String queryKTreeConfigs(){
        //kTree的结构，除了root字段以外都是configs
        return JsonUtils.toJson(kTree.getConfigs());
    }

    @GetMapping("/utils/data/config/quizGenerator/load")
    public String queryQuizGeneratorConfigs(String type){
        return JsonUtils.toJson(QuizGenerator.defaultConfigs(type));
    }

    @PostMapping("/utils/data/config/synchronize")
    public void synchronizeKTreeConfigs(@RequestBody String jsonString) throws ParseException {
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.KTreeController.synchronizeKTreeConfigs", jsonString);
        kTree.setConfigs(JsonUtils.readJson(jsonString, KTreeConfigs.class));
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
        String name = LocalDateTime.now().toString().replace(".","-").replace(":","-");
        kTreeLoader.create(name);
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
