package pers.juumii.MindTrace.model.service.ktree;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.utils.IOUtils;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.Paths;
import pers.juumii.MindTrace.utils.SpringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class KTreeJsonLoader implements KTreeLoader{

    private final String indexSrc;
    private final String dataDir;
    @Getter
    private final List<String> resourceNames;
    private String selectedResourceName;

    @Autowired
    @SuppressWarnings("unchecked")
    public KTreeJsonLoader(Paths paths) {
        indexSrc = paths.getKTreeIndexRoot();
        resourceNames = JsonUtils.readJson(IOUtils.readFile(paths.getKTreeIndexFile()), ArrayList.class);
        if (resourceNames != null)
            selectedResourceName = resourceNames.get(0);
        dataDir = paths.getJsonDataRoot();
    }

    @Override
    public void load(KTree kTree) {
        KNode root = JsonUtils.readJson(IOUtils.readFile(new File(getPath(selectedResourceName))), KNode.class);
        kTree.setRoot(root);
    }

    @Override
    public void synchronize(KTree kTree) {
        IOUtils.writeFile(new File(indexSrc), JsonUtils.toJson(resourceNames));
        IOUtils.writeFile(new File(getPath(selectedResourceName)), JsonUtils.toJson(kTree.getRoot()));
        System.out.println("data synchronized: " + selectedResourceName);
    }

    //先在index中注册，然后再创建一个根节点初始化这个json
    public void create(String name){
        if(!resourceNames.contains(name))
            resourceNames.add(name);
        IOUtils.writeFile(new File(indexSrc), JsonUtils.toJson(resourceNames));

        KNode root = new KNode();
        Knowledge knowledge = new Knowledge();
        knowledge.setDescription("ROOT");
        root.setData(knowledge);
        IOUtils.writeFile(new File(getPath(name)), JsonUtils.toJson(root));
    }

    private String getPath(String name) {
        return dataDir +name+".json";
    }

    public void delete(String name){
        resourceNames.remove(name);
        IOUtils.writeFile(new File(indexSrc), JsonUtils.toJson(resourceNames));
        if(selectedResourceName.equals(name)){
            selectedResourceName = resourceNames.get(0);
            load(SpringUtils.getBean(KTree.class));
        }
        System.out.println("KTree deleted : "+name);
    }

    @Override
    public String getCurrent() {
        return selectedResourceName;
    }

    @Override
    public void alterCurrentName(String newName) {
        resourceNames.set(resourceNames.indexOf(selectedResourceName), newName);
        IOUtils.writeFile(new File(indexSrc), JsonUtils.toJson(resourceNames));
        IOUtils.renameFile(new File(getPath(selectedResourceName)), new File(getPath(newName)));
        selectedResourceName = newName;
    }

    public void use(String name, KTree kTree){
        if(resourceNames.contains(name))
            selectedResourceName = name;
        load(kTree);
    }
}
