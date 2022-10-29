package pers.juumii.MindTrace.model.service.structure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.service.Repository;

@Service
public class XTree {

    private static class XNode{

    }

    private final Repository repository;
    private XNode root;


    public XTree(Repository repository) {
        this.repository = repository;
        init();
    }

    private void init() {

    }

    public String toJson(){
        return null;
    }
}
