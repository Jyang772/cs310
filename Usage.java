
public class Usage{
	private String user;
	private int count;
	
	public Usage(String user,int count) {
		this.user = user;
		this.count = count;
	}

	public void add() {
		count++;
	}
	
	public String getUser() {
		return user;
	}
	
	public int getCount() {
		return count;
	}
	
	public String toString() {
		return this.user + ", " + this.count;
	}
}
