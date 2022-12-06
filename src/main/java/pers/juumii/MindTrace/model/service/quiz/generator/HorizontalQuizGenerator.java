package pers.juumii.MindTrace.model.service.quiz.generator;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 实现覆盖的知识点尽可能多地生成Quiz
 */
@Service
public class HorizontalQuizGenerator extends QuizGenerator{

    @Override
    public List<QuizCard> quizzes(int scale, KTree kTree) {
        return QuizGenerator.quizzes(scale, kTree, (repo)->{
            List<QuizCard> repository = DataUtils.randomStackOf(repo, LocalDateTime.now().getDayOfYear());
            List<List<QuizCard>> groups = new ArrayList<>();
            //往groups里面加一个初始的group避免下面的循环直接终止
            groups.add(new ArrayList<>());
            for (QuizCard cur : repository) {
                for (int i = 0; i < groups.size(); i ++){
                    List<QuizCard> group = groups.get(i);
                    //如果这一组没找到和cur的knowledgeId相同的，那么说明这一组里cur是属于新的knowledge的，可以直接将其插入
                    if(DataUtils.getIf(group, card->card.getKnowledgeId() == cur.getKnowledgeId()) == null){
                        group.add(cur);
                        break;
                    }
                    //否则要向后继续寻找。如果已经是最后一组了，那么就要再新建一个组,并把cur加入新建的组
                    else if(DataUtils.getLast(groups) == group){
                        groups.add(new ArrayList<>(List.of(cur)));
                        break;
                    }
                }
            }

            return DataUtils.stackOf(DataUtils.reverse(DataUtils.join(groups)));
        });
    }

    public static ObjectNode defaultConfigs(){
        return new ObjectNode(JsonNodeFactory.instance);
    }

}
