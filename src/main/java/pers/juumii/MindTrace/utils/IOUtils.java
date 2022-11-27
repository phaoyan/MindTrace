package pers.juumii.MindTrace.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class IOUtils {
    public static String readFile(File src){
        try {
            return FileUtils.readFileToString(src, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static String readFile(String src){
        return readFile(new File(src));
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

    public static void writeFile(String src, String text){
        writeFile(new File(src), text);
    }

    public static void renameFile(File src, File newPath){
        boolean result = src.renameTo(newPath);
        if(result)
            System.out.println("file renamed: " + src + " to " + newPath);
        else System.out.println(newPath.getAbsolutePath());
    }

    public static void copyFile(File src, File target) {
        String content = readFile(src);
        writeFile(target, content);
    }

    public static void deleteFile(File src){
        try {
            FileUtils.delete(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Collection<File> files = FileUtils.listFiles(new File("D:/learning/MindTrace/data/json/data"), null, false);
        files.forEach(file -> System.out.println(file.getName()));
    }
}
