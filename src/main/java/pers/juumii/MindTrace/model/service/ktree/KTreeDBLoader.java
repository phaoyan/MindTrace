package pers.juumii.MindTrace.model.service.ktree;


import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class KTreeDBLoader implements KTreeLoader{

    private final Repository repository;

    public KTreeDBLoader(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void load(KTree kTree) {
        KNode root = new KNode();
        Knowledge knowledge = new Knowledge();
        knowledge.setId(0);
        knowledge.setSuperKnowledgeId(-1);
        knowledge.setMasteryMin(0);
        knowledge.setMasteryMax(0);
        knowledge.setDescription("ROOT");
        root.setData(knowledge);
        List<Knowledge> knowledges = repository.getByType(Knowledge.class);
        List<KNode> kNodes = new ArrayList<>();
        kNodes.add(root);
        knowledges.forEach(klg -> kNodes.add(new KNode(klg)));
        kNodes.forEach(kNode -> {
            KNode target = DataUtils.getIf(kNodes, superNode -> superNode.getData().getId() == kNode.getData().getSuperKnowledgeId());
            if(target != null)
                target.addSubKNode(kNode);
        });
        kTree.setRoot(root);
    }

    @Override
    public void synchronize(KTree kTree) {
        repository.clear();
        kTree.getRoot().queryKNodeBeneath().forEach(kNode-> repository.put(kNode.getData()));
        repository.commit();
    }

    @Override
    public List<String> getResourceNames() {
        return null;
    }

    @Override
    public void create(String name) {

    }

    @Override
    public void use(String name, KTree kTree) {

    }

    @Override
    public void delete(String name) {

    }

    @Override
    public String getCurrent() {
        return null;
    }

    @Override
    public void alterCurrentName(String newName) {

    }
}
