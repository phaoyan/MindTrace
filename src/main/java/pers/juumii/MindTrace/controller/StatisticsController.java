package pers.juumii.MindTrace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.juumii.MindTrace.model.service.statistics.Statistics;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.lang.reflect.InvocationTargetException;

@CrossOrigin
@RestController
public class StatisticsController {

    private final Statistics statistics;

    @Autowired
    public StatisticsController(Statistics statistics) {
        this.statistics = statistics;
    }

    @GetMapping("/statistics/general")
    public String getStatistics(String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.StatisticsController.getStatistics","");
        return JsonUtils.toJson(statistics.getClass().getMethod(methodName).invoke(statistics));
    }

}
