package org.grupo11.Interface;

import java.util.Scanner;

public class CLI {
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Displays a list of choices to the user and returns the selected option.
     * 
     * @param prompt  the prompt message to display
     * @param choices an array of choices to display
     * @return the index of the selected choice
     */
    public static int listPrompt(String prompt, String[] choices) {
        System.out.println(prompt);

        for (int i = 0; i < choices.length; i++) {
            System.out.println((i + 1) + ". " + choices[i]);
        }

        int choice = -1;
        while (choice < 1 || choice > choices.length) {
            System.out.print("Enter your choice (1-" + choices.length + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return choice - 1; // Adjusting for zero-based index
    }

    /**
     * Prompts the user for input and returns the entered string.
     * 
     * @param prompt the prompt message to display
     * @return the user input
     */
    public static String inputPrompt(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }
}