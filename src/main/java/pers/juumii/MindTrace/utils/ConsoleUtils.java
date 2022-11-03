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

    public static <T> void printLocation(Class<T> cl, String info){
        System.out.println(cl.getName() + ": " + info);
    }

    public static <T> void printLocation(String loc, String info){
        System.out.println(loc + ": " + info);
    }

    public static void main(String[] args) {
        System.out.println(input("hello input"));
    }
}
