package com.github.joel003.util;

public class ExtraFunctions {

    public static void clscr(int number) {

        int i=number;
        while (i <= 10) {
            System.out.println();
            i++;
        }
    }

    public static void delay(long millis) {
            try {
                Thread.sleep(millis);
            }catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }

}
