package com.devguy.texteditor;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Text Editor Menu:");
            System.out.println("1. Insert Text");
            System.out.println("2. Save");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter text to insert: ");
                    String newText = scanner.nextLine();
                    editor.insertText(newText);
                    break;
                case 2:
                    System.out.print("Enter the file path to save: ");
                    String filePath = scanner.nextLine();
                    editor.saveChange(filePath);
                    break;
                case 3:
                    System.out.println("Exiting Text Editor.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
