package pers.juumii.MindTrace.model.service.ktree;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KTreeLoader {

    void load(KTree kTree);
    void synchronize(KTree kTree);

    List<String> getResourceNames();

    void create(String name);

    void use(String name, KTree kTree);

    void delete(String name);

    String getCurrent();

    void alterCurrentName(String newName);
}
