import java.util.Arrays;

public class FindSingle {

	//Algorithm: Use binary search, to partition the array and search
	//recursively.
	//O(logn) because binary search
	//We are repeatedly dividing a partition by two, until we get 1.
	//n / 2^i <= 1
	// n <= 2^i
	//log_base 2 of n <= i

	//If mid is odd, and mid == mid-1, then search to the right
	//
	public static void main(String [] args) throws Exception{

	if(args.length > 0) {	
		int[] arr = new int[args.length];
	
		for(int i=0; i<args.length; i++)
			arr[i] = Integer.parseInt(args[i]);

		System.out.println(search(arr,0,args.length-1));
		//System.out.println(single);
	}else{
		
		{
		int [] in = new int[]{1,1,2,2,3};
		System.out.println(Arrays.toString(in) + "\n" + search(in));
		}
		
		{
		int [] in = new int[]{1,1,2,3,3};
		System.out.println(Arrays.toString(in) + "\n" + search(in));
		}
		
		{
		int [] in = new int[]{1, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8 };
		System.out.println(Arrays.toString(in) + "\n" + search(in));
		}
		
		{
		int [] in = new int[]{10, 10, 17, 17, 18, 18, 19, 19, 21, 21, 23};
		System.out.println(Arrays.toString(in) + "\n" + search(in));
		}
		
		{
		int [] in = new int[]{1, 3, 3, 5, 5, 7, 7, 8, 8, 9, 9, 10, 10};
		System.out.println(Arrays.toString(in) + "\n" + search(in));
		}
	}
	
	}
	
	static int search(int[] a) throws Exception{
		// Some sample lines. Feel free to remove them.
		if (a.length == 1) return a[0];
		if (a.length == 2) throw new Exception("No single");
		//Recursive function below		
		return search(a,0,a.length-1);
	}

	
	public static int search(int[] arr, int low, int high) {

		if(low > high)
			return -1;
		if(low == high) {
			//System.out.println(arr[low]);	
			return arr[low];	 //Found!
		}
		
		int mid = (low + high) / 2;
		
		//If mid is even, and [mid] == [mid+1], then search right
		//1,1,2,2,3,4,4,5,5		

		if(mid % 2 == 0) {
			if(arr[mid] == arr[mid+1]) {  //Search right
				//System.out.println(arr[mid+2]);
				return search(arr,mid+1,high);
			}
			else
				return search(arr,low,mid); //Search left
		}
		//1,2,3,3,4,4,5,5,7,7 - search left
		else {
			if(arr[mid] == arr[mid+1])
				return search(arr,low,mid);
			else
				return search(arr,mid+1,high);
		}
	}

}
