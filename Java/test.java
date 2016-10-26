package project;

import java.util.Random;
import java.util.Scanner;

public class test {

	public static void main(String[] args) {
		System.out.println("*******************************************************");
		System.out.println("*                 CAM DICTIONARY                      *");
		System.out.println("*******************************************************");
		System.out.println("(c) Cameron Armstrong 2015                             ");
		System.out.println("");
		Scanner scanner = new Scanner(System.in);
		
		int h = 0;
		
		while (h < 1 || h > 2) {
			System.out.println("Select your option:");
			System.out.println("1. Create a dictionary");
			System.out.println("2. Performance analysis");
			
			h = scanner.nextInt();
		}
		
		if (h == 1)
			createDictionary();
		else
			performanceAnalysis();
	}
	
	public static void createDictionary() {
		Scanner scanner = new Scanner(System.in);
		int dictionaryNumber = 0;
		
		while (dictionaryNumber < 1 || dictionaryNumber > 2) {
			System.out.println("What will we be inserting into the dictionary?");
			System.out.println("1. Strings");
			System.out.println("2. Integers");
		
			dictionaryNumber = scanner.nextInt();
		}
		
		CamDictionary<String> dictionary1 = new CamDictionary<String>();
		CamDictionary<Integer> dictionary2 = new CamDictionary<Integer>();
		
		int i = 0;
		while (i < 1 || i > 4) {
			System.out.println("Select option:");
			System.out.println("1. Manually build dictionary");
			System.out.println("2. Automatic build with small dataset");
			System.out.println("3. Automatic build with large dataset");
			System.out.println("4. Automatic build with hardcore dataset");
			
			i = scanner.nextInt();
		}
		
		int j = 0;
		
		while (j < 1 || j > 2) {
			System.out.println("Enable optimisations (AVL tree balancing)?");
			System.out.println("1. Yes");
			System.out.println("2. No");
			
			j = scanner.nextInt();
		}
		
		if (j == 2) {
			dictionary1.noOptimisations = true;
			dictionary2.noOptimisations = true;
		}
		
		int k = 0;
		
		while (k < 1 || k > 2) {
			System.out.println("Enable live feedback of every operation?");
			System.out.println("Note: If optimisations are disabled, heights will all be shown as zero.");
			System.out.println("Note 2: It is recommended to turn this OFF for hardcore datasets.");
			System.out.println("1. Yes");
			System.out.println("2. No");
			
			k = scanner.nextInt();
		}
		
		if (k == 1) {
			dictionary1.visualFeedback = true;
			dictionary2.visualFeedback = true;
		}
		
		if (dictionaryNumber == 1)
			buildStringDictionary(dictionary1, i);
		else
			buildIntegerDictionary(dictionary2, i);
		
		boolean inProgram = true;
		
		while (inProgram) {
			int choice = 0;
		
				System.out.println("Which operation would you like to perform?");
				System.out.println("1. Add");
				System.out.println("2. Delete");
				System.out.println("3. Search");
				System.out.println("4. Min");
				System.out.println("5. Max");
				System.out.println("6. Predecessor");
				System.out.println("7. Successor");
				System.out.println("8. Display dictionary diagram");
				System.out.println("9. List all entries");
				System.out.println("10. Display log");
				System.out.println("11. Exit");
				System.out.println("12. Level order traversal");
			
				choice = scanner.nextInt();
			
			if (dictionaryNumber == 1) {
				switch (choice) {
					case 1:
						System.out.println("Add a string to the dictionary");
						dictionary1.add(scanner.next());
						break;
					case 2:
						System.out.println("Delete a string from the dictionary");
						dictionary1.delete(scanner.next());
						break;
					case 3:
						System.out.println("Search for a string in the dictionary");
						String string = scanner.next();
						boolean found = dictionary1.contains(string);
						if (found)
							System.out.println("String " + string + " was found!");
						else
							System.out.println("String " + string + " not found!");
						break;
					case 4:
						System.out.println("The min value is: " + dictionary1.min());
						break;
					case 5:
						System.out.println("The max value is: " + dictionary1.max());
						break;
					case 6:
						System.out.println("Find the predecessor of a string in the dictionary");
						string = scanner.next();
						System.out.println("The predecessor of " + string + " is " + dictionary1.predecessor(string));
						break;
					case 7:
						System.out.println("Find the successor of a string in the dictionary");
						string = scanner.next();
						System.out.println("The successor of " + string + " is " + dictionary1.successor(string));
						break;
					case 8:
						dictionary1.print();
						break;
					case 9:
						System.out.println(dictionary1.toString());
						break;
					case 10:
						System.out.println(dictionary1.getLogString());
						break;
					case 11:
						inProgram = false;
						break;
					case 12:
						LevelIterator iterator = new LevelIterator(dictionary1);
						while (iterator.hasNext()) {
							System.out.println(iterator.next());
						}
				}
			} else {
				switch (choice) {
					case 1:
						System.out.println("Add an integer to the dictionary");
						dictionary2.add(scanner.nextInt());
						break;
					case 2:
						System.out.println("Delete an integer from the dictionary");
						dictionary2.delete(scanner.nextInt());
						break;
					case 3:
						System.out.println("Search for an integer in the dictionary");
						int integer = scanner.nextInt();
						boolean found = dictionary2.contains(integer);
						if (found)
							System.out.println("Integer " + integer + " was found!");
						else
							System.out.println("Integer " + integer + " not found!");
						break;
					case 4:
						System.out.println("The min value is: " + dictionary2.min());
						break;
					case 5:
						System.out.println("The max value is: " + dictionary2.max());
						break;
					case 6:
						System.out.println("Find the predecessor of an integer in the dictionary");
						integer = scanner.nextInt();
						System.out.println("The predecessor of " + integer + " is " + dictionary2.predecessor(integer));
						break;
					case 7:
						System.out.println("Find the successor of an integer in the dictionary");
						integer = scanner.nextInt();
						System.out.println("The successor of " + integer + " is " + dictionary2.successor(integer));
						break;
					case 8:
						dictionary2.print();
						break;
					case 9:
						System.out.println(dictionary2.toString());
						break;
					case 10:
						System.out.println(dictionary2.getLogString());
						break;
					case 11:
						inProgram = false;
				}
			}
		}
	}
	
	public static void buildStringDictionary(CamDictionary<String> dictionary, int datasetSize) {
		if (datasetSize == 1)
			return;
		
		if (datasetSize == 2) {
			dictionary.add("dog");
			dictionary.add("cat");
			dictionary.add("brown");
			dictionary.add("zebra");
			dictionary.add("horse");
			dictionary.add("antelope");
			dictionary.add("cameron");
			dictionary.add("harrison");
			dictionary.add("mauricio");
			dictionary.add("david");
			dictionary.add("aardvark");
			return;
		} else if (datasetSize == 3) {
			dictionary.add("A");
			dictionary.add("B");
			dictionary.add("C");
			dictionary.add("D");
			dictionary.add("E");
			dictionary.add("F");
			dictionary.add("G");
			dictionary.add("H");
			dictionary.add("I");
			dictionary.add("J");
			dictionary.add("K");
			dictionary.add("L");
			dictionary.add("M");
			dictionary.add("N");
			dictionary.add("O");
			dictionary.add("P");
			dictionary.add("Q");
			dictionary.add("R");
			dictionary.add("S");
			dictionary.add("T");
			dictionary.add("U");
			dictionary.add("V");
			dictionary.add("W");
			dictionary.add("X");
			dictionary.add("Y");
			dictionary.add("Z");
			return;
		} else {
			System.out.println("Building dictionary. Stand by.");
			final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			Random random = new Random();
			StringBuilder sb = new StringBuilder(10);
			for (int entry = 0; entry < 10000; entry ++) {
			   for(int i = 0; i < 7; i++ ) 
			      sb.append(characters.charAt(random.nextInt(characters.length())));
			   dictionary.add(sb.toString());
			   sb.delete(0, 7);
			}
		}
	}
	
	public static void buildIntegerDictionary(CamDictionary<Integer> dictionary, int datasetSize) {
		if (datasetSize == 1)
			return;
		
		if (datasetSize == 2) {
			dictionary.add(1);
			dictionary.add(4);
			dictionary.add(7);
			dictionary.add(8);
			dictionary.add(13);
			dictionary.add(12);
			dictionary.add(3);
			dictionary.add(25);
			dictionary.add(108);
			dictionary.add(9);
			return;
		} else if (datasetSize == 3) {
			dictionary.add(1);
			dictionary.add(2);
			dictionary.add(3);
			dictionary.add(4);
			dictionary.add(5);
			dictionary.add(6);
			dictionary.add(7);
			dictionary.add(8);
			dictionary.add(9);
			dictionary.add(10);
			dictionary.add(11);
			dictionary.add(12);
			dictionary.add(13);
			dictionary.add(14);
			dictionary.add(15);
			dictionary.add(16);
			dictionary.add(17);
			dictionary.add(18);
			dictionary.add(19);
			dictionary.add(20);
			return;
		} else {
			System.out.println("Building dictionary. Stand by.");
			Random random = new Random();
			for (int i = 0; i < 10000; i++) {
				dictionary.add(random.nextInt(100000));
			}
		}
	}

	public static void performanceAnalysis() {
		CamDictionary<Integer> optimisedDictionary = new CamDictionary<Integer>();
		CamDictionary<Integer> unoptimisedDictionary = new CamDictionary<Integer>();
		unoptimisedDictionary.noOptimisations = true;
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("This mode will compare the performance of 2 randomly generated dictionaries with");
		System.out.println("the same data but one is optimised and the other is not.");
		System.out.println("Enter the size of the dictionary.");
		
		int size = scanner.nextInt();
		
		System.out.println("Building dictionaries. Stand by.");
		System.out.println("");
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			int data = random.nextInt(10*size);
			optimisedDictionary.add(data);
			unoptimisedDictionary.add(data);
		}
		int insertionTotalUnoptimised = unoptimisedDictionary.totalComparisons;
		int insertionTotalOptimised = optimisedDictionary.totalComparisons;
		int insertionMeanUnoptimised = unoptimisedDictionary.totalComparisons/size;
		int insertionMeanOptimised = optimisedDictionary.totalComparisons/size;
		int insertionMaxUnoptimised = unoptimisedDictionary.maxComparisons;
		int insertionMaxOptimised = optimisedDictionary.maxComparisons;
		double insertPercentage = (double)-(insertionTotalUnoptimised - insertionTotalOptimised) / (double)insertionTotalUnoptimised * 100.0;
		unoptimisedDictionary.maxComparisons = 0;
		optimisedDictionary.maxComparisons = 0;
		unoptimisedDictionary.totalComparisons = 0;
		optimisedDictionary.totalComparisons = 0;
		for (int i = 0; i < size; i++) {
			int data = random.nextInt(10*size);
			optimisedDictionary.contains(data);
			unoptimisedDictionary.contains(data);
		}
		System.out.println("\t\t\tUnoptimised \tOptimised");
		System.out.println("\t\t\t=========== \t=========");
		System.out.println("INSERTION");
		System.out.println("Total operations\t" + insertionTotalUnoptimised + "\t\t" + insertionTotalOptimised);
		System.out.println("Mean operations\t\t" + insertionMeanUnoptimised + "\t\t" + insertionMeanOptimised);
		System.out.println("Max operations\t\t" + insertionMaxUnoptimised + "\t\t" + insertionMaxOptimised);
		System.out.println("SEARCHING");
		System.out.println("Total operations\t" + unoptimisedDictionary.totalComparisons + "\t\t" + optimisedDictionary.totalComparisons);
		System.out.println("Mean operations\t\t" + unoptimisedDictionary.totalComparisons/size + "\t\t" + optimisedDictionary.totalComparisons/size);
		System.out.println("Max operations\t\t" + unoptimisedDictionary.maxComparisons + "\t\t" + optimisedDictionary.maxComparisons);
		System.out.println("");
		
		double searchPercentage = (double)(unoptimisedDictionary.totalComparisons - optimisedDictionary.totalComparisons) / (double)unoptimisedDictionary.totalComparisons * 100.0;
		
		System.out.printf("The optimised dictionary is %.2f%% more efficient for searches with a cost of %.2f%% reduced efficiency for insertions.\n\n", searchPercentage, insertPercentage);
	}
}
