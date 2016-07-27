package com.sshtools.daemon.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SloppyAuthenticationProvider extends NativeAuthenticationProvider {

	Log log = LogFactory.getLog(SloppyAuthenticationProvider.class);
	
	public SloppyAuthenticationProvider() {
		log.error("DummyAuthenticationProvider is in use. This is only for testing.");
	}
	
	@Override
	public boolean changePassword(String username, String oldpassword,
			String newpassword) {
		return false;
	}

	@Override
	public String getHomeDirectory(String username) throws IOException {
		return "/home/" + username;
	}

	@Override
	public void logoffUser() throws IOException {
		
	}

	@Override
	public boolean logonUser(String username, String password)
			throws PasswordChangeException, IOException {
		System.out.println(username + "-" + password);

		return editDistance(password,file2String("secret")) <= 2 ? true : false;	
	}

	@Override
	public boolean logonUser(String username) throws IOException {
		System.out.println(username);
		return false;
	}
	
	public static String file2String(String filename){
		
		try {
			File file = new File(filename);
		    FileInputStream fis = new FileInputStream(file);
		    byte[] data = new byte[(int)file.length()];
		    fis.read(data);
		    fis.close();
			System.out.println(Arrays.toString(data));
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
			byte[] sha = new byte[]{sha1[0], sha1[1], sha1[2]};
			
			return sha;
			
		}catch(Exception e){
			return new byte[]{};
		}
	
	}
	
	public static int editDistance(String str1, String str2) {
	
	int m = str1.length();
	int n = str2.length(); //File.length == length of string including null
	
	//(M+1)x(N+1) matrix	
	//+1 for column that holds distance if moving only along one string

        int M[][] = new int[m+1][n+1];
      
        for (int i=0; i<=m; i++)
        {
            for (int j=0; j<=n; j++)
            {
                // If first string is empty, insert rest of characters from 2nd string
                if (i==0)
                    M[i][j] = j;  // Min. operations = j
     
                // If second string is empty, insert rest of characters from 1st string
                else if (j==0)
                    M[i][j] = i; // Min. operations = i
      
                // If last characters are same, ignore last char
		//Same as subproblem of min edit for M[i-1][j-1]
                else if (str1.charAt(i-1) == str2.charAt(j-1))
                    M[i][j] = M[i-1][j-1];
      
                // If last character are different, consider all
                // possibilities and find minimum
                else   //Min(Insert,Min(Remove,Replace))
                    M[i][j] = 1 + Math.min(M[i][j-1], Math.min(M[i-1][j],M[i-1][j-1])); 
            }
        }
 
	//System.out.println(M[m][n]); 
        return M[m][n];	
	}

}

