package pers.juumii.MindTrace.model.service.ktree;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.utils.IOUtils;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.Paths;
import pers.juumii.MindTrace.utils.SpringUtils;

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
        if(!resourceNames.isEmpty())
            selectedResourceName = resourceNames.get(0);
        dataDir = paths.getJsonDataRoot();
        backupDir = paths.getJsonBackupRoot();
    }

    @Override
    public void load(KTree kTree) {
        if(selectedResourceName == null)
            return;
        ObjectNode json = JsonUtils.readJson(IOUtils.readFile(new File(getPath(selectedResourceName))), ObjectNode.class);
        kTree.setRoot(JsonUtils.readJson(JsonUtils.toJson(json.get("root")), KNode.class));
        kTree.setConfigs(JsonUtils.readJson(JsonUtils.toJson(json.get("configs")), KTreeConfigs.class));
    }

    @Override
    public void synchronize(KTree kTree) {
        if(selectedResourceName == null)
            return;
        //备份
        IOUtils.copyFile(new File(getPath(selectedResourceName)), new File(backupDir + selectedResourceName + LocalDateTime.now().getNano() + ".json"));
        //更新
        IOUtils.writeFile(new File(getPath(selectedResourceName)), JsonUtils.toJsonShallowly(kTree));
        System.out.println("data synchronized: " + selectedResourceName);
    }

    public void create(String name){
        resourceNames.add(name);
        selectedResourceName = name;

        KTree kTree = new KTree();
        kTree.setRoot(KNode.getDefault());
        kTree.setConfigs(KTreeConfigs.getDefault());
        synchronize(kTree);
        use(name, SpringUtils.getBean(KTree.class));
    }

    private String getPath(String name) {
        return dataDir +name+".json";
    }

    public void delete(String name){
        if(resourceNames.size() == 1)
            return;
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
