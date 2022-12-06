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
                            imgRoot,
                            imgVirtualRoot,
                            configRoot,
                            settingsRoot,
                            settingSchemaRoot,
                            tempMarkdownRoot,
                            jsonBackupRoot,
                            frontAppRoot;
    @Getter
    private final File      tempMarkdownFile;

    @Autowired
    public Paths(){
        // for deployment
//        JsonObject generalConfigs = new Gson().fromJson(IOUtils.readFile("config.json"), JsonObject.class);

        // for development
        JsonObject generalConfigs = null;
        try {
            generalConfigs = new Gson().fromJson(IOUtils.readFile(new ClassPathResource("config.json").getFile()), JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert generalConfigs != null;
        resourceRoot = generalConfigs.get("resourceRoot").getAsString();
        frontAppRoot = generalConfigs.get("frontAppRoot").getAsString();
        dataRoot = resourceRoot + "data/";
        configRoot = resourceRoot + "configs/";
        jsonDataRoot = dataRoot + "json/data/";
        imgRoot = dataRoot + "img/";
        imgVirtualRoot = "http://localhost:9090/static/img/";
        settingsRoot = configRoot + "settings.json";
        settingSchemaRoot = configRoot + "settingSchema.json";
        tempMarkdownRoot = configRoot + "tempMarkdown.md";
        jsonBackupRoot = dataRoot + "json/backup/";
        tempMarkdownFile = new File(tempMarkdownRoot);
    }


}
