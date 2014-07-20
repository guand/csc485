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
        
        public double pearson_correlation(ArrayList<Item> selected, ArrayList<Item> comparison, int N){
            double sum_sqr_x = 0;
            double sum_sqr_y = 0;
            double sum_co_prod = 0;
            double mean_x = 0;
            double mean_y = 0;
            for(Item rating: selected){
                sum_sqr_x += rating.getRating() * rating.getRating();
                mean_x += rating.getRating();
                for(Item c_rating: comparison){
                    if(rating.getmId() == c_rating.getmId()){
                        sum_co_prod += rating.getRating() * c_rating.getRating();
                    }
                }
            }
            for(Item comparison_rating: comparison){
                sum_sqr_y += comparison_rating.getRating() * comparison_rating.getRating();
                mean_y += comparison_rating.getRating();
            }
            mean_x = mean_x / N;
            mean_y = mean_y / N;
            double pop_sd_x = Math.sqrt((sum_sqr_x/N) - (mean_x * mean_x));
            double pop_sd_y = Math.sqrt((sum_sqr_y/N) - (mean_y * mean_y));
            double cov_x_y = (sum_co_prod / N) - (mean_x * mean_y);
            double correlation = cov_x_y / (pop_sd_x * pop_sd_y);
            return correlation;
        }
        
        public void pearson_recommendation(){
            
        }
        
	
	public static void main(String [] args) throws IOException {
            int numOfUsers = 1000;
            int numOfMovies = 1700;
            HashMap userMap = new HashMap ();
            DataParser parser = new DataParser();
            parser.parse(userMap);
            double cor = parser.pearson_correlation((ArrayList<Item>)userMap.get(197), (ArrayList<Item>)userMap.get(184), numOfMovies);
            System.out.println(cor);
            System.out.println();
	}
}
