package com.github.joel003;

import com.github.joel003.controller_CLI.HomeInterface;

import java.util.Scanner;

public class Main {
    public static void main() {

        HomeInterface home = new HomeInterface();

        Scanner sc = new Scanner(System.in);
        home.homePage(sc);
        }
    }

