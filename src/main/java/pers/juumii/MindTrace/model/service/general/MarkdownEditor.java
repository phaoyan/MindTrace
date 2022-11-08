package pers.juumii.MindTrace.model.service.general;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 对于persistent数据的文本成员，如desc，front，back等，如果其有前缀$EXTERNAL$,则判定为应该用Markdown编辑器打开的内容
 * 这些数据不会在前端界面显示，而是通过前端提供的一个外链连接到这一MarkdownEditor对象
 * MarkdownEditor负责打开temp.md文件并将文本数据读入，同时负责将temp.md修改后的文本反过来写入原persistent数据中
 */
@Service
public class MarkdownEditor {

    private File src;
    public static final String EXTERNAL_PREFIX = "$EXTERNAL$";

    public MarkdownEditor(){
        try {
            src = new ClassPathResource("static/temp/temp.md").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取persistent数据的external打开temp.md
    public void open(String external){
        try {
            FileUtils.writeStringToFile(src, external.replace("$EXTERNAL$",""), StandardCharsets.UTF_8);
            Runtime.getRuntime().exec("cmd /c " + src.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //将temp.md中的文本录入persistent数据的external中
    public String close(){
        try {
            String res = EXTERNAL_PREFIX + FileUtils.readFileToString(src, StandardCharsets.UTF_8);
            FileUtils.writeStringToFile(src, "", StandardCharsets.UTF_8);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
