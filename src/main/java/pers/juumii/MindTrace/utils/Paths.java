package pers.juumii.MindTrace.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class Paths {
    @Getter
    private final String    resourceRoot,
                            dataRoot,
                            jsonDataRoot,
                            configRoot,
                            settingsRoot,
                            settingSchemaRoot,
                            tempMarkdownRoot,
                            jsonBackupRoot;
    @Getter
    private final File      tempMarkdownFile;

    @Autowired
    public Paths(){
        String general = "config.json";
        JsonObject generalConfigs = null;
        try {
            generalConfigs = new Gson().fromJson(IOUtils.readFile(new ClassPathResource(general).getFile()), JsonObject.class);
        } catch (IOException e) {e.printStackTrace();}
        assert generalConfigs != null;
        resourceRoot = generalConfigs.get("resourceRoot").getAsString();
        dataRoot = resourceRoot + "data/";
        configRoot = resourceRoot + "configs/";
        jsonDataRoot = dataRoot + "json/data/";
        settingsRoot = configRoot + "settings.json";
        settingSchemaRoot = configRoot + "settingSchema.json";
        tempMarkdownRoot = configRoot + "tempMarkdown.md";
        jsonBackupRoot = dataRoot + "json/backup/";
        tempMarkdownFile = new File(tempMarkdownRoot);
    }


}
