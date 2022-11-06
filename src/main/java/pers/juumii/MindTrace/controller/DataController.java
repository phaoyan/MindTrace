package pers.juumii.MindTrace.controller;


import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.KTree;
import pers.juumii.MindTrace.model.service.KNode;
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
    public void synchronizeKNode(@RequestBody String kNode){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.updateKNode", kNode);
        KNode xNode = JsonUtils.readJson(kNode, KNode.class);
        kTree.getKNode(xNode.getData().getId()).setData(xNode.getData());
        kTree.getKNode(xNode.getData().getId()).setSubKNodes(xNode.getSubKNodes());
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
