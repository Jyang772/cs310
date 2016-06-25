import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

public class Jump{

	public static void main(String [] args) {
	
	int[] arr = new int[args.length];
	for(int i=0; i<args.length; i++)
		arr[i] = Integer.parseInt(args[i]);
	

	//First set up a range(maximum jump length) for next jump
	//Search for values from current index to current range.
	//Look for a jump that will extend the range furthest

	
	ArrayList<Integer> res = new ArrayList<Integer>();
	
	int index = 0;
	int max = 0;	//Max range
	int range = 0;	//Max index reachable from index
	int remaining = 1;

	while(index + 1 < arr.length) {
		if(index + arr[index] > range) {
			range = index + arr[index];
			max = index;
			if(range >= arr.length - 1) {
				res.add(max);
				break;	
			}		
		}
		//Remaining elements in the 'range'
		if(--remaining == 0) {
			res.add(max);
			remaining = range - index;
		}
		index++;
	}
	//Add last index of array
	res.add(arr.length - 1);

	//Exclude starting index	
	System.out.println(res.size()-1);
	}
}
