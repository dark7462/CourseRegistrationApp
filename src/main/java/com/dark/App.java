package com.dark;

import com.dark.swingGUI.Main;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        try {
            // Apply Nimbus Look and Feel for modern components
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Problem in loading LookAndFeel : " + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}