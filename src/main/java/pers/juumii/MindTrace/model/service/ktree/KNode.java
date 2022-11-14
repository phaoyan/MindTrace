package pers.juumii.MindTrace.model.service.ktree;

import lombok.Data;
import lombok.ToString;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class KNode {

    private Knowledge data;
    private List<KNode> subKNodes;

    public KNode(){
        subKNodes = new ArrayList<>();
    }
    public KNode(Knowledge data) {
        this.data = data;
        subKNodes = new ArrayList<>();
    }

    public void addSubKNode(KNode kNode){
        subKNodes.add(kNode);
    }

    private List<KNode> queryKNodeBeneath(KNode root){
        List<KNode> res = new ArrayList<>(root.getSubKNodes());
        root.getSubKNodes().forEach(subXNode -> res.addAll(queryKNodeBeneath(subXNode)));
        return res;
    }

    public List<KNode> queryKNodeBeneath(){
        return queryKNodeBeneath(this);
    }

    public KNode querySubKNode(int id){
        return DataUtils.getIf(queryKNodeBeneath(), kNode -> kNode.getData().getId() == id);
    }

    public boolean containsKNode(int id){
        return querySubKNode(id) != null;
    }

    public static KNode prototype() {
        KNode res = new KNode();
        res.setData(Knowledge.protoType());
        res.setSubKNodes(new ArrayList<>());
        return res;
    }

    public int size() {
        int res = 1;
        for(KNode kNode: subKNodes)
            res += kNode.size();
        return res;
    }
}
