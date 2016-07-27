import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

public class BruteForce {

  final int min;
  final int max;
  final int sLength;

  private final int[] chars; 	//Hold current permutation of a set {}

  public BruteForce(char min, char max, int length) {
	this.min = min;
	this.max = max;
	this.sLength = length;
	
	chars = new int[sLength + 1];
	//Fill array of chars with the minimum value
	Arrays.fill(chars, 1, chars.length, min);
  }

  public void run() {
	while (chars[0] == 0) {
	check();
	increment();
	}
  }

  private void increment() {
	for (int i = chars.length - 1; i >= 0; i--) {
	if (chars[i] < max) {
	chars[i]++;
	return;			//\0aaa -> \0aab -> \0aba -> \0abb -> \0baa -> \0bab -> \0bba -> \0bbb -> aaaa
	}
	 
	chars[i] = min;		//Ex. \0aaa --> abbb when all have been changed to max, 
				//then the last(first) element will be changed to min, 
				//signalling the end of all permutations.

	}
  }

  private void check() {

	char[] b = new char[chars.length-1];
	for(int i=1; i<chars.length;i++)	//b[0] = \0
		b[i-1] = (char)chars[i];	
	byte[] a = getSha(new String(b));

	//Debug purposes: print out all permutations
	/*for(int i=0; i<chars.length; i++)
		System.out.print((char)chars[i]);
	System.out.println();
	*/
	
	
		
  }

  public static void main(String[] args) { 
	if(args.length < 1)
		System.out.println("Usage: ./java BruteForce 4 //generates password of length 4");
	//Bruteforce, time to bring out da big gunz
	new BruteForce('0','~', Integer.parseInt(args[0])).run();
  }



  public static byte[] getSha(String str){
	try{
		// part of java that gives us sha1
		MessageDigest m = MessageDigest.getInstance("SHA-1");

		// get the sha1
		byte[] sha1 = m.digest(str.getBytes("UTF-8"));

		//Check to see if we get a match
		//10, -39, 82
		if(sha1[0] == 10 && sha1[1] == -39 && sha1[2] == 82) {
			System.out.println(str);
			System.out.println(sha1(str));
		}


		// reduce complexity to only three bytes
		byte[] sha = new byte[]{sha1[0], sha1[1], sha1[2]};

		return sha;

	}catch(Exception e){
		return new byte[]{};
	}

        }

	//Cite JavaDocs 7 wikipedia
        public static String sha1(String input) {
        try {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
        }catch(Exception e){
                        return null;
        }


        }



}
