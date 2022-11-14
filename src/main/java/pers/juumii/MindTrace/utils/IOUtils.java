package pers.juumii.MindTrace.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IOUtils {
    public static String readFile(String src){
        try {
            return FileUtils.readFileToString(new ClassPathResource(src).getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static String readFile(File src){
        try {
            return FileUtils.readFileToString(src, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static void writeFile(String src, String text){
        try {
            FileUtils.writeStringToFile(new ClassPathResource(src).getFile(), text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                File file = new File(new ClassPathResource("application.properties").getFile().getParentFile().getAbsolutePath().replace("\\", "/") + src);
                if(file.createNewFile())
                    FileUtils.writeStringToFile(new ClassPathResource(src).getFile(), text, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void writeFile(File src, String text){
        try {
            FileUtils.writeStringToFile(src, text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                if(src.createNewFile())
                    FileUtils.writeStringToFile(src, text, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void renameFile(String src, String newPath){
        try {
            boolean result = new ClassPathResource(src).getFile().renameTo(new File(new ClassPathResource("application.properties").getFile().getParentFile().getAbsolutePath().replace("\\","/")+newPath));
            if(result)
                System.out.println("file renamed: " + src + " to " + newPath);
            else System.out.println(new ClassPathResource("application.properties").getFile().getAbsolutePath()+newPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void renameFile(File src, File newPath){
        boolean result = src.renameTo(newPath);
        if(result)
            System.out.println("file renamed: " + src + " to " + newPath);
        else System.out.println(newPath.getAbsolutePath());
    }
}
