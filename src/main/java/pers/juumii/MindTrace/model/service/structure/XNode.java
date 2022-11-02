package pers.juumii.MindTrace.model.service.structure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class XNode<T> {

    private T data;
    private List<XNode<T>> subKNodes;

    public XNode(){
        subKNodes = new ArrayList<>();
    }
    public XNode(T data) {
        this.data = data;
        subKNodes = new ArrayList<>();
    }

    public XNode(T data, List<XNode<T>> subKNodes) {
        this.data = data;
        this.subKNodes = subKNodes;
    }

    public void addSubKNode(XNode<T> kNode){
        subKNodes.add(kNode);
    }
}
