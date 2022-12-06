package pers.juumii.MindTrace.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.service.statistics.Statistics;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.general.LinkOpener;
import pers.juumii.MindTrace.model.service.general.Settings;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.quiz.generator.QuizGenerator;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.Constants;
import pers.juumii.MindTrace.utils.SpringUtils;
import pers.juumii.MindTrace.utils.algorithm.DataUtils;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
public class ServiceController {

    private final KTree kTree;
    private final LinkOpener linkOpener;
    private final Settings settings;
    private final Statistics statistics;

    @Autowired
    public ServiceController
            (KTree kTree,
             LinkOpener linkOpener,
             Settings settings,
             Statistics statistics) {
        this.kTree = kTree;
        this.linkOpener = linkOpener;
        this.settings = settings;
        this.statistics = statistics;
    }

    @GetMapping("/utils/exit")
    public void exit(){}


    @PostMapping("/utils/openLink")
    public void openLink(@RequestBody String rawUrl) throws IOException {
        linkOpener.openLink(rawUrl);
    }

    @GetMapping("/utils/quiz/generate")
    public String generateQuiz() throws ClassNotFoundException {
        QuizGenerator quizGenerator = QuizGenerator.load(SpringUtils.getBeans(QuizGenerator.class), kTree.getConfigs().getQuizGenerator());
        //去除当天有过quizRecord记录的quizCard（这说明在当天的另一个时间点已经做过了这道题）
        List<QuizCard> quizzes = DataUtils.difference(quizGenerator.quizzes(kTree.getConfigs().getQuizScale(), kTree), statistics.completedQuizCards());

        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.ServiceController.generateQuiz", "");
        return JsonUtils.toJson(quizzes);
    }

    @GetMapping("utils/quiz/finished")
    public String queryQuizFinished(){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.ServiceController.queryQuizFinished", statistics.completedQuizCards().toString());
        return JsonUtils.toJson(statistics.completedQuizCards());
    }

    @GetMapping("/utils/quiz/isDue")
    public boolean quizIsDue(){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.ServiceController.quizIsDue", kTree.getConfigs().getTimeAnchor() == null ? Constants.timeAnchor.toString() : kTree.getConfigs().getTimeAnchor().toString());
        // 将当前时间和给定的时间锚点做差，若整除quizSchedule，则当天为quiz day，返回true。如果当前kTree没有定义时间锚点，那么就用Constants中的timeAnchor
        return Duration.between(kTree.getConfigs().getTimeAnchor() == null ? Constants.timeAnchor : kTree.getConfigs().getTimeAnchor(), LocalDateTime.now()).toDays() % kTree.getConfigs().getQuizSchedule() == 0;
    }

}
