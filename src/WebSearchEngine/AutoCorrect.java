package WebSearchEngine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AutoCorrect {
    private static String wordsFileName = "src/WebSearchEngine/words.txt";
    public static String[] words; // Word list array

    public static void loadWords() {
        String line = null; // Temp variable for storing one line at a time
        ArrayList<String> temp = new ArrayList<String>();

        try {

            FileReader fileReader = new FileReader(wordsFileName);
            BufferedReader buffReader = new BufferedReader(fileReader);

            while ((line = buffReader.readLine()) != null) {
                temp.add(line.trim());
            }

            buffReader.close();
            words = new String[temp.size()];
            temp.toArray(words);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <E extends Comparable<E>> void sort(E[] array) {
        int n = array.length; // Get length of array

        // Insertion sort
        for (int i = 1; i < n; i++) {
            E temp = array[i]; // Save the element at index i
            int j = i - 1; // Let j be the element one index before i

            // Iterate through array
            while (j > -1 && (array[j].compareTo(temp) > 0)) {
                // Insert element at array[j] in proper place
                array[j + 1] = array[j];
                j--;
            }
            // Complete swap
            array[j + 1] = temp;
        }
    }

    public static void main(String[] args) {
        loadWords();
        sort(words);
        startSimulation();
    }

    public static void startSimulation() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a word: ");
            String word = scanner.nextLine().toLowerCase();

            System.out.print("Enter the edit distance: ");
            int distance = scanner.nextInt();

            List<String> suggestions = new ArrayList<>();

            for (String dictWord : words) {
                if (editDistance(word, dictWord) <= distance) {
                    suggestions.add(dictWord);
                }
            }

            if (suggestions.isEmpty()) {
                System.out.println("No suggestions found.");
                System.out.print("Do you want to add the word you provided to the word bank: (y/n)");
                String toggle = scanner.next().trim().toLowerCase();

                if (toggle.equalsIgnoreCase("yes") || toggle.equalsIgnoreCase("y")) {
                    try (PrintWriter output = new PrintWriter(new FileWriter("src/websearchengine/words.txt", true))) {
                        output.printf("%s\r\n", word);
                        System.out.println("\n'" + word + "' has been successfully added to the word bank!\n");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    loadWords();
                    sort(words);
                }

            } else {
                System.out.println("Suggestions:");
                for (String suggestion : suggestions) {
                    System.out.println(suggestion);
                }
            }
        }
    }

    private static int editDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        return dp[m][n];
    }
}