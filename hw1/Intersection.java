import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

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
	    int[] y = {5, 2, 9, 0, 1};
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

		ArrayList<Integer> dup = new ArrayList<Integer>();
	
		Arrays.sort(nums1);
		Arrays.sort(nums2);	
	
		int i=0,j=0;	//Improved! O(nlogn) runtime  
		int dp = Integer.MAX_VALUE;
		while(true) {	
			if((i >= nums1.length) || (j >= nums2.length)) {
				break;		
			}
			if(nums1[i] == nums2[j]) { //Take advantage of short-circuit evaluation
				if((i==0 || j==0) || ((nums1[i] != dp) && (nums2[j] != dp))) {
					//if((i==0 || j==0) || (nums1[i] != nums1[i-1]) || (nums2[j] != nums2[j-1]))
					dup.add(nums1[i]);
					dp = nums1[i];
				}
				i++;
				j++;
			}
			else if(nums1[i] > nums2[j])
				j++;
			else
				i++;
		}
				
		/*      First version, slightly better than O(n^2), still required peeking back one to check for duplicate :(
                int k=0;
                for(int i=0; i<nums1.length; i++) {             //Check for duplicates in first array
                        if((i==0) || (nums1[i] != nums1[i-1]))  //Take advantage of short-ciruit evaluation
                                for(int j=k; j<nums2.length; j++) {
                                        if(nums1[i] == nums2[j]){
                                                if((j==0 || i==0) || ((nums2[j-1] != nums2[j])))  //Check for duplicates in result
                                                        dup.add(nums1[i]);
                                                k=j+1;          //Don't check [j] < current     
                                        }
                                }
                }
*/  

		//Convert ArrayList<Integer> to int[]
		Iterator<Integer> iterator = dup.iterator();
		int[] ret = new int[dup.size()];
    		for (i = 0; i < ret.length; i++)
    		{
        		ret[i] = iterator.next().intValue();
    		}
		return ret;
	}
}
