package csc485project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Hybrid {

	public int[][] computePearsonFirst() throws IOException {

		//load the Pearson results file too save time
		int [][] predictedTable = new int [943][1682];
		BufferedReader br = new BufferedReader(new FileReader("Pearson_results.csv"));
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
	    
	    //then use Baseline
	    //predictedTable = new NewPearson().compute(predictedTable);
	    BaseLine baseline = new BaseLine();
		baseline.compute(predictedTable);

		return predictedTable;
	}
	
	public int[][] computeBaselineFirst() throws IOException {

		//load the baseline results file too save time
		int [][] predictedTable = new int [943][1682];
		BufferedReader br = new BufferedReader(new FileReader("baseline_results.csv"));
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
	    
	    //then use Pearson
	    predictedTable = new NewPearson().compute(predictedTable);

		return predictedTable;
	}
	
	public void writeToFile(HashMap<Integer, ArrayList<Item>> userMap,
			List<Integer> uniqueMoives, int[][] table)
			throws FileNotFoundException, UnsupportedEncodingException {

		PrintWriter writer = new PrintWriter("Hybrid_pearson_first.csv", "UTF-8");
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
		
		int[][] prediction = new Hybrid().computePearsonFirst();
		new Hybrid().writeToFile(userMap, uniqueMoives, prediction);
	}
}
