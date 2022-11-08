package pers.juumii.MindTrace;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import pers.juumii.MindTrace.model.service.Repository;
import pers.juumii.MindTrace.model.service.KTree;
import pers.juumii.MindTrace.utils.ConsoleUtils;
import pers.juumii.MindTrace.utils.JsonUtils;
import pers.juumii.MindTrace.utils.SpringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@SpringBootApplication
public class MindTraceApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(MindTraceApplication.class, args);

		while (true) {
			String input = ConsoleUtils.lowercaseInput("Exit: X; Save: S");
			if (input.equals("x")) {
				SpringUtils.getBean(KTree.class).synchronize();
				SpringUtils.getBean(Repository.class).commit();
				System.exit(0);
			}else if(input.equals("s")){
				FileUtils.writeStringToFile(
					new ClassPathResource("/static/json/data/temp.json").getFile(),
					JsonUtils.toJson(SpringUtils.getBean(KTree.class).getRoot()),
					StandardCharsets.UTF_8);
			}
		}
	}

}
