import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;
import java.util.PriorityQueue;

public class IntervalPartition {
	
	public static class Rooms implements Comparable<Rooms> {
		int last;
		List<Integer[]> classes;
		
		public Rooms(List<Integer[]> classes, int key) {
			this.classes = classes;
			this.last = key;
		}		
		
		public int compareTo(Rooms other) {
			return (this.last - other.last);
		}
	}

	
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
		//Sort intervals by first value
		Collections.sort(intervals,new Comparator<Integer[]>() {
			public int compare(Integer[] a, Integer[] b) {
				return(Integer)(a[0]).compareTo(b[0]);
			}
		});

		
	
		ArrayList<List<Integer[]>> rooms = new ArrayList<List<Integer[]>>();
		rooms.add(new ArrayList<Integer[]>());
		
		rooms.get(0).add(intervals.get(0));
		
		PriorityQueue<Rooms> q = new PriorityQueue<Rooms>();
		
		q.add(new Rooms(rooms.get(0),intervals.get(0)[1]));
		
		int d = 0;
		Rooms temp;	
		for(int i=1; i<intervals.size(); i++) {
			temp = q.peek();
			if(intervals.get(i)[0] >= temp.last){
				q.poll();	
				temp.classes.add(intervals.get(i));
				temp.last = intervals.get(i)[1];
				q.add(new Rooms(temp.classes,temp.last));
			}	
			else {
				d++;
				rooms.add(new ArrayList<Integer[]>());
				rooms.get(d).add(intervals.get(i));
				q.add(new Rooms(rooms.get(d),intervals.get(i)[1]));
			}
		}		



		//Pretty print deez elements 		
		Stack<Rooms> st = new Stack<Rooms>();	
	
		int i=1,s=0;	
		int size;
		while(!q.isEmpty())
			st.push(q.poll());
		while(!st.isEmpty()) {
		System.out.print("Room " + i + ": ");
		size = st.peek().classes.size();
		s=0;
		for(Integer[] b : st.pop().classes) {
               		System.out.print("[" + b[0] + "," + b[1] + "]");
			if(s++ < size-1)
				System.out.print(",");
			}
			 
		i++; System.out.println();
		}
	}
}


