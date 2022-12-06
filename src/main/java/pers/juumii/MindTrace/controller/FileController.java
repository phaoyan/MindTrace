package pers.juumii.MindTrace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.Paths;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@CrossOrigin
@RestController
public class FileController {

    private final Paths paths;

    @Autowired
    public FileController(Paths paths) {
        this.paths = paths;
    }

    @PostMapping("/file/image/upload")
    public String uploadImage(@RequestParam("file") MultipartFile image) throws IOException {
        //参考https://www.cnblogs.com/shuisanya/p/16558056.html
        String imgName = LocalDateTime.now().toString().replaceAll(":","-").replaceAll("\\.","-");
        image.transferTo(new File(paths.getImgRoot() + imgName + ".png"));
        HashMap<String, String> res = new HashMap<>();
        res.put("url", paths.getImgVirtualRoot() + imgName + ".png");
        res.put("desc", imgName);
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.FileController.uploadImage", image.toString());
        return JsonUtils.toJson(res);
    }
}
