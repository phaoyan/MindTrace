package pers.juumii.MindTrace.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.exception.DataClearedException;
import pers.juumii.MindTrace.model.data.*;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinkingSearcher {
    @Getter
    @Setter
    private Repository repository;

    public LinkingSearcher(Repository repository) {
        this.repository = repository;
    }

    public List<Knowledge> getKnowledgeChain(Knowledge knowledge){
        List<Knowledge> res = new ArrayList<>();
        if(knowledge.isClear())
            return res;
        if(knowledge.getSuperKnowledgeId() != 0) {
            try {
                res.addAll(getKnowledgeChain(repository.getById(knowledge.getSuperKnowledgeId(), Knowledge.class)));
            } catch (DataClearedException ignored) {
                //之前已经进行过判断
            }
        }
        res.add(knowledge);
        return res;
    }

    public List<Knowledge> getSubKnowledges(Knowledge knowledge){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(Knowledge.class), cur -> cur.getSuperKnowledgeId() == knowledge.getId()));
    }

    public List<Knowledge> getKnowledgesAside(Knowledge knowledge){
        if(knowledge.getSuperKnowledgeId() == 0)
            return List.of(knowledge);
        Knowledge superKnowledge = null;
        try {
            superKnowledge = repository.getById(knowledge.getSuperKnowledgeId(), Knowledge.class);
        } catch (DataClearedException e) {
            //如果该knowledge已经被clear，则直接返回空的数组即可
            return new ArrayList<>();
        }
        return getSubKnowledges(superKnowledge);
    }

    public List<Knowledge> getAllKnowledgesBeyond(Knowledge knowledge){
        List<Knowledge> res = new ArrayList<>();
        List<Knowledge> knowledgeChain = getKnowledgeChain(knowledge);
        knowledgeChain.removeIf(cur -> cur.getId() == knowledge.getId());
        res.add(DataUtils.getIf(knowledgeChain, knowledge1 -> knowledge1.getSuperKnowledgeId() == 0));
        for(Knowledge chained: knowledgeChain)
            res.addAll(getSubKnowledges(chained));
        return res;
    }

    public List<Knowledge> getAllKnowledgesBeneath(Knowledge knowledge){
        List<Knowledge> res = new ArrayList<>();
        if(getSubKnowledges(knowledge).size() != 0)
            getSubKnowledges(knowledge).forEach(subKnowledge -> res.addAll(getAllKnowledgesBeneath(subKnowledge)));
        res.add(knowledge);
        return res;
    }

    public List<LearningTask> getLearningTasks(Knowledge knowledge){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(LearningTask.class), task -> task.getKnowledgeId() == knowledge.getId()));
    }

    public List<LearningTask> getLearningTasks(int knowledgeId){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(LearningTask.class), task -> task.getKnowledgeId() ==knowledgeId));
    }

    public List<Content> getLearningContents(LearningTask learningTask){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(Content.class), content -> content.getTaskId() == learningTask.getId()));
    }

    public List<LearningTask> getLearningTaskChain(LearningTask task){
        List<LearningTask> res = new ArrayList<>();
        if(task.getSuperTaskId() != 0) {
            try {
                res.addAll(getLearningTaskChain(repository.getById(task.getSuperTaskId(), LearningTask.class)));
            } catch (DataClearedException e) {
                //如果该learningTask已经被clear，则直接返回空数组即可
                return res;
            }
        }
        res.add(task);
        return res;
    }

    public List<LearningRecord> getLearningRecord(LearningTask task){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(LearningRecord.class), record -> record.getTaskId() == task.getId()));
    }

    public List<LearningRecord> getLearningRecord(Knowledge knowledge){
        List<LearningRecord> records = new ArrayList<>();
        for(LearningTask task: getLearningTasks(knowledge))
            records.addAll(getLearningRecord(task));
        return records;
    }

    public List<QuizTask> getQuizTasks(Knowledge knowledge){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(QuizTask.class), task -> task.getKnowledgeId() == knowledge.getId()));
    }

    public List<QuizCard> getQuizCards(QuizTask quizTask){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(QuizCard.class), quizCard -> quizCard.getQuizId() == quizTask.getId()));
    }

    //返回属于同一个knowledge的quizTasks
    public List<QuizTask> getQuizTasksAside(QuizTask quizTask){
        try {
            return getQuizTasks(repository.getById(quizTask.getKnowledgeId(), Knowledge.class));
        } catch (DataClearedException e) {
            //如果该quizTask已经被clear，则它的aside就是空的数组，直接返回空的数组即可
            return new ArrayList<>();
        }
    }

    //返回属于同级knowledge的quizTasks
    public List<QuizTask> getAllQuizTasksAside(QuizTask quizTask){
        List<QuizTask> res = new ArrayList<>();
        try {
            getKnowledgesAside(repository.getById(quizTask.getKnowledgeId(),Knowledge.class)).forEach(knowledge -> res.addAll(getQuizTasks(knowledge)));
        } catch (DataClearedException e) {
            //如果该quizTask已经被clear，则它的aside就是空的数组，直接返回空的数组即可
            return new ArrayList<>();
        }
        return res;
    }

    //返回属于同一个knowledge及其subKnoledges的quizTasks
    public List<QuizTask> getQuizTasksBeneath(QuizTask quizTask){
        List<QuizTask> res = new ArrayList<>();
        try {
            getAllKnowledgesBeneath(repository.getById(quizTask.getKnowledgeId(), Knowledge.class)).forEach(knowledge -> res.addAll(getQuizTasks(knowledge)));
        } catch (DataClearedException e) {
            //如果该quizTask已经被clear，则它的beneath就是空的数组，直接返回空的数组即可
            return new ArrayList<>();
        }
        return res;
    }

    public List<QuizRecord> getQuizRecord(QuizTask task){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(QuizRecord.class), record -> record.getTaskId() == task.getId()));
    }

    public List<QuizRecord> getQuizRecord(Knowledge knowledge){
        List<QuizRecord> records = new ArrayList<>();
        for(QuizTask task: getQuizTasks(knowledge))
            records.addAll(getQuizRecord(task));
        return records;
    }



    //添加一个和reference同属于一个superKnowledge的knowledge，除了superKnowledgeId外不对其赋初值，而将其返回让调用方完成。
    public Knowledge addKnowledgeAside(Knowledge reference){
        Knowledge added = repository.create(Knowledge.class);
        added.setSuperKnowledgeId(reference.getSuperKnowledgeId());
        return added;
    }

    //添加一个reference的subKnowledge，除了superKnowledgeId外不对其赋初值，而将其返回让调用方完成。
    public Knowledge addKnowledgeBeneath(Knowledge reference){
        Knowledge added = repository.create(Knowledge.class);
        added.setSuperKnowledgeId(reference.getId());
        return added;
    }

    //添加一个和reference同属一个knowledge的learningTask，通过这种方式添加的learningTask和reference的content默认相同
    public LearningTask addLearningTaskAside(LearningTask reference){
        LearningTask added = repository.create(LearningTask.class);
        added.setSuperTaskId(reference.getSuperTaskId());

        return added;
    }
}
