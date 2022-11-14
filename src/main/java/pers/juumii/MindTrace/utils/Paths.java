package pers.juumii.MindTrace.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Paths {
    @Getter
    private final String    resourceRoot,
                            dataRoot,
                            jsonDataRoot,
                            configRoot,
                            kTreeIndexRoot,
                            linkOpenerRoot,
                            tempMarkdownRoot;
    @Getter
    private final File  linkOpenerFile,
                        kTreeIndexFile,
                        tempMarkdownFile;

    @Autowired
    public Paths(){
        String general = "config.json";
        JsonObject generalConfigs = new Gson().fromJson(IOUtils.readFile(general), JsonObject.class);
        resourceRoot = generalConfigs.get("resourceRoot").getAsString();
        dataRoot = resourceRoot + "data/";
        configRoot = resourceRoot + "configs/";
        jsonDataRoot = dataRoot + "json/";
        kTreeIndexRoot = configRoot + "kTreeIndex.json";
        linkOpenerRoot = configRoot + "linkOpener.json";
        tempMarkdownRoot = configRoot + "tempMarkdown.md";
        linkOpenerFile = new File(linkOpenerRoot);
        kTreeIndexFile = new File(kTreeIndexRoot);
        tempMarkdownFile = new File(tempMarkdownRoot);
    }


}
