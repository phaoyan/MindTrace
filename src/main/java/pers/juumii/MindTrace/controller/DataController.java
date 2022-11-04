package pers.juumii.MindTrace.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningCard;
import pers.juumii.MindTrace.model.service.LinkingSearcher;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.model.service.structure.KTree;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.util.List;


@CrossOrigin
@RestController
public class DataController {

    private final Repository repository;
    private final LinkingSearcher searcher;
    private final KTree kTree;

    public DataController(Repository repository, LinkingSearcher searcher, KTree kTree) {
        this.repository = repository;
        this.searcher = searcher;
        this.kTree = kTree;
    }

    @GetMapping("/knowledge/getRoot")
    public String getKnowledgeRoot(){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.getKnowledgeRoot", kTree.getRoot().getData().getDescription());
        return JsonUtils.toJson(kTree.getRoot());
    }

    @GetMapping("/knowledge/getKnowledgeById")
    public String getKnowledgeById(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.getKnowledgeById", "id="+id);
        Knowledge knowledge = id == 0 ? kTree.getRoot().getData() : repository.getById(id, Knowledge.class);
        return JsonUtils.toJson(knowledge);
    }


    @GetMapping("/knowledge/addKnowledgeBySuperId")
    public String addKnowledgeBySuperId(int superId){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.addKnowledgeBySuperId", "superId=" + superId);
        Knowledge knowledge = repository.create(Knowledge.class);
        knowledge.setId(repository.getByType(Knowledge.class).size()+1);
        knowledge.setSuperKnowledgeId(superId);
        knowledge.setDescription("empty...");
        kTree.refresh();
        return JsonUtils.toJson(kTree);
    }

    @PostMapping("/knowledge/update")
    public String updateKnowledge(@RequestBody String knowledge){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.updateKnowledge", knowledge);
        JsonObject json = new Gson().fromJson(knowledge, JsonObject.class);
        int id = json.get("id").getAsInt();
        String desc = json.get("description").getAsString();
        int superKnowledgeId = json.get("superKnowledgeId").getAsInt();

        Knowledge selected = repository.getById(id, Knowledge.class);
        selected.setDescription(desc);
        selected.setSuperKnowledgeId(superKnowledgeId);
        kTree.refresh();
        return JsonUtils.toJson(selected);
    }

    @GetMapping("knowledge/remove")
    public String removeKnowledge(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.removeKnowledge", "id="+id);
        searcher.getKnowledgesBeneath(repository.getById(id, Knowledge.class)).forEach(repository::remove);
        kTree.refresh();
        return "remove success from backend";
    }

    //返回一个knowledge下所有learning cards（不包括subKnowledge中的），具体包括其desc和content，其中content包括desc和src
    @GetMapping("knowledge/getLearningCards")
    public String getLearningCards(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.getLearningCards", "id="+ id);
        List<LearningCard> learningCards = searcher.getLearningCards(id);
        return JsonUtils.toJson(learningCards);
    }

    @GetMapping("knowledge/addLearningCard")
    public String addLearningCard(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.addLearningCard", "id="+id);
        LearningCard card = repository.create(LearningCard.class);
        card.setId(repository.getByType(LearningCard.class).size());
        card.setKnowledgeId(id);
        card.setDescription("empty...");
        return JsonUtils.toJson(card);
    }

    @GetMapping("knowledge/updateLearningCard")
    public String updateLearningCard(String json){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.updateLearningCard", "json:" + json);
        JsonObject cardJson = new Gson().fromJson(json, JsonObject.class);
        int id = cardJson.get("id").getAsInt();
        int knowledgeId = cardJson.get("knowledgeId").getAsInt();
        String desc = cardJson.get("description").getAsString();
        String resource = cardJson.get("resource").getAsString();
        LearningCard card = repository.getById(id, LearningCard.class);
        card.setDescription(desc);
        card.setResource(resource);
        card.setKnowledgeId(knowledgeId);

        return JsonUtils.toJson(card);
    }

    @GetMapping("/knowledge/removeLearningCard")
    public String removeLearningCard(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.removeLearningCard", "id="+id);
        repository.remove(repository.getById(id, LearningCard.class));
        return "Learning card removed.";
    }
}
