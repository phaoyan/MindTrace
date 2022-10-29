package pers.juumii.MindTrace.utils;

import java.util.Scanner;

public class ConsoleUtils {

    public static String input(String prompt){
        System.out.print(prompt+": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String lowercaseInput(String prompt){
        return input(prompt).toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(input("hello input"));
    }
}
