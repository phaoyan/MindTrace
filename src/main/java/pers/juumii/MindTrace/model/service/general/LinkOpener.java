package pers.juumii.MindTrace.model.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class LinkOpener {

    private String defaultSearchEngine;
    private Map<String, String> abbrMap;

    @Autowired
    @SuppressWarnings("unchecked")
    public LinkOpener(Settings settings){
        abbrMap = (Map<String ,String>)settings.query("localLinks");
        defaultSearchEngine = abbrMap.get("defaultSearchEngine");
    }

    @SuppressWarnings("unchecked")
    private void load() {
        abbrMap = (Map<String, String>) SpringUtils.getBean(Settings.class).query("localLinks");
        defaultSearchEngine = abbrMap.get("defaultSearchEngine");
    }

    public void openLink(String rawUrl) throws IOException {
        load();
        String valid = check(rawUrl);
        Runtime.getRuntime().exec("cmd /c start " + valid);
    }

    private String check(String rawUrl) {
        if(abbrMap.containsKey(rawUrl))
            return abbrMap.get(rawUrl);
        else if(validate(rawUrl))
            return rawUrl;
        else
            return defaultSearchEngine + rawUrl;
    }

    private boolean validate(String rawUrl){
        String filePathRegex = "[a-zA-Z]:[(\\\\)/].*?";
        String websiteRegex = "https?://.*?";
        return Pattern.matches(filePathRegex, rawUrl) || Pattern.matches(websiteRegex, rawUrl);
    }
}
