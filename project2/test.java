import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;

public class test {

	public static void main(String [] args) {


	System.out.println(Arrays.toString(getSha(args[0])).replace(" ",""));

//	System.out.println(sha1(args[0]));	
	//	System.out.println(Arrays.toString(getSha(file2String("secret"))).replace(" ",""));
	}

	public static String file2String(String filename) {
		
		try {
			File file = new File(filename);
		    FileInputStream fis = new FileInputStream(file);
		    byte[] data = new byte[(int)file.length()];
		    fis.read(data);
		    fis.close();
		    return new String(data);
		} catch (Exception e) {
			return new String("File read error");
		}
		
	}

	public static byte[] getSha(String str){
		
		try{
			// part of java that gives us sha1
			MessageDigest m = MessageDigest.getInstance("SHA-1");
			
			// get the sha1
			byte[] sha1 = m.digest(str.getBytes("UTF-8"));
	
			// reduce complexity to only three bytes
				System.out.println(str);
			byte[] sha = new byte[]{sha1[0], sha1[1], sha1[2]};
			
			return sha;
			
		}catch(Exception e){
			return new byte[]{};
		}
	
	}
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
