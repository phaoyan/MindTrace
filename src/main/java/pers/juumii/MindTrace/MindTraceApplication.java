package pers.juumii.MindTrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.quiz.generator.PureRandomQuiz;
import pers.juumii.MindTrace.utils.SpringUtils;
import pers.juumii.MindTrace.view.console.WelcomeUI;

import java.util.List;

@SpringBootApplication
public class MindTraceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindTraceApplication.class, args);
		List<QuizCard> analyze = SpringUtils.getBean(PureRandomQuiz.class).analyze(10);
		for(QuizCard quizCard: analyze)
			System.out.println(quizCard);
	}

}
