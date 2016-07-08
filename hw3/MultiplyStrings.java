import java.util.Arrays;

public class MultiplyStrings {

	public static void main(String [] args) {
		
		char[] num1 = args[0].toCharArray();
		char[] num2 = args[1].toCharArray();
		int size = num1.length + num2.length;
		
		int []r = new int[size];
		
		int j = num1.length - 1;
		int i = num2.length - 1;
		int k = r.length - 1;

		if((i == 0 || j == 0) && ((num1[i]-'0' == 0) || (num2[j]-'0' == 0))) {
			System.out.println(0);
			return;
		}

		//Textbook multiplication method, not optimal (n^2)
		//Multiply two numbers as taught in school
		// 123
		//x 49
		//----
		//  49
		//x123
		//----

		//Warning: I write Java like I write C ...lulz		
		int tmp = 0;
		int carry = 0; //carry value
		int left = 0; //move to left
		while(i >= 0) {
			k = r.length - 1 - left++;
			j = num1.length - 1;
			
			while(j >= 0 || carry > 0) {
				if(j >= 0)
					tmp = (num1[j]-'0') * (num2[i]-'0');
				else
					tmp = 0;
				tmp += carry;
				carry = tmp/10;
				r[k] += tmp % 10; //Add to previous sum
				carry += r[k]/10; //Add carry from prev. sum
				r[k] = r[k] % 10;
				j--;
				k--;
			}
		i--;
		}

	
	//Pretty print deez elements
	//Must ignore leading zeros, otherwise use more dynamic data structure
	i = 0;
	int z = 0;
	while(r[i++] == 0)
		z++;
	r = Arrays.copyOfRange(r,z,r.length);

	for(int d : r)
		System.out.print(d);
	System.out.println();


	}		
}

