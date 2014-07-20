package csc485project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseLine {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void compute(HashMap data) {
		
		List<Integer> uniqueMoives = new ArrayList<Integer>();
		int sum = 0;
		int ratingCount = 0;		
		Iterator iterator = data.entrySet().iterator();
		
		//Loop through all the users
		while (iterator.hasNext()) {
	        Map.Entry pairs = (Map.Entry)iterator.next();
	        ArrayList<Item> itemList = (ArrayList<Item>) pairs.getValue();
	        
	        //Loop through the movie that is watched by the current user
	        for(Item item: itemList){
	        	sum = sum + item.getRating();
	        	ratingCount++;
	        	
	        	//Keep track of all the unique movies
	        	Integer currentItem = item.getmId();
	        	if(!uniqueMoives.contains(currentItem)) {
	        		uniqueMoives.add(currentItem);
	        	}
	        }
	    }
		
		int mean = Math.round(sum/ratingCount);
		
		//need a loop here for all the u and i
		double bu = 0.0;
		double bi = 0.0;
		double previousBu = 0.0;
		double previousBi = 0.0;
		double r = 0.005;
		double l1 = 0.02;
		double l2 = 0.02;
		int predicted = 0;
		ArrayList<Item> itemList = (ArrayList<Item>) data.get(196);
		int currentValue = itemList.get(1).getRating();
		
		while (true) {
			predicted = (int) Math.round(mean+bu+bi);
			previousBu = bu;
			bu = bu + r*(currentValue-predicted-l1*bu);
			
			previousBi = bi;
			bi = bi + r*(currentValue-predicted-l2*bi);
			
			//will loop about 52 times
			if ((bu-previousBu)<0.0001) {
				break;
			}
		}
		System.out.println("Bi = " + bi);
		System.out.println("Bu = " + bu);
		System.out.println("predicted = " + predicted);

		
		
	}
	
	public static void main(String [] args) throws IOException {
		DataParser parser = new DataParser();
		HashMap userMap = new HashMap ();
		parser.parse(userMap);
		
		BaseLine baseline = new BaseLine();
		baseline.compute(userMap);
	}

}
