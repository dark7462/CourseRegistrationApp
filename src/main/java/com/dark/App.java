package com.dark;

import com.dark.swingGUI.Main;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}