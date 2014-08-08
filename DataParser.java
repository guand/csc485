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

public class DataParser {
	
	
	public void parse (Map<Integer, ArrayList<Item>> userMap,List<Integer> uniqueMoives) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("u.data"));
	    String line = br.readLine();
	    while (line != null) {
	    	
			Item movie = new Item();
	    	String[] userMovieArray = line.split("\\s");
	    	
	    	int currentMovie = Integer.parseInt(userMovieArray[1]);
	    	movie.setmId(currentMovie);
	    	movie.setRating(Integer.parseInt(userMovieArray[2]));
			
			if (!uniqueMoives.contains(currentMovie)) {
				uniqueMoives.add(currentMovie);
			}
	    	
	    	int userId = Integer.parseInt(userMovieArray[0]);
	    	if (!userMap.containsKey(userId)) {
	    		ArrayList<Item> itemList = new ArrayList<Item>();
	    		itemList.add(movie);
	    		userMap.put(userId, itemList);
	    	} else {
	    		ArrayList<Item> itemList = userMap.get(userId);
	    		itemList.add(movie);
	    	}
	    	
	    	line = br.readLine();
	    	
	    }
	    br.close();
	}
	
	public int[][] toTable(Map<Integer, ArrayList<Item>> userMap, List<Integer> uniqueMoives) {
		

		//create a new table, and initialize the elements to zero
		int [][] table = new int[userMap.keySet().size()][uniqueMoives.size()];
		for (int i=0;i<userMap.keySet().size();i++) {
			for (int j=0;j<uniqueMoives.size();j++) {
				
				table[i][j] = 0;
			}
		}
		int userIndex = 0;
		int itemIndex = 0;
		Iterator<Entry<Integer, ArrayList<Item>>> iterator = userMap.entrySet().iterator();
		// Loop through all the users
		while (iterator.hasNext()) {
			
			Map.Entry<Integer, ArrayList<Item>> pairs = (Map.Entry<Integer, ArrayList<Item>>) iterator.next();
			ArrayList<Item> itemList = (ArrayList<Item>) pairs.getValue();
			// Loop through the movie that is watched by the current user
			for (Item item : itemList) {
				itemIndex = uniqueMoives.indexOf(item.getmId());
				table[userIndex][itemIndex] = item.getRating();
			}
			userIndex++;
		}
		return table;

	}
	
	public Map<Integer, ArrayList<Item>> toMap(int[][] table, List<Integer> uniqueMoives) {
		Map<Integer, ArrayList<Item>> userMap = new HashMap<Integer, ArrayList<Item>>();
		
		for (int i=0; i< table.length;i++) {
			ArrayList<Item> itemList = new ArrayList<Item>();
			for (int j=0; j<table[0].length;j++) {
				if (table[i][j] != 0) {
					Item movie = new Item();
					movie.setmId(uniqueMoives.get(j));
					movie.setRating(table[i][j]);
		    		itemList.add(movie);
				}
			}
			userMap.put(i, itemList);
		}
		return userMap;
		
	}
	
	public void writeToFile(HashMap<Integer, ArrayList<Item>> userMap,
			List<Integer> uniqueMoives, int[][] table)
			throws FileNotFoundException, UnsupportedEncodingException {

		PrintWriter writer = new PrintWriter("origian_data.csv", "UTF-8");
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
        
	
	public static void main(String [] args) throws IOException {
            HashMap userMap = new HashMap ();
            DataParser parser = new DataParser();
			List<Integer> uniqueMoives = new ArrayList<Integer>();
            parser.parse(userMap,uniqueMoives);
            int[][] table = parser.toTable(userMap, uniqueMoives);
            parser.writeToFile(userMap, uniqueMoives, table);
            
            
            
            
            
	}
}
