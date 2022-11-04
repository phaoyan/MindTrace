package pers.juumii.MindTrace.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class DataLinker {
    @Getter
    @Setter
    private Repository repository;

    public void link(){

    }
}
