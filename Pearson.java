/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csc485project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Danny
 */
public class Pearson {
    
    public double pearson_correlation(ArrayList<Item> selected, ArrayList<Item> comparison){
        double sum_sqr_x = 0;
        double sum_sqr_y = 0;
        double sum_co_prod = 0;
        double mean_x = 0;
        double mean_y = 0;
        double movie_no = 0;
        for(Item rating: selected){
            for(Item c_rating: comparison){
                if(rating.getmId() == c_rating.getmId()){
                    sum_sqr_x += rating.getRating() * rating.getRating();
                    sum_sqr_y += c_rating.getRating() * c_rating.getRating();
                    mean_x += rating.getRating();
                    mean_y += c_rating.getRating();
                    sum_co_prod += rating.getRating() * c_rating.getRating();
                    movie_no++;
                }
            }
        }

        mean_x = mean_x / movie_no;
        mean_y = mean_y / movie_no;
        double pop_sd_x = Math.sqrt((sum_sqr_x/movie_no) - (mean_x * mean_x));
        double pop_sd_y = Math.sqrt((sum_sqr_y/movie_no) - (mean_y * mean_y));
        double cov_x_y = (sum_co_prod / movie_no) - (mean_x * mean_y);
        double correlation = cov_x_y / (pop_sd_x * pop_sd_y);
        if(Double.isNaN(correlation)){
            correlation = 0.0;
        } else if(correlation == Double.POSITIVE_INFINITY){
            correlation = 0.0;
        }
        return correlation;
    }
    
    public void pearson_complete(ArrayList<Item> selected, int hashkey, Map<Integer, ArrayList<Item>> userMap, Map<Integer, ArrayList<Item>> newUserMap, int ratingContain){
          double mrating = 0.0;
          double similarity_denom = 0.0;
          double similarity_num = 0.0;
          double similarity_calc = 0.0;
          double srating = 0.0;
          double msrating = 0.0;
          double frating = 0.0;
          int iFrating = 0;
          int counter = 0;
          for(Item rating : selected){
              srating += rating.getRating();
              counter++;
          }
          msrating = srating/counter;
          ArrayList<PersonSimilarity> similarityList = new ArrayList<PersonSimilarity>();
          Iterator it = userMap.entrySet().iterator();
          double pearson_correlation = 0.0;
          double mod_rating = 0.0;
          boolean flag = false;
          while (it.hasNext()){
              Map.Entry pairs = (Map.Entry)it.next();
              if(pairs.getKey() != hashkey){
                  flag = false;
                  for(Item rating : (ArrayList<Item>)pairs.getValue()){
                      if(ratingContain == rating.getmId()){
                          flag = true;
                      }
                  }
                  if(flag){
                    pearson_correlation = pearson_correlation(selected, (ArrayList<Item>)pairs.getValue());
                    mod_rating = pearson_calculate(selected, (ArrayList<Item>)pairs.getValue(), ratingContain);
//                    mod_rating = 0.0;
                    PersonSimilarity similarity = new PersonSimilarity((int)pairs.getKey(), pearson_correlation, mod_rating);
                    similarityList.add(similarity);
                  }
              }
          }
//         Collections.sort(similarityList, Collections.reverseOrder());
         for(PersonSimilarity stuff : similarityList){
//             System.out.println(stuff.getmId() + " " + stuff.getSimilarity()+ " " + stuff.getModRating());
             if(stuff.getSimilarity() < 0)
                similarity_denom += (-1)*stuff.getSimilarity();
             else
                 similarity_denom += stuff.getSimilarity();
             similarity_num += stuff.getModRating();
         }
         similarity_calc = (similarity_num/similarity_denom);
         if(Double.isNaN( similarity_calc)){
             similarity_calc = 0.0;
        } else if( similarity_calc == Double.POSITIVE_INFINITY){
             similarity_calc = 0.0;
        }
         frating = msrating + similarity_calc;
         iFrating = (int)Math.round(frating);
         if(iFrating > 5)
             iFrating = 5;
         Item movie = new Item(ratingContain, iFrating);
         ArrayList<Item> newItem = newUserMap.get(hashkey);
         newItem.add(movie);
    }
    
    
    
    public ArrayList<Integer> pearson_find(ArrayList<Item> selected, Map<Integer, ArrayList<Item>> userMap){
        ArrayList<Integer> possibleRecommendation = new ArrayList<Integer>();
        boolean not_here = false;
        Iterator it = userMap.entrySet().iterator();
          while (it.hasNext()){
              Map.Entry pairs = (Map.Entry)it.next();
              for(Item m_rating : (ArrayList<Item>)pairs.getValue()){
                  not_here = false;
                  for( Item rating : selected) {
                      if(rating.getmId() == m_rating.getmId()){
                          not_here = false;
                          break;
                      }
                      not_here = true;
                  }
                  if(not_here){
                      if(!possibleRecommendation.contains(m_rating.getmId())){
                          possibleRecommendation.add(m_rating.getmId());
                      }
                  }
              }
          }
          return possibleRecommendation;
    }
    
    public void pearson_findone(ArrayList<Item> selected, ArrayList<Item> compare){
        boolean not_here = false;
        for(Item m_rating : compare){
            for(Item rating : selected){
                if(rating.getmId() == m_rating.getmId()){
                          not_here = false;
                          break;
                }
                not_here = true;
            }
            if(not_here){
//                if(!possibleRecommendation.contains(m_rating.getmId())){
//                    possibleRecommendation.add(m_rating.getmId());
//                }
                System.out.println(m_rating.getmId());
            }
        }
    }
    
    public double pearson_calculate(ArrayList<Item> selected, ArrayList<Item> compare, int ratingItem){
        double pearson_correlation = pearson_correlation(selected, compare);
        double m_rating = 0.0;
        double avg_rating = 0.0;
        double s_rating = 0.0;
        for(Item rating : compare){
            if(rating.getmId() == ratingItem){
                s_rating = rating.getRating();
            }
        }
        double modified_rating = 0;
        int counter = 0;
        for(Item rating : compare){
            if(rating.getmId() != ratingItem){
                m_rating += rating.getRating();
                counter++;
            }
        }
        avg_rating = m_rating/counter;
        modified_rating = (s_rating - avg_rating)*pearson_correlation;
        return modified_rating;
    }
    
    public static void main(String [] args) throws IOException {
//        int numOfUsers = 1000;
//        int numOfMovies = 1700;
        Map userMap = new HashMap ();
        Map newUserMap = new HashMap ();
        DataParser parser = new DataParser();
        List<Integer> uniqueMoives = new ArrayList<Integer>();
        ArrayList<Integer> possibleRecommendation = new ArrayList<Integer>();
        parser.parse(userMap,uniqueMoives);
        parser.parse(newUserMap, uniqueMoives);
        
        Pearson pearson = new Pearson();
        ArrayList<PersonSimilarity> similarityList = new ArrayList<PersonSimilarity>();
        Iterator it = userMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pairs = (Map.Entry)it.next();
            possibleRecommendation = pearson.pearson_find((ArrayList<Item>)pairs.getValue(),userMap);
            for(Integer stuff : possibleRecommendation){
                pearson.pearson_complete((ArrayList<Item>)pairs.getValue(), (int)pairs.getKey(), userMap, newUserMap, stuff);
            }
        }
        
       int[][] table = parser.toTable(newUserMap, uniqueMoives);
       for(int i = 0; i < table.length; i++){
           for(int j = 0; j <table[i].length; j++){
               System.out.print(table[i][j] + " ");
           }
           System.out.println();
       }
    }
}
