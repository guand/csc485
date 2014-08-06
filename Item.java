package csc485project;

public class Item {
	
	private int mId;
	private int rating;
        
        public Item(){}
        
        public Item(int mId, int rating){
            this.mId = mId;
            this.rating = rating;
        }
	
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
