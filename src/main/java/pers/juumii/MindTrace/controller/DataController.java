package pers.juumii.MindTrace.controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.juumii.MindTrace.exception.DataClearedException;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.LearningTask;
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
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.getKnowledgeRoot", kTree.getRoot().toString());
        return JsonUtils.toJson(kTree.getRoot());
    }

    @GetMapping("/knowledge/getKnowledgeById")
    public String getKnowledgeById(int id) throws DataClearedException {
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
        knowledge.setDesc("empty...");
        kTree.refresh();
        return JsonUtils.toJson(kTree);
    }

    @GetMapping("/knowledge/update")
    public String updateKnowledge(@RequestParam String knowledge) throws DataClearedException {
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.updateKnowledge", knowledge);
        JsonObject json = new Gson().fromJson(knowledge, JsonObject.class);
        int id = json.get("id").getAsInt();
        String desc = json.get("desc").getAsString();
        int superKnowledgeId = json.get("superKnowledgeId").getAsInt();

        Knowledge selected = repository.getById(id, Knowledge.class);
        selected.setDesc(desc);
        selected.setSuperKnowledgeId(superKnowledgeId);
        kTree.refresh();
        return JsonUtils.toJson(selected);
    }

    @GetMapping("knowledge/remove")
    public String removeKnowledge(int id) throws DataClearedException {
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.removeKnowledge", "id="+id);
        searcher.getAllKnowledgesBeneath(repository.getById(id, Knowledge.class)).forEach(repository::remove);
        kTree.refresh();
        return "remove success from backend";
    }

    //返回一个knowledge下所有learning tasks（不包括subKnowledge中的），具体包括其desc和content，其中content包括desc和src
    @GetMapping("knowledge/getLearningTasks")
    public String getLearningTasks(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.getLearningTasks", "id="+ id);
        List<LearningTask> learningTasks = searcher.getLearningTasks(id);
        JsonArray res = new JsonArray();
        for(LearningTask task: learningTasks){
            JsonObject taskJson = new JsonObject();
            taskJson.addProperty("desc", task.getDesc());
            taskJson.addProperty("id",task.getId());
            taskJson.addProperty("knowledgeId",task.getKnowledgeId());
            taskJson.addProperty("superTaskId",task.getSuperTaskId());

            res.add(taskJson);
        }
        ConsoleUtils.printLocation(getClass(), new Gson().toJson(res));
        return res.toString();
    }

    @GetMapping("knowledge/addLearningTask")
    public String addLearningTask(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.addLearningTask", "id="+id);
        LearningTask task = repository.create(LearningTask.class);
        task.setId(repository.getByType(LearningTask.class).size());
        task.setKnowledgeId(id);
        task.setDesc("empty...");
        return JsonUtils.toJson(task);
    }

    @GetMapping("knowledge/updateLearningTask")
    public String updateLearningTask(String json) throws DataClearedException {
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.updateLearningTask", "json:" + json);
        JsonObject taskJson = new Gson().fromJson(json, JsonObject.class);
        int id = taskJson.get("id").getAsInt();
        String desc = taskJson.get("desc").getAsString();
        int superTaskId = taskJson.get("superTaskId").getAsInt();
        int knowledgeId = taskJson.get("knowledgeId").getAsInt();
        LearningTask task = repository.getById(id, LearningTask.class);
        task.setDesc(desc);
        task.setSuperTaskId(superTaskId);
        task.setKnowledgeId(knowledgeId);

        return JsonUtils.toJson(task);
    }

    @GetMapping("/knowledge/removeLearningTask")
    public String removeLearningTask(int id){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.DataController.removeLearningTask", "id="+id);
        try {
            repository.remove(repository.getById(id,LearningTask.class));
            return "Learning task removed.";
        } catch (DataClearedException e) {
            return "Learning task with id=" + id + " is not found.";
        }
    }
}
