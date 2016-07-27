import java.math.BigInteger;
public class Stairs {
	
	public static void main(String [] args) {

		//System.out.println(steps(Integer.parseInt(args[0])));
		//System.out.println(steps(Integer.parseInt(args[0]),BigInteger.ONE,BigInteger.valueOf(0)));        
		
		BigInteger fibonacci;
                BigInteger num = new BigInteger("1");
                BigInteger num2 = new BigInteger("1");
                for (int loop = 0; loop < Integer.parseInt(args[0]); loop++)
                {
                        fibonacci = num.add(num2);
                        num = num2;
                        num2 = fibonacci;
                }
                System.out.println(num);
	

	}

/*
	
	i: 0 1 2 3 4 5
	f: 1 1 2 3 5 8
	//Dynamic programming	
	//Calculate Nth fibonacci number from [0..N]
	public static BigInteger steps(int n) {
		BigInteger steps[] = new BigInteger[n+1];
		steps[0] = BigInteger.ONE;
		steps[1] = BigInteger.ONE;
		for(int i=2; i<=n; i++)
			steps[i] = steps[i-1].add(steps[i-2]);
		return steps[n];
	}
	
	//Tail recursion - https://en.wikipedia.org/wiki/Tail_call
	public static BigInteger steps(int n, BigInteger val, BigInteger prev) {
		if(n==0) return val;   //For [0..N]
		//if(n==1) return val; //For [1..N]
		return steps(n-1,val.add(prev),val);
	}
*/

	
}
