import java.util.PriorityQueue;
public class KthLargestElement {

	//Algorithm for O(nlogk)
	//Insert first k elements into a minimum heap
	//Iterate through array from k-n elements
	//If ith element is greater than root of minHeap,
	//replace root with ith element
	

	public static void main(String [] args) {
	
		int k = Integer.parseInt(args[0]);
		int [] arr = new int[args.length-1];	
		
		for(int i=1; i<args.length; i++)
			arr[i-1] = Integer.parseInt(args[i]);

		//From Javadocs
		//this implementation provides O(log(n)) time for the enqueing and dequeing methods	
		PriorityQueue<Integer> kmax = new PriorityQueue<Integer>();

		//Insert first k elements into minHeap O(klogk)
		//Then insert/replace root with (n-k) elements if ith element is greater than min of heap O((n-k)logk)
		//Minheap never grows bigger than k
		//Depth k	
		//O(klogk) - initial k elements inserted into min-heap
		//O((n-k)logk) - remaining elements inserted into min-heap
		//O(klogk) + O((n-k)logk) == O((k+n-k)logk) == O(nlogk)
		
		for(int i=0; i<k; i++)	
			kmax.add(arr[i]);	

		for(int i=k; i<arr.length; i++) {
			if(arr[i] >= kmax.peek()) {
				kmax.poll();
				kmax.add(arr[i]);
			}
		}
		
		System.out.println(kmax.poll());
    }	

}
