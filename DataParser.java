package csc485project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataParser {
	
	
	public void parse (HashMap<Integer, ArrayList<Item>> userMap) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("u.data"));
	    String line = br.readLine();
	    while (line != null) {
	    	
			Item movie = new Item();
	    	String[] userMovieArray = line.split("\\s");
	    	
	    	movie.setmId(Integer.parseInt(userMovieArray[1]));
	    	movie.setRating(Integer.parseInt(userMovieArray[2]));
	    	
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
        
	
	public static void main(String [] args) throws IOException {
            HashMap userMap = new HashMap ();
            DataParser parser = new DataParser();
            parser.parse(userMap);
            System.out.println();
	}
}
