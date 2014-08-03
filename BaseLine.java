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
	
	public void compute(HashMap<Integer,ArrayList<Item>> data, int [][] table) {
		
		int sum = 0;
		int ratingCount = 0;		
		Iterator<Entry<Integer, ArrayList<Item>>> iterator = data.entrySet().iterator();
		
		//Loop through all the users
		while (iterator.hasNext()) {
	        Map.Entry<Integer,ArrayList<Item>> pairs = (Map.Entry<Integer,ArrayList<Item>>)iterator.next();
	        ArrayList<Item> itemList = (ArrayList<Item>) pairs.getValue();
	        
	        //Loop through the movie that is watched by the current user
	        for(Item item: itemList){
	        	sum = sum + item.getRating();
	        	ratingCount++;
	        }
	    }
		
		int mean = Math.round(sum/ratingCount);
		System.out.println("Mean = " + mean);
		for (int i=0;i<table.length; i++) {
			for (int j=0;j<table[0].length; j++) {
				
				//need a loop here for all the u and i
				double bu = 0.0;
				double bi = 0.0;
				double previousBu = 0.0;
				//double previousBi = 0.0;
				double r = 0.005;
				double l1 = 0.02;
				double l2 = 0.02;
				int predicted = 0;
				int currentValue = table[i][j];
				
				while (true) {
					predicted = (int) Math.round(mean+bu+bi);
					previousBu = bu;
					bu = bu + r*(currentValue-predicted-l1*bu);
					
					//previousBi = bi;
					bi = bi + r*(currentValue-predicted-l2*bi);

					//will loop about 52 times
					if ((bu-previousBu)<0.000000001) {
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
	
	public void writeToFile(HashMap<Integer, ArrayList<Item>> userMap, List<Integer> uniqueMoives, int [][] table) throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter("baseline_results.csv", "UTF-8");
		writer.print(" ,");
		//save the movie id in file
		Iterator<Integer> movieIterator = uniqueMoives.iterator();
		while(movieIterator.hasNext()) {
			int movieId = movieIterator.next();
			writer.print(movieId+", ");
		}
		writer.println();
		
		Iterator<Integer> iterator = userMap.keySet().iterator();
		
		
		for (int i=0;i<table.length; i++) {
			int userId = iterator.next();
			writer.print(userId+", ");
			for (int j=0;j<table[0].length; j++) {
				writer.print(table[i][j]+", ");
			}
			writer.println();
		}
		
		writer.close();
		
	}
	
	public static void main(String [] args) throws IOException {
		DataParser parser = new DataParser();
		HashMap<Integer,ArrayList<Item>> userMap = new HashMap<Integer,ArrayList<Item>> ();
		List<Integer> uniqueMoives = new ArrayList<Integer>();
		parser.parse(userMap,uniqueMoives);
		int [][] table = parser.toTable(userMap,uniqueMoives);
		
		BaseLine baseline = new BaseLine();
		baseline.compute(userMap,table);
		baseline.writeToFile(userMap,uniqueMoives,table);
		
	}

}
