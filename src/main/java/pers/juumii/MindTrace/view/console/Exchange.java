package pers.juumii.MindTrace.view.console;

import pers.juumii.MindTrace.utils.ConsoleUtils;

/**
 * 定义一组用户交互。由两部分组成：第一部分是prompt，提示信息并等待用户输入指令。第二部分是response，针对用户指令打印响应结果。
 *
 */
public class Exchange {
    private ContextInfo ctx;
    private String prompt;

    public void exchange(){
        response(ConsoleUtils.input(prompt));
    }

    private void response(String input) {

    }
}
