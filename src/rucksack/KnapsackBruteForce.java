package rucksack;

import java.util.ArrayList;
import java.util.List;

public class KnapsackBruteForce extends Knapsack {

	private int bestTotalValue = Integer.MIN_VALUE;
	private int bestTotalSize = 0;

	public KnapsackBruteForce(Data data) {
			super(data);
	}

	@Override
	public void solveKnapsack(int n) {
			List<Element> dataSet = data.getSetWithElements(n);
			int capacity = data.getMaxWeight();

			List<int[]> bestSolution = new ArrayList<>();
			findOptimalSolution(dataSet, capacity, 0, new ArrayList<>(), 0, 0, bestSolution);

			items = bestSolution;
			totalValue = bestTotalValue;
			totalCapacity = bestTotalSize;
	}

	private void findOptimalSolution(List<Element> dataSet, int capacity, int index, List<int[]> currentSelection, int currentTotalValue, int currentTotalSize, List<int[]> bestSolution) {
			numberOfCombinationsChecked++;

			if (index == dataSet.size()) {
					if (currentTotalValue > bestTotalValue) {
							bestTotalValue = currentTotalValue;
							bestTotalSize = currentTotalSize;
							bestSolution.clear();
							bestSolution.addAll(currentSelection);
					}
					return;
			}

			Element currentItem = dataSet.get(index);
			int itemValue = currentItem.getValue();
			int itemSize = currentItem.getSize();

			findOptimalSolution(dataSet, capacity, index + 1, currentSelection, currentTotalValue, currentTotalSize, bestSolution);

			if (currentTotalSize + itemSize <= capacity) {
					currentSelection.add(new int[] { index + 1, itemSize, itemValue });
					findOptimalSolution(dataSet, capacity, index + 1, currentSelection, currentTotalValue + itemValue, currentTotalSize + itemSize, bestSolution);
					currentSelection.remove(currentSelection.size() - 1);
			}
	}
}