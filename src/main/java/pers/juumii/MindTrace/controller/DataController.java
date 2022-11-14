package pers.juumii.MindTrace.controller;


import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.ktree.KNode;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.JsonUtils;

@CrossOrigin
@RestController
public class DataController {

    private final KTree kTree;

    public DataController(KTree kTree) {
        this.kTree = kTree;
    }

    @GetMapping("/KNode/load")
    public String loadKNode(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.getKNode", "id="+id);
        return JsonUtils.toJson(kTree.getKNode(id));
    }

    @SuppressWarnings("all")
    @PostMapping("/KNode/synchronize")
    public void synchronizeKNode(@RequestBody String kNodeJson){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.updateKNode", kNodeJson);
        KNode kNode = JsonUtils.readJson(kNodeJson, KNode.class);
        kTree.getKNode(kNode.getData().getId()).setData(kNode.getData());
        kTree.getKNode(kNode.getData().getId()).setSubKNodes(kNode.getSubKNodes());
    }

    @GetMapping("/KNode/protoType")
    public String getProtoType(String type){
        return switch (type){
            case "kNode" -> JsonUtils.toJson(KNode.prototype());
            case "knowledge" -> JsonUtils.toJson(Knowledge.protoType());
            case "learningCard" -> JsonUtils.toJson(LearningCard.protoType());
            case "quizCard" -> JsonUtils.toJson(QuizCard.protoType());
            default -> "";
        };
    }
}
