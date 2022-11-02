package pers.juumii.MindTrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pers.juumii.MindTrace.model.data.QuizCard;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.model.service.quiz.generator.PureRandomQuiz;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.SpringUtils;
import pers.juumii.MindTrace.view.console.WelcomeUI;

import java.util.List;

@SpringBootApplication
public class MindTraceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindTraceApplication.class, args);

		while (true) {
			if (ConsoleUtils.lowercaseInput("Exit: X").equals("x")) {
				SpringUtils.getBean(Repository.class).commit();
				System.exit(0);
			}
		}
	}

}
