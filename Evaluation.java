package csc485project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Evaluation {

	// 10 fold
	public int[][] folding(int[][] table) {
		int totalElement = table.length*table[0].length;
		int foldSize = totalElement/10;
		int lastFoldSize = totalElement % 10;
		for (int foldCount = 0; foldCount<10; foldCount++) {
			while(true) {
				
			}
		}
		
		return table;
	}

	public void computeRMSR(int[][] table) {

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
