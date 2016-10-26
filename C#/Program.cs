using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CamDictionary;

namespace CamDictionaryConsole
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.SetWindowSize(150, 40);
            Console.ForegroundColor = ConsoleColor.Green;
		Console.WriteLine("*******************************************************");
		Console.WriteLine("*                 CAM DICTIONARY                      *");
		Console.WriteLine("*******************************************************");
        Console.ForegroundColor = ConsoleColor.Red;
		Console.WriteLine("(c) Cameron Armstrong 2015                             ");
		Console.WriteLine("");
        Console.ForegroundColor = ConsoleColor.White;
		int h = 0;
		
		while (h < 1 || h > 2) {
			Console.WriteLine("Select your option:");
			Console.WriteLine("1. Create a dictionary");
			Console.WriteLine("2. Performance analysis");
			
			h = Convert.ToInt32(Console.ReadLine());
		}
		
		if (h == 1)
			createDictionary();
		else
			performanceAnalysis();
	}
	
	public static void createDictionary() {
		int dictionaryNumber = 0;
		
		while (dictionaryNumber < 1 || dictionaryNumber > 2) {
			Console.WriteLine("What will we be inserting into the dictionary?");
			Console.WriteLine("1. Strings");
			Console.WriteLine("2. Integers");
		
			dictionaryNumber = Convert.ToInt32(Console.ReadLine());
		}

		CamDictionary<string> dictionary1 = new CamDictionary<string>();
		CamDictionary<int> dictionary2 = new CamDictionary<int>();
		
		int i = 0;
		while (i < 1 || i > 4) {
			Console.WriteLine("Select option:");
			Console.WriteLine("1. Manually build dictionary");
			Console.WriteLine("2. Automatic build with small dataset");
			Console.WriteLine("3. Automatic build with large dataset");
			Console.WriteLine("4. Automatic build with hardcore dataset");
			
			i = Convert.ToInt32(Console.ReadLine());
		}
		
		int j = 0;
		
		while (j < 1 || j > 2) {
			Console.WriteLine("Enable optimisations (AVL tree balancing)?");
			Console.WriteLine("1. Yes");
			Console.WriteLine("2. No");
			
			j = Convert.ToInt32(Console.ReadLine());
		}
		
		if (j == 2) {
			dictionary1.noOptimisations = true;
			dictionary2.noOptimisations = true;
		}
		
		int k = 0;
		
		while (k < 1 || k > 2) {
			Console.WriteLine("Enable live feedback of every operation?");
			Console.WriteLine("Note: If optimisations are disabled, heights will all be shown as zero.");
			Console.WriteLine("Note 2: It is recommended to turn this OFF for large datasets.");
			Console.WriteLine("It will crash on systems with low RAM if you try!!");
			Console.WriteLine("1. Yes");
			Console.WriteLine("2. No");
			
			k = Convert.ToInt32(Console.ReadLine());
		}
		
		if (k == 1) {
			dictionary1.visualFeedback = true;
			dictionary2.visualFeedback = true;
		}
		
		if (dictionaryNumber == 1)
			buildStringDictionary(dictionary1, i);
		else
			buildIntegerDictionary(dictionary2, i);
		
		bool inProgram = true;

		while (inProgram) {
			int choice = 0;
            Console.ForegroundColor = ConsoleColor.Gray;
				Console.WriteLine("Which operation would you like to perform?");
				Console.WriteLine("1. Add");
				Console.WriteLine("2. Delete");
				Console.WriteLine("3. Search");
				Console.WriteLine("4. Min");
				Console.WriteLine("5. Max");
				Console.WriteLine("6. Predecessor");
				Console.WriteLine("7. Successor");
				Console.WriteLine("8. Display dictionary diagram");
				Console.WriteLine("9. List all entries");
				Console.WriteLine("10. Display log");
				Console.WriteLine("11. Exit");
			
				choice = Convert.ToInt32(Console.ReadLine());
                Console.ForegroundColor = ConsoleColor.Yellow;
			if (dictionaryNumber == 1) {
				switch (choice) {
					case 1:
						Console.WriteLine("Add a string to the dictionary");
						dictionary1.add(Console.ReadLine());
						break;
					case 2:
						Console.WriteLine("Delete a string from the dictionary");
						dictionary1.delete(Console.ReadLine());
						break;
					case 3:
						Console.WriteLine("Search for a string in the dictionary");
						string stringToSearch = Console.ReadLine();
						bool found = dictionary1.contains(stringToSearch);
						if (found)
							Console.WriteLine("String " + stringToSearch + " was found!");
						else
							Console.WriteLine("String " + stringToSearch + " not found!");
						break;
					case 4:
						Console.WriteLine("The min value is: " + dictionary1.min());
						break;
					case 5:
						Console.WriteLine("The max value is: " + dictionary1.max());
						break;
					case 6:
						Console.WriteLine("Find the predecessor of a string in the dictionary");
						stringToSearch = Console.ReadLine();
						Console.WriteLine("The predecessor of " + stringToSearch + " is " + dictionary1.predecessor(stringToSearch));
						break;
					case 7:
						Console.WriteLine("Find the successor of a string in the dictionary");
						stringToSearch = Console.ReadLine();
						Console.WriteLine("The successor of " + stringToSearch + " is " + dictionary1.successor(stringToSearch));
						break;
					case 8:
						dictionary1.print();
						break;
					case 9:
						Console.WriteLine(dictionary1.toString());
						break;
					case 10:
						Console.WriteLine(dictionary1.getLogString());
						break;
					case 11:
						inProgram = false;
                        break;
				}
			} else {
				switch (choice) {
					case 1:
						Console.WriteLine("Add an integer to the dictionary");
						dictionary2.add(Convert.ToInt32(Console.ReadLine()));
						break;
					case 2:
						Console.WriteLine("Delete an integer from the dictionary");
						dictionary2.delete(Convert.ToInt32(Console.ReadLine()));
						break;
					case 3:
						Console.WriteLine("Search for an integer in the dictionary");
						int integer = Convert.ToInt32(Console.ReadLine());
						bool found = dictionary2.contains(integer);
						if (found)
							Console.WriteLine("Integer " + integer + " was found!");
						else
							Console.WriteLine("Integer " + integer + " not found!");
						break;
					case 4:
						Console.WriteLine("The min value is: " + dictionary2.min());
						break;
					case 5:
						Console.WriteLine("The max value is: " + dictionary2.max());
						break;
					case 6:
						Console.WriteLine("Find the predecessor of an integer in the dictionary");
						integer = Convert.ToInt32(Console.ReadLine());
						Console.WriteLine("The predecessor of " + integer + " is " + dictionary2.predecessor(integer));
						break;
					case 7:
						Console.WriteLine("Find the successor of an integer in the dictionary");
						integer = Convert.ToInt32(Console.ReadLine());
						Console.WriteLine("The successor of " + integer + " is " + dictionary2.successor(integer));
						break;
					case 8:
						dictionary2.print();
						break;
					case 9:
						Console.WriteLine(dictionary2.toString());
						break;
					case 10:
						Console.WriteLine(dictionary2.getLogString());
						break;
					case 11:
						inProgram = false;
                        break;
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
			Console.WriteLine("Building dictionary. Stand by.");
			const string characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			Random random = new Random();
			StringBuilder sb = new StringBuilder(10);
			for (int entry = 0; entry < 10000; entry ++) {
			   for(int i = 0; i < 7; i++ ) 
			      sb.Append(characters.ElementAt(random.Next(characters.Length)));
			   dictionary.add(sb.ToString());
			   sb.Remove(0, 7);
			}
		}
	}
	
	public static void buildIntegerDictionary(CamDictionary<int> dictionary, int datasetSize) {
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
			Console.WriteLine("Building dictionary. Stand by.");
			Random random = new Random();
			for (int i = 0; i < 10000; i++) {
				dictionary.add(random.Next(100000));
			}
		}
	}

	public static void performanceAnalysis() {
		CamDictionary<int> optimisedDictionary = new CamDictionary<int>();
		CamDictionary<int> unoptimisedDictionary = new CamDictionary<int>();
		unoptimisedDictionary.noOptimisations = true;
		
		Console.WriteLine("This mode will compare the performance of 2 randomly generated dictionaries with");
		Console.WriteLine("the same data but one is optimised and the other is not.");
		Console.WriteLine("Enter the size of the dictionary.");
		
		int size = Convert.ToInt32(Console.ReadLine());
        Console.ForegroundColor = ConsoleColor.Cyan;
		Console.WriteLine("Building dictionaries. Stand by.");
		Console.WriteLine("");
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			int data = random.Next(10*size);
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
			int data = random.Next(10*size);
			optimisedDictionary.contains(data);
			unoptimisedDictionary.contains(data);
		}
        Console.ForegroundColor = ConsoleColor.Magenta;
		Console.WriteLine("\t\t\tUnoptimised \tOptimised");
        Console.ForegroundColor = ConsoleColor.White;
		Console.WriteLine("\t\t\t=========== \t=========");
        Console.ForegroundColor = ConsoleColor.Yellow;
		Console.WriteLine("INSERTION");
        Console.ForegroundColor = ConsoleColor.Green;
		Console.WriteLine("Total operations\t" + insertionTotalUnoptimised + "\t\t" + insertionTotalOptimised);
		Console.WriteLine("Mean operations\t\t" + insertionMeanUnoptimised + "\t\t" + insertionMeanOptimised);
		Console.WriteLine("Max operations\t\t" + insertionMaxUnoptimised + "\t\t" + insertionMaxOptimised);
        Console.ForegroundColor = ConsoleColor.Yellow;
		Console.WriteLine("SEARCHING");
        Console.ForegroundColor = ConsoleColor.Green;
		Console.WriteLine("Total operations\t" + unoptimisedDictionary.totalComparisons + "\t\t" + optimisedDictionary.totalComparisons);
		Console.WriteLine("Mean operations\t\t" + unoptimisedDictionary.totalComparisons/size + "\t\t" + optimisedDictionary.totalComparisons/size);
		Console.WriteLine("Max operations\t\t" + unoptimisedDictionary.maxComparisons + "\t\t" + optimisedDictionary.maxComparisons);
		Console.WriteLine("");
		
		double searchPercentage = (double)(unoptimisedDictionary.totalComparisons - optimisedDictionary.totalComparisons) / (double)unoptimisedDictionary.totalComparisons * 100.0;
        Console.ForegroundColor = ConsoleColor.White;
		Console.WriteLine("The optimised dictionary is {0:0.00}% more efficient for searches with a cost of {1:0.00}% reduced efficiency for insertions.\n\n", searchPercentage, insertPercentage);
        Console.ReadKey();
	}
        }
}

