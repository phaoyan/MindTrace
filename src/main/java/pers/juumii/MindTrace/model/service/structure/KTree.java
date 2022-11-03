package pers.juumii.MindTrace.model.service.structure;

import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class KTree {


    private final Repository repository;
    private XNode<Knowledge> root;


    public KTree(Repository repository) {
        this.repository = repository;
        refresh();
    }

    public void refresh() {
        root = new XNode<>();
        root.setData(new Knowledge(0, -1, "ROOT", 0,0));
        List<Knowledge> knowledges = repository.getByType(Knowledge.class);
        List<XNode<Knowledge>> kNodes = new ArrayList<>();
        knowledges.forEach(knowledge -> kNodes.add(new XNode<>(knowledge)));
        kNodes.forEach(xNode -> link(xNode, kNodes));
    }

    private void link(XNode<Knowledge> kNode, List<XNode<Knowledge>> kNodes) {
        XNode<Knowledge> target = DataUtils.getIf(kNodes, superNode -> superNode.getData().getId() == kNode.getData().getSuperKnowledgeId());
        if(target == null)
            root.addSubKNode(kNode);
        else
            target.addSubKNode(kNode);
    }

    public XNode<Knowledge> getRoot(){
        assert root != null;
        return root;
    }

    public XNode<String> getDescTreeRoot(){
        assert root != null;
        return getDescTreeRoot(root);
    }

    private XNode<String> getDescTreeRoot(XNode<Knowledge> root){
        XNode<String> res = new XNode<>();
        if(root.getData() != null)
            res.setData(root.getData().getDesc());
        for(XNode<Knowledge> kNode: root.getSubKNodes()){
            XNode<String> subNode = new XNode<>(kNode.getData().getDesc(), getDescTreeRoot(kNode).getSubKNodes());
            res.addSubKNode(subNode);
        }
        return res;
    }

    public XNode<Integer> getIdTreeRoot(){
        assert root != null;
        return getIdTreeRoot(root);
    }

    private XNode<Integer> getIdTreeRoot(XNode<Knowledge> root){
        XNode<Integer> res = new XNode<>();
        if(root.getData() != null)
            res.setData(root.getData().getId());
        for(XNode<Knowledge> kNode: root.getSubKNodes()){
            XNode<Integer> subNode = new XNode<>(kNode.getData().getId(), getIdTreeRoot(kNode).getSubKNodes());
            res.addSubKNode(subNode);
        }
        return res;
    }



}
