package pers.juumii.MindTrace.model.service.general;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.juumii.MindTrace.utils.IOUtils;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.Paths;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.util.Map;

@Service
public class Settings {

    @Getter
    @Setter
    private Map<String, Object> kvPairs;

    @Autowired
    @SuppressWarnings("unchecked")
    public Settings(Paths paths) {
        this.kvPairs = JsonUtils.readJson(IOUtils.readFile(paths.getSettingsRoot()), Map.class);

    }

    @SuppressWarnings("unchecked")
    public void load(){
        //从settings.json文件中加载json为kvPairs对象
        this.kvPairs = JsonUtils.readJson(IOUtils.readFile(SpringUtils.getBean(Paths.class).getSettingsRoot()), Map.class);
    }

    public void synchronize(){
        //将kvPairs序列化为json并写入settings.json文件
        IOUtils.writeFile(SpringUtils.getBean(Paths.class).getSettingsRoot(), JsonUtils.toJson(kvPairs));
    }

    public Object query(String key){
        return kvPairs.get(key);
    }
}
