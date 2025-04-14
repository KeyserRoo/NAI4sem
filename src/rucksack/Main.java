package rucksack;

import java.util.List;
import java.util.Random;

public class Main {
	public static void main(String[] args) throws Exception {
		Data data = Data.loadData("src/data/backpack/plecak.txt");
		int setNumber = generateNumber(1, 15);

		KnapsackBruteForce bruteForce = new KnapsackBruteForce(data);
		KnapsackHeuristic heuristic = new KnapsackHeuristic(data);

		showResult(data, setNumber, bruteForce);
		System.out.println("        +++++++++++++++++++++++++++++++++++");
		showResult(data, setNumber, heuristic);

	}

	private static void showResult(Data data, int setNumber, Knapsack knapsack) {
		long startTime = System.currentTimeMillis();
		knapsack.solveKnapsack(setNumber);
		long endTime = System.currentTimeMillis();

		System.out.println("dataset: " + setNumber);
		for (int[] optimalItems : knapsack.getItems()) {
			System.out.print(" number: " + optimalItems[0] +
					" size: " + optimalItems[1] +
					" value: " + optimalItems[2]);
			System.out.println();
		}
		System.out.println("knapsack value: " + knapsack.getTotalValue());
		System.out.println("knapsack size: " + knapsack.getTotalCapacity());
		System.out.println("execution time: " + (endTime - startTime) + "ms");
	}

	private static void print(Data data) {
		for (int i = 1; i <= 15; i++) {
			List<Element> list = data.getSetWithElements(i);

			int[] sizes = new int[30];
			int[] vals = new int[30];
			for (int j = 0; j < 30; j++) {
				sizes[j] = list.get(j).getSize();
				vals[j] = list.get(j).getValue();
			}

			System.out.print("dataset " + i + ":\n");
			System.out.print("sizes = {");
			for (int j = 0; j < 30; j++) {
				System.out.print(sizes[j] + ", ");
			}
			System.out.print("}\n");
			System.out.print("vals = {");
			for (int j = 0; j < 30; j++) {
				System.out.print(vals[j] + ", ");
			}
			System.out.print("}\n");
		}
	}

	private static int generateNumber(int from, int to) {
		Random rand = new Random();
		int randomNumber = rand.nextInt(to - from + 1) + from;
		return randomNumber;
	}
}
