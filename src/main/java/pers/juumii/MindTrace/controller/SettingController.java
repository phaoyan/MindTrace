package pers.juumii.MindTrace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.service.general.Settings;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.util.Map;

@CrossOrigin
@RestController
public class SettingController {

    private final Settings settings;

    @Autowired
    public SettingController(Settings settings) {
        this.settings = settings;
    }

    @GetMapping("/settings/load")
    public String loadSettings(){
        settings.load();
        return JsonUtils.toJson(settings.getKvPairs());
    }

    @SuppressWarnings("unchecked")
    @PostMapping("settings/synchronize")
    public void synchronizeSettings(@RequestBody String settingsJson){
        Map<String, Object> kvPairs = JsonUtils.readJson(settingsJson, Map.class);
        if(kvPairs == null || kvPairs.isEmpty())
            return;
        settings.getKvPairs().forEach((key, value) ->{
            if(kvPairs.containsKey(key))
                settings.getKvPairs().put(key, kvPairs.get(key));
        });
        settings.synchronize();
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.SettingController.synchronizeSettings", kvPairs.toString());
    }
}
