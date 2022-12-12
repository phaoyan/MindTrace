package pers.juumii.MindTrace.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.general.LinkOpener;
import pers.juumii.MindTrace.model.service.general.Settings;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.quiz.generator.QuizGenerator;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.Constants;
import pers.juumii.MindTrace.utils.SpringUtils;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@CrossOrigin
@RestController
public class ServiceController {

    private final KTree kTree;
    private final LinkOpener linkOpener;
    private final Settings settings;

    @Autowired
    public ServiceController
            (KTree kTree,
             LinkOpener linkOpener,
             Settings settings) {
        this.kTree = kTree;
        this.linkOpener = linkOpener;
        this.settings = settings;
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
        List<QuizCard> quizzes = quizGenerator.quizzes(kTree.getConfigs().getQuizScale(), kTree);
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.ServiceController.generateQuiz", "");
        return JsonUtils.toJson(quizzes);
    }

    @GetMapping("utils/quiz/finished")
    public String queryQuizFinished(){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.ServiceController.queryQuizFinished", kTree.getNewlyAddedQuizCards().toString());
        return JsonUtils.toJson(kTree.getNewlyReviewedQuizCards());
    }

    @GetMapping("/utils/quiz/isDue")
    public boolean quizIsDue(){
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.ServiceController.quizIsDue", kTree.getConfigs().getTimeAnchor() == null ? Constants.timeAnchor.toString() : kTree.getConfigs().getTimeAnchor().toString());
        // 将当前时间和给定的时间锚点做差，若整除quizSchedule，则当天为quiz day，返回true。如果当前kTree没有定义时间锚点，那么就用Constants中的timeAnchor
        return Duration.between(kTree.getConfigs().getTimeAnchor() == null ? Constants.timeAnchor : kTree.getConfigs().getTimeAnchor(), Constants.today).toDays() % kTree.getConfigs().getQuizSchedule() == 0;
    }
}
