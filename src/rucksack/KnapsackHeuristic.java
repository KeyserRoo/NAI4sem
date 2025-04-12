// package rucksack;

// import java.util.List;

// public class KnapsackHeuristic extends Knapsack {
// 	public KnapsackHeuristic(Data data) {
// 		super(data);
// 	}

// 	@Override
// 	public void solveKnapsack(int n) {
// 		List<int[]> set = data.getSetWithArrays(n);
// 		int weight = data.getMaxWeight();

// 		for (int i = 0; i < set.get(0).length; i++) {
// 			int itemSize = set.get(0)[i];
// 			int itemValue = set.get(1)[i];

// 			if (totalCapacity + itemSize <= weight) {
// 				totalValue += itemValue;
// 				totalCapacity += itemSize;
// 				items.add(new int[] { i + 1, itemSize, itemValue });
// 			} else {
// 				break;
// 			}
// 		}
// 	}
// }

