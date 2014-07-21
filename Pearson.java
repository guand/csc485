/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csc485project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Danny
 */
public class Pearson {
    
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
    
    public static void main(String [] args) throws IOException {
        int numOfUsers = 1000;
        int numOfMovies = 1700;
        HashMap userMap = new HashMap ();
        DataParser parser = new DataParser();
        parser.parse(userMap);
        Pearson pearson = new Pearson();
        double cor = pearson.pearson_correlation((ArrayList<Item>)userMap.get(197), (ArrayList<Item>)userMap.get(184), numOfMovies);
        System.out.println(cor);
    }
}
