package pers.juumii.MindTrace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.ktree.KTreeLoader;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.MathUtils;

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
