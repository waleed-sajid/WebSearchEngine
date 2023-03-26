package WebSearchEngine;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import Utilities.TST;

import static WebSearchEngine.AutoCorrect.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		boolean state = true;
		ArrayList<String> options = new ArrayList<String>() {
			{
				add("\n1. Search URL");
				add("2. Remove Cache");
				add("3. Search word");
				add("4. Auto-correct word");
				add("5. Auto-fill word");
				add("6. Show history");
				add("7. Delete history");
				add("8. Exit");
			}
		};

		do {
			for (String option : options) {
				Utilities.log(option);
			}

			int option = 0;
			Utilities.log("Please select one option between 1 to 8:");
			try {
				option = input.nextInt();
				if (option < 1 || option > 8) {
					System.out.println("\nEntered Number is not in the options!!!\n");
					return;
				}
			} catch (InputMismatchException e) {
				System.out.println("\nThat was not a number!!!\n");
				break;
			}

			switch (option) {

				case 1: {
					int depth = 1;
					String uri = "";
					Utilities.log("Selected option: " + options.get(option - 1));

					input.nextLine();
					// user input
					Utilities.log("Enter Uri");
					uri = input.nextLine();

					// check if valid Uri
					if (Pattern.matches(WebCrawler.urlPattern, uri)) {
						// check if present in cache
						if (!Cache.existsInCache(uri)) {
							// calling crawler
							try {
								WebCrawler.checkCrawl(1, uri, new ArrayList<String>(), depth);
							} catch (IOException e) {
								Utilities.log(e.getMessage());
							}
						} else {
							Utilities.log("This URL has already been crawled.");
						}
					} else {
						Utilities.log("Invalid uri");
					}
					break;
				}

				case 2: {
					Utilities.log("Selected option: " + options.get(option - 1));
					Cache.deleteCache();
					break;
				}

				case 3: {
					Utilities.log("Selected option: " + options.get(option - 1));
					try {
						KeywordSearch.read_files();
					} catch (Exception e) {
						Utilities.log(e.getMessage());
					}
					break;
				}

				case 4: {
					Utilities.log("Selected option: " + options.get(option - 1));
					System.out.println("\nAUTOCORRECT SIMULATION\n");
					loadWords();
					sort(words);
					startSimulation();
					break;
				}

				case 5: {
					Utilities.log("Selected option: " + options.get(option - 1));

					input.nextLine();
					Utilities.log("Enter word for suggestions (We use history.txt for this feature)");
					String word = input.nextLine();
					int top = 0;

					ArrayList<String> keys = new ArrayList<String>();

					// contains the history of searches by the user (for suggestions)
					TST<Integer> history = new TST<Integer>();
					try {
						File myObj = new File("history.txt");
						Scanner myReader = new Scanner(myObj);
						while (myReader.hasNextLine()) {
							String data = myReader.nextLine();
							keys.add(data.toLowerCase());
							history.put(data.toLowerCase(), top);
							top++;
						}
						myReader.close();
					} catch (Exception e) {
						Utilities.log("Error: " + e.getMessage());
					}
					history.printSimilarWords(word.toLowerCase());
					break;
				}

				case 6: {
					Utilities.log("Selected option: " + options.get(option - 1));
					Utilities.log("\nHistory of Search:\n");
					try {
						File myObject = new File("history.txt");
						Scanner FileReader = new Scanner(myObject);
						int count = 1;
						while (FileReader.hasNextLine()) {
							String datahistory = FileReader.nextLine();
							Utilities.log(count + ". " + datahistory);
							count++;
						}
						Utilities.log("\nSelect One Option\n");
						FileReader.close();
					} catch (Exception c) {
						Utilities.log("Error: " + c.getMessage());
					}
					break;
				}

				case 7: {
					Utilities.log("Selected option:\n " + options.get(option - 1));
					try {
						new FileOutputStream("history.txt").close();
						Utilities.log("Search history has been cleared!\n");
						Utilities.log("\nSelect One Option\n");
					} catch (Exception c) {
						Utilities.log("Error:" + c.getMessage());
					}
					break;
				}

				case 8: {
					Utilities.log("Selected option: " + options.get(option - 1));
					state = false;
					break;
				}
				default: {
					Utilities.log("Selected option: " + options.get(option - 1));
					break;
				}
			}
		} while (state);

		input.close();
	}

}