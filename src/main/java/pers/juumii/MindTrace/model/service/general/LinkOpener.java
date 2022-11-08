package pers.juumii.MindTrace.model.service.general;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.SpringConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class LinkOpener {

    private String defaultSearchEngine;

    private Map<String, String> abbrMap;

    @SuppressWarnings("unchecked")
    public LinkOpener(){
        try {
            String jsonString = FileUtils.readFileToString(new ClassPathResource("LinkOpener.json").getFile(), StandardCharsets.UTF_8);
            abbrMap = new Gson().fromJson(jsonString, HashMap.class);
            defaultSearchEngine = abbrMap.get("defaultSearchEngine");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openLink(String rawUrl) throws IOException {
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

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        LinkOpener opener = ctx.getBean(LinkOpener.class);
        opener.openLink("BACKs");
    }
}
