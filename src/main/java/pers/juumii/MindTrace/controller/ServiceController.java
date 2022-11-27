package pers.juumii.MindTrace.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.juumii.MindTrace.model.Statistics;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.general.LinkOpener;
import pers.juumii.MindTrace.model.service.general.MarkdownEditor;
import pers.juumii.MindTrace.model.service.general.Settings;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.model.service.quiz.generator.QuizGenerator;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.JsonUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
public class ServiceController {

    private final KTree kTree;
    private final LinkOpener linkOpener;
    private final MarkdownEditor markdownEditor;
    private final QuizGenerator quizGenerator;
    private final Settings settings;
    private final Statistics statistics;

    @Autowired
    public ServiceController
            (KTree kTree,
             LinkOpener linkOpener,
             MarkdownEditor markdownEditor,
             List<QuizGenerator> quizGenerators,
             Settings settings,
             Statistics statistics) {
        this.kTree = kTree;
        this.linkOpener = linkOpener;
        this.markdownEditor = markdownEditor;
        this.quizGenerator = QuizGenerator.load(quizGenerators, settings);
        this.settings = settings;
        this.statistics = statistics;
    }

    @GetMapping("/utils/exit")
    public void exit(){}


    @PostMapping("/utils/openLink")
    public void openLink(@RequestBody String rawUrl) throws IOException {
        linkOpener.openLink(rawUrl);
    }

    @PostMapping("/utils/markdown/open")
    public void openMarkdown(@RequestBody String text){
        markdownEditor.open(text);
    }

    @GetMapping("/utils/markdown/close")
    public String closeMarkdown(){
        return markdownEditor.close();
    }

    @GetMapping("/utils/quiz/generate")
    public String generateQuiz(){
        quizGenerator.setScale((int)settings.query("quizScale"));
        List<QuizCard> quizzes = quizGenerator.quizzes();
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.ServiceController.generateQuiz", quizzes.toString());
        return JsonUtils.toJson(quizzes);
    }

    @GetMapping("/utils/quiz/isDue")
    public boolean quizIsDue(){
        if(kTree.getRoot() == null)
            return false;
        ConsoleUtils.printLocation("pers.juumii.MindTrace.controller.ServiceController.quizIsDue", statistics.lastQuizTime().toString());
        // 如果上一次 quizTask 在 now - quizSchedule 之前，那么说明到了再次进行quizTask的时间了
        return statistics.lastQuizTime().isBefore(LocalDateTime.now().minusDays((int)settings.query("quizSchedule")));
    }

}
