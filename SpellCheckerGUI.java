// SpellChecker 
// Author: Archit Jain
// Roll No:2401330130060

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpellCheckerGUI {

    public static int editDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + Math.min(
                        dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1])
                    );
            }
        }
        return dp[m][n];
    }

    public static List<String> getSuggestions(String word, List<String> dictionary) {
        List<String> suggestions = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;

        for (String dictWord : dictionary) {
            int distance = editDistance(word.toLowerCase(), dictWord.toLowerCase());

            if (distance < minDistance) {
                suggestions.clear();
                suggestions.add(dictWord);
                minDistance = distance;

            } else if (distance == minDistance) {
                suggestions.add(dictWord);
            }
        }
        return suggestions;
    }

    public static void main(String[] args) {
        List<String> dictionary = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Dictionary file not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame frame = new JFrame("Spell Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 450);
        frame.setLayout(new BorderLayout());

        Color bgColor = new Color(225, 240, 255);
        Color btnColor = new Color(180, 210, 255);
        frame.getContentPane().setBackground(bgColor);

        // Heading
        JLabel heading = new JLabel("SPELL CHECKER", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 28));
        heading.setForeground(new Color(20, 40, 120));
        frame.add(heading, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(bgColor);

        JLabel label = new JLabel("Enter a word:");
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JTextField textField = new JTextField(18);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JButton checkButton = new JButton("Check");
        checkButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        checkButton.setBackground(btnColor);

        inputPanel.add(label);
        inputPanel.add(textField);
        inputPanel.add(checkButton);

        frame.add(inputPanel, BorderLayout.CENTER);

        // Output + Footer panel
        JPanel southPanel = new JPanel(new BorderLayout());

        JTextArea outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        outputArea.setBackground(Color.WHITE);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("SansSerif", Font.BOLD, 20));
        outputArea.setForeground(new Color(0, 60, 0));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        southPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel footer = new JLabel(
            "Created by: Archit Jain (2401330130060)",
            SwingConstants.CENTER
        );
        footer.setFont(new Font("SansSerif", Font.BOLD, 18));
        footer.setForeground(new Color(120, 20, 20));
        southPanel.add(footer, BorderLayout.SOUTH);

        frame.add(southPanel, BorderLayout.SOUTH);

        // Button action
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText().trim().toLowerCase();

                if (input.isEmpty()) {
                    outputArea.setText("⚠ Please enter a word.");
                    return;
                }

                if (dictionary.contains(input)) {
                    outputArea.setText("✔ Correct spelling!");
                } else {
                    List<String> suggestions = getSuggestions(input, dictionary);

                    if (suggestions.isEmpty()) {
                        outputArea.setText("❌ No suggestions found.");
                    } else {
                        outputArea.setText("❓ Did you mean?\n" + String.join(", ", suggestions));
                    }
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

