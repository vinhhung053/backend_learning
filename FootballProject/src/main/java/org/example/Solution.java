package org.example;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập tên đội bóng: ");
        String team = scanner.nextLine();

        System.out.print("Nhập năm: ");
        String yearInput = scanner.nextLine();
        int year = Integer.parseInt(yearInput.trim());

        int result = Result.getTotalGoals(team, year);

        System.out.println("Tổng số bàn thắng của " + team + " trong năm " + year + " là: " + result);

        scanner.close();
    }
}