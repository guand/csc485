package csc485project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluation {

	// evaluation using RMSE, precision and recall. We categorize rating "1-3" as irrelevant and
	// "4-5" as relevant
	public void evaluate(String fileName ) throws IOException {
		
		int [][] predictedTable = new int [943][1682];
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = br.readLine();
		int lineNumber = 0;
	    while (line != null) {
	    	
	    	//Skip the first line
	    	if (lineNumber==0) {
	    		lineNumber++;
	    		line = br.readLine();
	    		continue;
	    	}
	    	String[] row = line.split(",\\s*");
	    	for (int i=1;i<row.length;i++) {
	    		predictedTable[lineNumber-1][i-1] = Integer.parseInt(row[i]);
	    	}
	    	lineNumber++;
	    	line = br.readLine();
	    }
	    br.close();
	    
	    int [][] originalTable = new int [943][1682];
		BufferedReader br2 = new BufferedReader(new FileReader("origian_data.csv"));
		String line2= br2.readLine();
		int lineNumber2 = 0;
	    while (line2 != null) {
	    	
	    	//Skip the first line
	    	if (lineNumber2==0) {
	    		lineNumber2++;
	    		line2 = br2.readLine();
	    		continue;
	    	}
	    	String[] row = line2.split(",\\s*");
	    	for (int i=1;i<row.length;i++) {
	    		originalTable[lineNumber2-1][i-1] = Integer.parseInt(row[i]);
	    	}
	    	lineNumber2++;
	    	line2 = br2.readLine();
	    }
	    br2.close();
	    
	    int TP = 0;
		int FP = 0;
		int TN = 0;
		int FN = 0;
		int count = 0;
		int sum = 0;
		for (int i = 0; i < originalTable.length; i++) {
			for (int j = 0; j < originalTable[0].length; j++) {
				if (originalTable[i][j] != 0) {
					
					//compute the error for RMSE
					int diff = predictedTable[i][j] - originalTable[i][j];
					diff = (int) Math.pow(diff, 2);
					sum = sum + diff;
					count++;
					
					// compute precision and recall
					// count number of relevant in ground truth table
					if (originalTable[i][j] > 3) {
						
						// Recommendation predicted the item as relevant as ground
						// truth
						if (predictedTable[i][j] > 3) {
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
						if (predictedTable[i][j] > 3) {
							FP++;
						}
						
						// Recommendation predicted the item as irrelevant as ground
						// truth
						else {
							TN++;
						}
						
					}
					
				}
			}
		}
		double mean = (double) sum/count;
		double RMSE = Math.sqrt(mean);
		double precision = (double) TP / (TP + FP);
		double recall = (double) TP / (TP + FN);
		double accurcy = (double) (TP + TN) / (TP + TN + FN + FP);

		System.out.println("precision: " + precision);
		System.out.println("recall: " + recall);
		System.out.println("accurcy: " + accurcy);
		System.out.println("RMSE: " + RMSE);
		
		
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
		System.out.println("BaseLine");
		eva.evaluate("baseline_results.csv");
		
		System.out.println("Pearson Results");
		eva.evaluate("Pearson_results.csv");
		
		System.out.println("Hybrid pearson first");
		eva.evaluate("Hybrid_pearson_first.csv");
	}
}
