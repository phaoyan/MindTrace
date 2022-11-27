package pers.juumii.MindTrace.model.service.ktree;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.model.data.Knowledge;
import pers.juumii.MindTrace.utils.IOUtils;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.Paths;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class KTreeJsonLoader implements KTreeLoader{

    private final String dataDir;
    private final String backupDir;
    @Getter
    private final List<String> resourceNames = new ArrayList<>();
    private String selectedResourceName;

    @Autowired
    public KTreeJsonLoader(Paths paths) {
        Collection<File> sourceDataFiles = FileUtils.listFiles(new File(paths.getJsonDataRoot()), null, false);
        sourceDataFiles.forEach(file -> resourceNames.add(file.getName().replace(".json","")));
        selectedResourceName = resourceNames.get(0);
        dataDir = paths.getJsonDataRoot();
        backupDir = paths.getJsonBackupRoot();
    }

    @Override
    public void load(KTree kTree) {
        if(selectedResourceName == null)
            return;
        KNode root = JsonUtils.readJson(IOUtils.readFile(new File(getPath(selectedResourceName))), KNode.class);
        kTree.setRoot(root);
    }

    @Override
    public void synchronize(KTree kTree) {
        if(selectedResourceName == null)
            return;
        IOUtils.copyFile(new File(getPath(selectedResourceName)), new File(backupDir + selectedResourceName + LocalDateTime.now().getNano() + ".json"));
        IOUtils.writeFile(new File(getPath(selectedResourceName)), JsonUtils.toJson(kTree.getRoot()));
        System.out.println("data synchronized: " + selectedResourceName);
    }

    //先在index中注册，然后再创建一个根节点初始化这个json
    public void create(String name){
        if(!resourceNames.contains(name))
            resourceNames.add(name);

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
        IOUtils.deleteFile(new File(getPath(name)));
        selectedResourceName = null;
        System.out.println("KTree deleted : "+name);
    }

    @Override
    public String getCurrent() {
        return selectedResourceName;
    }

    @Override
    public void alterCurrentName(String newName) {
        resourceNames.set(resourceNames.indexOf(selectedResourceName), newName);
        IOUtils.renameFile(new File(getPath(selectedResourceName)), new File(getPath(newName)));
        selectedResourceName = newName;
    }

    public void use(String name, KTree kTree){
        if(resourceNames.contains(name))
            selectedResourceName = name;
        load(kTree);
    }
}
