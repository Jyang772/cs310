import java.math.BigInteger;
public class StairsSlow {

	public static void main(String [] args) {
		
		BigInteger n = new BigInteger(args[0]);
		System.out.println(steps(n));	
	
	}

	public static BigInteger steps(BigInteger n) {
		if((n.compareTo(BigInteger.valueOf(0)) == 0) || n.compareTo(BigInteger.ONE) == 0)
			return BigInteger.ONE;
		return steps(n.subtract(BigInteger.ONE)).add(steps(n.subtract(BigInteger.valueOf(2)))); 
	}
}
