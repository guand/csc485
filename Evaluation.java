package csc485project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Evaluation {

	// 10 fold
	public double tenFoldRMSE(int[][] table) {

		int empty = emptyElement(table);
		int total = table.length * table[0].length;
		int totalElement = table.length * table[0].length - emptyElement(table);
		int[][] tablePredicted;
		int[] predictedArray;
		List<int[]> predictedPostions;

		int foldSize = totalElement / 10;
		int lastFoldSize = totalElement % 10;
		int blankedCount;

		int tableIndexX = 0;
		int tableIndexY = 0;

		int[] actulValues;
		int actulValuesIndex;

		double[] sqrMeanArray = new double[10];

		for (int foldCount = 0; foldCount < 10; foldCount++) {
			//displayTable(table);
			tablePredicted = copyTable(table);
			blankedCount = 0;
			actulValuesIndex = 0;
			predictedPostions = new ArrayList<int[]>();
			actulValues = new int[foldSize];
			predictedArray = new int[foldSize];

			// Blank out the element in the subset
			outerloop: while (tableIndexX < table.length) {
				while (tableIndexY < table[0].length) {
					
					if(table[tableIndexX][tableIndexY] == 0) {
						tableIndexY++;
						continue;
						
					}

					// store the actual value in an array
					actulValues[actulValuesIndex] = table[tableIndexX][tableIndexY];
					actulValuesIndex++;

					tablePredicted[tableIndexX][tableIndexY] = 0;

					// store the position in an array, and put it in a list
					int[] positon = { tableIndexX, tableIndexY };
					predictedPostions.add(positon);

					tableIndexY++;
					blankedCount++;

					if (blankedCount == foldSize) {
						break outerloop;
					}
				}
				tableIndexY = 0;
				tableIndexX++;
			}

			// run recommendation
			BaseLine baseline = new BaseLine();
			baseline.compute(tablePredicted);
			//displayTable(tablePredicted);

			// Store the recommended rating in an array
			int predictedArrayIndex = 0;
			for (int[] position : predictedPostions) {

				predictedArray[predictedArrayIndex] = tablePredicted[position[0]][position[1]];
				predictedArrayIndex++;

			}

			int sumSqrDiff = 0;
			for (int i = 0; i < foldSize; i++) {
				sumSqrDiff = (int) (Math.pow(
						actulValues[i] - predictedArray[i], 2) + sumSqrDiff);
			}

			double mean = (double) sumSqrDiff / foldSize;
			double sqrMean = Math.sqrt(mean);
			sqrMeanArray[foldCount] = sqrMean;
		}

		return computeAvg(sqrMeanArray);
	}

	public double computeAvg(double[] sqrMeanArray) {

		double sum = 0.0;
		for (int i = 0; i < sqrMeanArray.length; i++) {

			sum = sum + sqrMeanArray[i];

		}

		return sum / sqrMeanArray.length;
	}

	public int emptyElement(int[][] table) {

		int totalEmpty = 0;
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				if (table[i][j] == 0) {
					totalEmpty++;
				}
			}
		}

		return totalEmpty;
	}

	public int[][] copyTable(int[][] table) {

		int[][] copiedTable = new int[table.length][table[0].length];

		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				copiedTable[i][j] = table[i][j];
			}
		}

		return copiedTable;
	}
	
	public void displayTable(int[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				System.out.print(table[i][j]+" ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException {
		DataParser parser = new DataParser();
		HashMap<Integer, ArrayList<Item>> userMap = new HashMap<Integer, ArrayList<Item>>();
		List<Integer> uniqueMoives = new ArrayList<Integer>();
		parser.parse(userMap, uniqueMoives);
		int[][] table = parser.toTable(userMap, uniqueMoives);

		Evaluation eva = new Evaluation();
		System.out.println("RMSE: "+eva.tenFoldRMSE(table));
		// BaseLine baseline = new BaseLine();
		// baseline.compute(table);
		// baseline.writeToFile(userMap, uniqueMoives, table);

	}
}
