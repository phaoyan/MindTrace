package pers.juumii.MindTrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.model.service.KTree;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.SpringUtils;


@SpringBootApplication
public class MindTraceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindTraceApplication.class, args);

		while (true) {
			if (ConsoleUtils.lowercaseInput("Exit: X").equals("x")) {
				SpringUtils.getBean(KTree.class).synchronize();
				SpringUtils.getBean(Repository.class).commit();
				System.exit(0);
			}
		}
	}

}
