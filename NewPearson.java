package csc485project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class NewPearson {

	public int[][] compute(int[][] table) {

		// Create a empty table so we can store the predicted values
		int[][] tablePredicted = new int[table.length][table[0].length];
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				//blank the value and run prediction, then restore the value back
				int temp = table[i][j];
				table[i][j] = 0;
				tablePredicted[i][j] = predict(i, j, table);
				table[i][j] = temp;
			}
		}

		return tablePredicted;
	}

	public int predict(int userI, int itemJ, int[][] table) {

		// for storing who else also rated item j && the similarity between
		// users
		ArrayList<Integer> userList = new ArrayList<Integer>();
		ArrayList<Double> simList = new ArrayList<Double>();

		// Compute userI's mean
		double userIMean = 0.0;
		int userICount = 0;
		for (int i = 0; i < table[0].length; i++) {

			if (table[userI][i] != 0) {
				userIMean = userIMean + table[userI][i];
				userICount++;
			}
		}
		userIMean = userIMean / userICount;

		// Find who else also rated the same item as user I
		for (int i = 0; i < table.length; i++) {
			if ((table[i][itemJ] != 0) && i != userI) {
				userList.add(i);
			}
		}

		double numerator = 0.0;
		// compute the numerator part
		for (int otherUser : userList) {
			// compute Similarity between UserI and any other user in the list
			double sim = Similarity(userI, otherUser, table);
			simList.add(sim);

			// compute the user mean
			double otherUserMean = 0.0;
			int otherUserCount = 0;
			for (int i = 0; i < table[0].length; i++) {

				if ((table[userI][i] != 0) && (table[otherUser][i]) != 0) {
					otherUserMean = otherUserMean + table[otherUser][i];
					otherUserCount++;
				}
			}
			otherUserMean = otherUserMean / otherUserCount;
			numerator = numerator + sim
					* (table[otherUser][itemJ] - otherUserMean);

		}

		double denominator = 0.0;
		// compute denominator
		for (double sim : simList) {

			denominator = denominator + Math.abs(sim);
		}

		double prediction = userIMean + numerator / denominator;

		return (int) Math.round(prediction);
	}

	// Compute Similarity using Pearson Correlation
	public double Similarity(int user1, int user2, int[][] table) {

		double sim = 0.0;
		double user1Mean = 0.0;
		double user2Mean = 0.0;
		int user1Count = 0;
		int user2Count = 0;
		ArrayList<Integer> user1Ratings = new ArrayList<Integer>();
		ArrayList<Integer> user2Ratings = new ArrayList<Integer>();

		for (int i = 0; i < table[0].length; i++) {

			if ((table[user1][i] != 0) && (table[user2][i] != 0)) {
				user1Mean = user1Mean + table[user1][i];
				user1Count++;
				user2Mean = user2Mean + table[user2][i];
				user2Count++;

				user1Ratings.add(table[user1][i]);
				user2Ratings.add(table[user2][i]);
			}
		}

		user1Mean = user1Mean / user1Count;
		user2Mean = user2Mean / user2Count;

		double numerator = 0.0;
		double denominator = 1.0;
		// compute sim
		for (int i = 0; i < user1Ratings.size(); i++) {
			numerator = numerator + (user1Ratings.get(i) - user1Mean)
					* (user2Ratings.get(i) - user2Mean);
		}

		double sum1 = 0.0;
		double sum2 = 0.0;

		for (int j = 0; j < user1Ratings.size(); j++) {
			sum1 = sum1 + Math.pow(user1Ratings.get(j) - user1Mean, 2);
			sum2 = sum2 + Math.pow(user2Ratings.get(j) - user2Mean, 2);
		}

		denominator = Math.sqrt(sum1) * Math.sqrt(sum2);

		if (denominator == 0) {
			sim = 0;
		} else {
			sim = numerator / denominator;
		}

		return sim;
	}
	
	public void writeToFile(HashMap<Integer, ArrayList<Item>> userMap,
			List<Integer> uniqueMoives, int[][] table)
			throws FileNotFoundException, UnsupportedEncodingException {

		PrintWriter writer = new PrintWriter("Pearson_results.csv", "UTF-8");
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
		HashMap userMap = new HashMap();
		DataParser parser = new DataParser();
		List<Integer> uniqueMoives = new ArrayList<Integer>();
		parser.parse(userMap, uniqueMoives);
		int[][] table = parser.toTable(userMap, uniqueMoives);

		int[][] prediction = new NewPearson().compute(table);
		new NewPearson().writeToFile(userMap, uniqueMoives, prediction);
		
	}

}
