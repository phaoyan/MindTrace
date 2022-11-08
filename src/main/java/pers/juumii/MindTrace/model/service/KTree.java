package pers.juumii.MindTrace.model.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class KTree {

    private final Repository repository;
    @Getter
    private KNode root;

    public KTree(Repository repository) {
        this.repository = repository;
        load();
    }

    public void load() {
        root = new KNode();
        Knowledge knowledge = new Knowledge();
        knowledge.setId(0);
        knowledge.setSuperKnowledgeId(-1);
        knowledge.setMasteryMin(0);
        knowledge.setMasteryMax(0);
        knowledge.setDescription("ROOT");
        root.setData(knowledge);
        List<Knowledge> knowledges = repository.getByType(Knowledge.class);
        List<KNode> kNodes = new ArrayList<>();
        knowledges.forEach(klg -> kNodes.add(new KNode(klg)));
        kNodes.forEach(xNode -> link(xNode, kNodes));
    }

    private void link(KNode kNode, List<KNode> kNodes) {
        KNode target = DataUtils.getIf(kNodes, superNode -> superNode.getData().getId() == kNode.getData().getSuperKnowledgeId());
        if(target == null)
            root.addSubKNode(kNode);
        else
            target.addSubKNode(kNode);
    }

    //将KTree中储存的数据同步到repository
    public void synchronize(){
        repository.clear();
        root.queryKNodeBeneath().forEach(kNode-> repository.put(kNode.getData()));
    }

    public KNode getKNode(int id){
        return id == 0 ? root: DataUtils.getIf(root.queryKNodeBeneath(), kNode->kNode.getData().getId() == id);
    }

    public int size() {
        return root.size();
    }

    public int quizCardSize(){
        int res = 0;
        for(KNode kNode: root.queryKNodeBeneath())
            res += kNode.getData().getQuizCards().size();
        return res;
    }

    public int learningCardSize(){
        int res = 0;
        for(KNode kNode: root.queryKNodeBeneath())
            res += kNode.getData().getLearningCards().size();
        return res;
    }

    public void setRoot(KNode root) {
        this.root = root;
    }
}
