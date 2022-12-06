package pers.juumii.MindTrace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pers.juumii.MindTrace.model.service.ktree.KTree;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.Paths;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.io.IOException;


@SpringBootApplication
public class MindTraceApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(MindTraceApplication.class, args);
		//打开前端
//		Runtime.getRuntime().exec("cmd /c start " + "\"\" \"" + SpringUtils.getBean(Paths.class).getFrontAppRoot() + "\"");

		while (true) {
			String input = ConsoleUtils.lowercaseInput("Exit: X");
			if (input.equals("x")) {
				SpringUtils.getBean(KTree.class).synchronize();
				System.exit(0);
			}
		}
	}

}
