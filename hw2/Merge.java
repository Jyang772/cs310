import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;
import java.util.Comparator;

public class Merge {
	
	public static void main(String [] args) {
	
		int[] arr = new int[args.length];
		for(int i=0; i<args.length; i++)
			arr[i] = Integer.parseInt(args[i]);
		
		ArrayList<Integer[]> intervals = new ArrayList<Integer[]>();
		
		{ int i=0;		
			while((i+1) < arr.length) {
				intervals.add(new Integer[]{arr[i],arr[i+1]});
				i+=2;
			}
		}	
	/*	for(Integer[] e: intervals){
			System.out.println("Interval: " + e[0] + " " + e[1]);
		}
	*/
	
		Stack<Integer[]> st = new Stack<Integer[]>();
		Integer[] top = new Integer[2];

		//Sort intervals by first value
		Collections.sort(intervals,new Comparator<Integer[]>() {
			public int compare(Integer[] a, Integer[] b) {
				return(Integer)(a[0]).compareTo(b[0]);
			}
		});
		
		st.push(intervals.get(0));
		
		for(int i=1; i<intervals.size(); i++) {
			top = st.peek();
			
			if(top[1] < intervals.get(i)[0])
				st.push(intervals.get(i));
			
			else if(top[1] < intervals.get(i)[1]) {
				top[1] = intervals.get(i)[1];
				st.pop();
				st.push(top);
			}
		}
		
		ArrayList<Integer[]>res = new ArrayList<Integer[]>();	
		res = new ArrayList<Integer[]>(st);	
	
		int i=0;	
		for(Integer[] a : res) {
			System.out.print("[" + a[0] + "," + a[1] + "]");
			if(i++ < res.size() - 1)
				System.out.print(",");
		}

	
		System.out.println();		
	}
}
