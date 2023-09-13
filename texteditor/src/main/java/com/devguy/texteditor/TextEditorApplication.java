package com.devguy.texteditor;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class TextEditorApplication extends Application {
    private StringBuilder text;
    private List<String> undoStack;
    private List<String> redoStack;
    private TextArea textArea;
    private File currentFile;
    private UndoManager undoManager;


    public void TextEditor() {
        text = new StringBuilder();
        undoStack = new ArrayList<>();
        redoStack = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Editor");

        textArea = new TextArea();
        undoManager = new UndoManager(textArea);

        MenuBar menuBar = createMenuBar();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(textArea);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem saveAsMenuItem = new MenuItem("Save As");
        MenuItem exitMenuItem = new MenuItem("Exit");

        newMenuItem.setOnAction(e -> newDocument());
        openMenuItem.setOnAction(e -> openDocument());
        saveMenuItem.setOnAction(e -> saveDocument());
        saveAsMenuItem.setOnAction(e -> saveDocumentAs());
        exitMenuItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, new SeparatorMenuItem(), exitMenuItem);

        Menu editMenu = new Menu("Edit");
        MenuItem undoMenuItem = new MenuItem("Undo");
        MenuItem redoMenuItem = new MenuItem("Redo");
        MenuItem findMenuItem = new MenuItem("Find");
        MenuItem replaceMenuItem = new MenuItem("Replace");

        undoMenuItem.setOnAction(e -> undoManager.undo());
        redoMenuItem.setOnAction(e -> undoManager.redo());
        findMenuItem.setOnAction(e -> findText());
        replaceMenuItem.setOnAction(e -> replaceText());

        editMenu.getItems().addAll(undoMenuItem, redoMenuItem, new SeparatorMenuItem(), findMenuItem, replaceMenuItem);

        menuBar.getMenus().addAll(fileMenu, editMenu);

        return menuBar;
    }

    private void newDocument() {
        textArea.clear();
        currentFile = null;
    }

    private void openDocument() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                reader.close();
                currentFile = selectedFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDocument() {
        if (currentFile != null) {
            try {
                FileWriter writer = new FileWriter(currentFile);
                writer.write(textArea.getText());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDocumentAs() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                FileWriter writer = new FileWriter(selectedFile);
                writer.write(textArea.getText());
                writer.close();
                currentFile = selectedFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void findText() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Find Text");
        dialog.setHeaderText("Enter the text to find:");
        dialog.setContentText("Text:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(textToFind -> {
            String text = textArea.getText();
            int index = text.indexOf(textToFind);
            if (index != -1) {
                textArea.selectRange(index, index + textToFind.length());
            }
        });
    }

    private void replaceText() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Replace Text");
        dialog.setHeaderText("Enter the text to find and replace:");
        dialog.setContentText("Find:");

        Optional<String> findResult = dialog.showAndWait();
        findResult.ifPresent(findText -> {
            TextInputDialog replaceDialog = new TextInputDialog();
            replaceDialog.setTitle("Replace Text");
            replaceDialog.setHeaderText("Enter the replacement text:");
            replaceDialog.setContentText("Replace with:");

            Optional<String> replaceResult = replaceDialog.showAndWait();
            replaceResult.ifPresent(replaceText -> {
                String text = textArea.getText();
                String newText = text.replace(findText, replaceText);
                textArea.setText(newText);
            });
        });
    }

    public class UndoManager {
        private Stack<String> undoStack;
        private Stack<String> redoStack;

        public UndoManager(TextArea textArea) {
            undoStack = new Stack<>();
            redoStack = new Stack<>();
        }

        public void addToUndoStack(String text) {
            undoStack.push(text);
        }

        public String undo() {
            if (!canUndo()) {
                return null; // Nothing to undo
            }
            String undoneText = undoStack.pop();
            redoStack.push(undoneText);
            return undoneText;
        }

        public String redo() {
            if (!canRedo()) {
                return null; // Nothing to redo
            }
            String redoneText = redoStack.pop();
            undoStack.push(redoneText);
            return redoneText;
        }

        public boolean canUndo() {
            return !undoStack.isEmpty();
        }

        public boolean canRedo() {
            return !redoStack.isEmpty();
        }
    }

    public void saveChange(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(text.toString());
            System.out.println("File saved successfully.");
        } catch (IOException e) {
            System.err.println("Error: Unable to save the file.");
            e.printStackTrace();
        }
    }

    public void insertText(String newText) {
        text.append(newText);
        undoStack.add(text.toString());
        redoStack.clear();
    }



}