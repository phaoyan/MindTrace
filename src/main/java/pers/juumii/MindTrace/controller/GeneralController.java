package pers.juumii.MindTrace.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.SpringUtils;

@CrossOrigin
@RestController
public class GeneralController {

    @GetMapping("/exit")
    public void exit(){
        System.out.println("exit ... ");
        SpringUtils.getBean(KTree.class).synchronize();
        System.exit(0);
    }
}
