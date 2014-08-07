package csc485project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Hybrid {

	public int[][] compute(int[][] tablePredicted, int[][] table) {

		tablePredicted = new NewPearson().compute(tablePredicted);
		BaseLine baseline = new BaseLine();
		baseline.compute(tablePredicted);

		// recover the ground truth
		for (int i = 0; i < tablePredicted.length; i++) {
			for (int j = 0; j < tablePredicted[i].length; j++) {
				if (table[i][j] != 0) {
					tablePredicted[i][j] = table[i][j];
				}
			}
		}

		return tablePredicted;
	}

	public static void main(String[] args) throws IOException {
	}
}
