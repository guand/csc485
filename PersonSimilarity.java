/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csc485project;

import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Danny
 */
public class PersonSimilarity implements Comparable<PersonSimilarity> {
    private int mId;
    private double similarity;
    private double modRating;
    
    
    public PersonSimilarity(int mId, double similarity, double modRating){
        this.mId = mId;
        this.similarity = similarity;
        this.modRating = modRating;
    }
    
    public int getmId(){
        return mId;
    }
    
    public void setmId(int mId){
        this.mId = mId;
    }
    
    public double getSimilarity(){
        return similarity;
    }
    
    public void setSimilarity(double similarity){
        this.similarity = similarity;
    }
    
    public double getModRating(){
        return modRating;
    }
    
    public void setModRating(double modRating){
        this.modRating = modRating;
    }

    @Override
    public int compareTo(PersonSimilarity o) {
        return new Double(similarity).compareTo(o.similarity);
    }
     @Override
    public String toString() {
        return String.valueOf(similarity);
    }
}
