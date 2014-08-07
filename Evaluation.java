package csc485project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Evaluation {

	// 10 fold cross validation using RMSE, precision and recall. We categorize rating "1-3" as irrelevant and
	// "4-5" as relevant
	public double tenFoldRMSE(int[][] table, List<Integer> uniqueMoives,
			int mode) throws IOException {

		int totalElement = table.length * table[0].length - emptyElement(table);
		int[][] tablePredicted;
		int[] predictedArray;
		List<int[]> predictedPostions;

		int foldSize = totalElement / 10;
		int blankedCount;

		int tableIndexX = 0;
		int tableIndexY = 0;

		int[] actulValues;
		int actulValuesIndex;
		
		int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;

		double[] sqrMeanArray = new double[10];

		for (int foldCount = 0; foldCount < 10; foldCount++) {
			// displayTable(table);
			tablePredicted = copyTable(table);
			blankedCount = 0;
			actulValuesIndex = 0;
			predictedPostions = new ArrayList<int[]>();
			actulValues = new int[foldSize];
			predictedArray = new int[foldSize];

			// Blank out the element in the subset
			outerloop: while (tableIndexX < table.length) {
				while (tableIndexY < table[0].length) {

					if (table[tableIndexX][tableIndexY] == 0) {
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

			// Run Hybrid model
			if (mode == 0) {
				tablePredicted = new Hybrid().compute(tablePredicted,table);
			}

			// Run Pearson
			else if (mode == 1) {
				tablePredicted = new NewPearson().compute(tablePredicted);

			}

			// Run Base Line
			else if (mode == 2) {
				new BaseLine().compute(tablePredicted);
			}

			//displayTable(tablePredicted,table);
			//System.out.println();

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
				
				// compute precision and recall
				// count number of relevant in ground truth table
				if (actulValues[i] > 3) {
					
					// Recommendation predicted the item as relevant as ground
					// truth
					if (predictedArray[i] > 3) {
						TP++;
					}
					// Recommendation predicted the item as irrelevant,
					// different from ground truth
					else {
						FN++;
					}
					
				} else {
					
					// Recommendation predicted the item as relevant, different
					// from ground truth
					if (predictedArray[i] > 3) {
						FP++;
					}
					
					// Recommendation predicted the item as irrelevant as ground
					// truth
					else {
						TN++;
					}
					
				}

			}

			double mean = (double) sumSqrDiff / foldSize;
			double sqrMean = Math.sqrt(mean);
			sqrMeanArray[foldCount] = sqrMean;
			
		}
		double precision = (double) TP / (TP + FP);
		double recall = (double) TP / (TP + FN);
		double accurcy = (double) (TP + TN) / (TP + TN + FN + FP);

		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall);
		System.out.println("accurcy: " + accurcy);

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

	// display preditcted table and original table for debuging
	public void displayTable(int[][] tablePredicted, int[][] table) {
		for (int i = 0; i < tablePredicted.length; i++) {
			for (int j = 0; j < tablePredicted[0].length; j++) {
				System.out.print(tablePredicted[i][j] + ""+table[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException {
		DataParser parser = new DataParser();
		Map<Integer, ArrayList<Item>> userMap = new HashMap<Integer, ArrayList<Item>>();
		Map<Integer, ArrayList<Item>> newUserMap = new HashMap<Integer, ArrayList<Item>>();
		List<Integer> uniqueMoives = new ArrayList<Integer>();
		parser.parse(userMap, uniqueMoives);
		parser.parse(newUserMap, uniqueMoives);

		int[][] table = parser.toTable(userMap, uniqueMoives);

		Evaluation eva = new Evaluation();

		System.out.println("Pearson Results");
		System.out.println("RMSE: " + eva.tenFoldRMSE(table, uniqueMoives, 1) + "\n");

		System.out.println("BaseLine");
		System.out.println("RMSE: " + eva.tenFoldRMSE(table, uniqueMoives, 2) + "\n");

		System.out.println("Hybrid");
		System.out.println("RMSE: " + eva.tenFoldRMSE(table, uniqueMoives, 0) + "\n");
	}
}
