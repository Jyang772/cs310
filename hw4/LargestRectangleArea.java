import java.util.Stack;
public class LargestRectangleArea {
	public static void main(String [] args) {

	int[] hist = new int[args.length];
	for(int i=0; i<args.length; i++)
		hist[i] = Integer.parseInt(args[i]);		

	/*
	
	index:	   0	 1     2		
		|-----|-----|-----|
	i:	0     1     2     3	
		Left index of rect. = i - stack.top - 1;
		Right index of rect = i;
	*/

	Stack<Integer> stack = new Stack<Integer>();
	int i=0;
	int max = 0;
	int top = 0;
	int li = 0, ri = 0, area = 0;
	while(i < hist.length) {
		//short-circuit evaluation
		if(stack.isEmpty() || hist[stack.peek()] <= hist[i])
			stack.push(i++);
		else {
			top = stack.pop();
			//For Debug:
			//Check to see if the max area needs to be updated
			//If stack is empty, multiply hist[top] * i
			//Else hist[top] * (i - stack.peek() - 1)
			max = Math.max(max, hist[top] * (stack.isEmpty() ? i : (i - stack.peek() - 1)));
			
		}
	}

	//Must go through remaining hist[i]. 
	//Ex 5,4,5. 
	//
	while(!stack.isEmpty()) {
		top = stack.pop();	
	 	//For Debug:
		max = Math.max(max, hist[top] * (stack.isEmpty() ? i : (i - stack.peek() - 1)));
		
	}
	
	System.out.println(max);
	}	
}
