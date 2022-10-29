package pers.juumii.MindTrace.model.service;


import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.data.Level;
import pers.juumii.MindTrace.model.data.Persistent;
import pers.juumii.MindTrace.model.data.Settings;
import pers.juumii.MindTrace.utils.Constants;
import pers.juumii.MindTrace.utils.DataUtils;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class GeneralStatistics {
    public static <T extends Persistent> int getItemCount(Class<T> cl){
        return SpringUtils.getBean(Repository.class).getByType(cl).size();
    }
    public static List<Knowledge> getKnowledgesByMastery(Level level){
        List<Knowledge> knowledgeRepo = SpringUtils.getBean(Repository.class).getByType(Knowledge.class);
        return DataUtils.getAllIf(knowledgeRepo, knowledge -> Level.getByPercents(knowledge.getMasteryAvg()) == level);
    }
    public static List<Knowledge> getKnowledgesBelowLevelLine(){
        Repository repository = SpringUtils.getBean(Repository.class);
        List<Knowledge> knowledgeRepo = repository.getByType(Knowledge.class);
        Level levelLine = Level.getByString(DataUtils.getIf(repository.getByType(Settings.class), setting -> setting.getK().equals(Constants.KNOWLEDGE_LEVEL_LINE)).getV());
        return DataUtils.getAllIf(knowledgeRepo, knowledge ->  Level.getByPercents(knowledge.getMasteryAvg()).compareTo(levelLine) <= 0);
    }

    public static LocalDateTime getAvgLastRecordTime() {
        //todo
        return null;
    }

    public static Duration getTimeScale() {
        //todo
        return null;
    }
}
