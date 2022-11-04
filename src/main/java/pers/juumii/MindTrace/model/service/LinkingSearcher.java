package pers.juumii.MindTrace.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
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

    public List<Knowledge> getSubKnowledges(Knowledge knowledge){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(Knowledge.class), cur -> cur.getSuperKnowledgeId() == knowledge.getId()));
    }

    public List<Knowledge> getKnowledgesAside(Knowledge knowledge){
        return getSubKnowledges(repository.getById(knowledge.getSuperKnowledgeId(),Knowledge.class));
    }

    //返回由该knowledge及其所有祖先组成的列表
    public List<Knowledge> getKnowledgesBeyond(Knowledge knowledge){
        List<Knowledge> res = new ArrayList<>();
        if(knowledge != null){
            res.add(knowledge);
            res.addAll(getKnowledgesBeyond(repository.getById(knowledge.getSuperKnowledgeId(), Knowledge.class)));
        }
        return res;
    }

    //返回由该knowledge及其所有后代组成的列表
    public List<Knowledge> getKnowledgesBeneath(Knowledge knowledge){
        List<Knowledge> res = new ArrayList<>(List.of(knowledge));
        for (Knowledge sub: getSubKnowledges(knowledge))
            res.addAll(getKnowledgesBeneath(sub));
        return res;
    }

    public List<LearningCard> getLearningCards(Knowledge knowledge){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(LearningCard.class), data->data.getKnowledgeId() == knowledge.getId()));
    }

    public List<LearningCard> getLearningCards(int knowledgeId){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(LearningCard.class), data->data.getKnowledgeId() == knowledgeId));
    }

    public List<LearningRecord> getLearningRecord(LearningCard card){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(LearningRecord.class), record -> record.getCardId() == card.getId()));
    }

    public List<LearningRecord> getLearningRecord(Knowledge knowledge){
        List<LearningRecord> records = new ArrayList<>();
        for(LearningCard task: getLearningCards(knowledge))
            records.addAll(getLearningRecord(task));
        return records;
    }

    public List<QuizCard> getQuizCards(Knowledge knowledge){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(QuizCard.class), card -> card.getKnowledgeId() == knowledge.getId()));
    }

    //返回属于同一个knowledge的quizTasks
    public List<QuizCard> getQuizCardsAside(QuizCard quizCard){
        return getQuizCards(repository.getById(quizCard.getKnowledgeId(), Knowledge.class));
    }

    //返回属于同一个knowledge及其subKnoledges的quizTasks
    public List<QuizCard> getQuizCardsBeneath(QuizCard quizCard){
        List<QuizCard> res = new ArrayList<>();
        for(Knowledge knowledge: getKnowledgesBeneath(repository.getById(quizCard.getKnowledgeId(), Knowledge.class)))
            res.addAll(getQuizCards(knowledge));
        return res;
    }

    public List<QuizRecord> getQuizRecord(QuizCard card){
        return new ArrayList<>(DataUtils.getAllIf(repository.getByType(QuizRecord.class), record -> record.getCardId() == card.getId()));
    }

    public List<QuizRecord> getQuizRecord(Knowledge knowledge){
        List<QuizRecord> records = new ArrayList<>();
        for(QuizCard task: getQuizCards(knowledge))
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
}
