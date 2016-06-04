import java.util.Arrays;
import java.util.ArrayList;

public class Intersection {

	public static void main(String[] args) {
	    {
	    int[] x = {1, 2, 2, 1};
	    System.out.println("x = " + Arrays.toString(x));
	    int[] y = {2, 2};
	    System.out.println("y = " + Arrays.toString(y));
	    int[] z = intersection(x, y);
	    System.out.println("intersection(x, y) = " + Arrays.toString(z));

	    System.out.println(Arrays.equals(z, new int[]{2}));
	    }

	    {
	    int[] x = {2, 5, 3, 7};
	    System.out.println("x = " + Arrays.toString(x));
	    int[] y = {77,86,79,61,31,80,74,83,19,66,97,60,1,84,91,3,25,38,44,99,78,49,95,7,96,87,30,33,13,65,94,37,35,68,8,62,56,89,10,53,59,16,54,88,67,23,5,70,46,43,9,51,47,28,90,36,12,81,75,71,40,26,73,76,72,92,64,32,41,24,42,17,6,85,93,50,21,20,48,98,63,18,27,58,82,4,69,14,57,22,29,100,55,52,2,39,45,15,34,11};//{5, 2, 9, 0, 1};
	    System.out.println("y = " + Arrays.toString(y));
	    int[] z = intersection(x, y);
	    System.out.println("intersection(x, y) = " + Arrays.toString(z));
	    
	    Arrays.sort(z);
	    int [] correct = new int[]{2,5};
	    Arrays.sort(correct);
	    System.out.println(Arrays.equals(z, correct));
	    }
	}

	static int[] intersection(int[] nums1, int[] nums2) {

		ArrayList<Integer> result = new ArrayList<Integer>();
	
		Arrays.sort(nums1);
		Arrays.sort(nums2);	
		System.out.println("AAAAAAAAAAA");
		System.out.println(Arrays.toString(nums1));
		System.out.println(Arrays.toString(nums2));
		int k=0;
		for(int i=0; i<nums1.length; i++) {

			if((i==0) || (nums1[i] != nums1[i-1]))
			for(int j=k; j<nums2.length; j++) {
				if(nums1[i] == nums2[j]){
					if((j==0 || i==0) || ((nums2[j-1] != nums2[j]) && (nums1[i-1] != nums1[i]))){
						result.add(nums1[i]);
					}
					k=j+1;
				}
			}
		}
		System.out.println(result);
		return nums1;
		//return result.toArray(new int[result.size()]);
	}
}
