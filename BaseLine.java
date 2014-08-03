package csc485project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BaseLine {

	public void compute(int[][] table) {

		int sum = 0;
		int ratingCount = 0;
		
		int userRatingSum = 0;
		int userRatingCount = 0;
		
		double[] initialBuArray = new double [table.length];
		double[] initialBiArray = new double [table[0].length];
		int[] itemRatingCount = new int [table[0].length];
		

		// Loop through all the users
		for (int i = 0; i < table.length; i++) {
			// Loop through the movie that is watched by the current user
			for (int j = 0; j < table[0].length; j++) {
				if (table[i][j] != 0) {
					
					//for computing mean for all ratings
					sum = table[i][j] + sum;
					ratingCount++;
					
					//for compute initial user bias
					userRatingSum = table[i][j] + userRatingSum;
					userRatingCount++;
					
					//for compute initial item bias
					initialBiArray[j] = initialBiArray[j] + table[i][j];
					itemRatingCount[j] = itemRatingCount[j] + 1;
				}
			}
			initialBuArray[i] = (double)userRatingSum/userRatingCount;
			//reset for next user
			userRatingSum = 0;
			userRatingCount = 0;

		}
		
		//compute initial item bias
		for (int i = 0; i < table[0].length; i++) {
			initialBiArray[i] = (double) initialBiArray[i]/itemRatingCount[i];
		}
		
		
		int mean = Math.round(sum / ratingCount);
		System.out.println("Mean = " + mean);
		
		for (int i = 0; i < table.length; i++) {
			initialBuArray[i] = initialBuArray[i] - mean;
			
		}
		
		for (int i = 0; i < table[0].length; i++) {
			initialBiArray[i] = initialBiArray[i] - mean;
			
		}		
		
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {

				// need a loop here for all the u and i
				double bu = initialBuArray[i];
				double bi = initialBiArray[j];
				double previousBu = 0.0;
				double previousBi = 0.0;
				double r = 0.005;
				double l1 = 0.02;
				double l2 = 0.02;
				int predicted = 0;
				int currentValue = table[i][j];

				int loop_count = 0;

				while (true) {
					predicted = (int) Math.round(mean + bu + bi);
					if (predicted > 5) {
						predicted = 5;
					}
					previousBu = bu;
					bu = bu + r * (currentValue - predicted - l1 * bu);

					previousBi = bi;
					bi = bi + r * (currentValue - predicted - l2 * bi);
					loop_count ++;
					// will loop about 52 times
					if (((bu - previousBu) < 0.0001)&&((bi - previousBi) < 0.0001)) {
						break;
					}

				}
				System.out.println("origianl = " + currentValue);
				System.out.println("Bi = " + bi);
				System.out.println("Bu = " + bu);
				System.out.println("predicted = " + predicted + '\n');
				table[i][j] = predicted;
			}
			System.out.println();
		}

	}

	public void writeToFile(HashMap<Integer, ArrayList<Item>> userMap,
			List<Integer> uniqueMoives, int[][] table)
			throws FileNotFoundException, UnsupportedEncodingException {

		PrintWriter writer = new PrintWriter("baseline_results.csv", "UTF-8");
		writer.print(" ,");
		// save the movie id in file
		Iterator<Integer> movieIterator = uniqueMoives.iterator();
		while (movieIterator.hasNext()) {
			int movieId = movieIterator.next();
			writer.print(movieId + ", ");
		}
		writer.println();

		Iterator<Integer> iterator = userMap.keySet().iterator();

		for (int i = 0; i < table.length; i++) {
			int userId = iterator.next();
			writer.print(userId + ", ");
			for (int j = 0; j < table[0].length; j++) {
				writer.print(table[i][j] + ", ");
			}
			writer.println();
		}

		writer.close();

	}

	public static void main(String[] args) throws IOException {
		DataParser parser = new DataParser();
		HashMap<Integer, ArrayList<Item>> userMap = new HashMap<Integer, ArrayList<Item>>();
		List<Integer> uniqueMoives = new ArrayList<Integer>();
		parser.parse(userMap, uniqueMoives);
		int[][] table = parser.toTable(userMap, uniqueMoives);

		BaseLine baseline = new BaseLine();
		baseline.compute(table);
		baseline.writeToFile(userMap, uniqueMoives, table);

	}

}
