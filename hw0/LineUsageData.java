
public class LineUsageData {
	private SinglyLinkedList<Usage> list;
	
	public LineUsageData() {
		list = new SinglyLinkedList<Usage>();
	}

	public void addObservation(String username) {
	
		for(int i=0; i<list.size();i++) {
			if(username.equals(list.get(i).getUser())) {
				list.get(i).add();
				return;
			}
		}	
		
		list.add(new Usage(username, 1));
	}
	
	public Usage findMaxUsage() {
		Usage max = new Usage(null, 0);
		
		for(int i=0; i<list.size(); i++)
			if(list.get(i).getCount() > max.getCount())
				max = list.get(i);
		return max;
	}

	public int size() {
		return list.size();
	}
}
