// package rucksack;

// import java.util.ArrayList;
// import java.util.List;

// public abstract class Knapsack {
// 	protected final Data data;
// 	protected int totalCapacity = 0;
// 	protected int totalValue = 0;
// 	protected List<int[]> items = new ArrayList<>();

// 	public Knapsack(Data data) {
// 		this.data = data;
// 	}

// 	public abstract void solveKnapsack(int n);

// 	public List<int[]> getItems() {
// 		return items;
// 	}

// 	public int getTotalValue() {
// 		return totalValue;
// 	}

// 	public int getTotalCapacity() {
// 		return totalCapacity;
// 	}
// }