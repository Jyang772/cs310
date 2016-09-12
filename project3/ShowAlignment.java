import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class ShowAlignment {

	public static int MATCH = 0;
	public static int NONMATCH = 1;
	public static int DELTA = 2;
	public static BlosumMatrix bM;
	
	public static void main(String[] args) throws IOException {
		
		String p1 = readFile(args[0]);
		String p2 = readFile(args[1]);
		char[][] trace = new char[p1.length()+p2.length()+1][2];
	
		if(args.length > 2)
			bM = new BlosumMatrix(args[2]);
	

		
		p1 = p1.substring(p1.indexOf("\n")+1).replaceAll("[\n\r]","");
		p2 = p2.substring(p2.indexOf("\n")+1).replaceAll("[\n\r]","");
			
		int editDist = getDistance(p1,p2,trace);	
		System.out.println("Cost of " + editDist);

		//Print out alignment from traceback
		for(int i=0; i<trace.length; i++) {
			if(trace[i][0] != 0 && trace[i][1] != 0) {		
				System.out.print(trace[i][0] + " " + trace[i][1] + " ");
				System.out.println(cost(trace[i][0],trace[i][1]));
			}
		}

	}

	public static int getDistance(String p1, String p2, char[][] trace) {
	
		//Build edit cost matrix	
		//Trace from right bottom corner to top left corner

		int[][] arr = new int[p1.length() + 1][p2.length()+1];
		arr[0][0] = 0;
		
		for(int i=1; i<=p1.length(); i++)
			arr[i][0] = (bM==null) ? DELTA*i : -bM.gapPenalty()*i;
		for(int i=1; i<=p2.length(); i++)
			arr[0][i] = (bM==null) ? DELTA*i : -bM.gapPenalty()*i;
		
		for(int i=1; i<=p1.length(); i++)
			for(int j=1; j<=p2.length(); j++) {
				int l=arr[i][j-1];
				int t=arr[i-1][j];
				int d=arr[i-1][j-1];
				
				if(bM==null) {
					t+=DELTA;
					l+=DELTA;
				} else {
					t+= -bM.gapPenalty();
					l+= -bM.gapPenalty();
				}
				d+=cost(p1.charAt(i-1),p2.charAt(j-1));
				arr[i][j] = Math.min(t,Math.min(l,d));
			}

		//Walk through matrix backwards
		int i=p1.length();
		int j=p2.length();	
		int t=0,l=0,d=0;
		int s=i+j;	//Index for trace
		
		if(bM != null)
			DELTA = -bM.gapPenalty();
			
		while(i>0 || j>0) {
			if(i!=0 && j!=0)
				d=arr[i-1][j-1];
			else
				d=0;
			if(i!=0)
				t=arr[i-1][j];
			else
				t= (bM==null) ? DELTA*j : j*-bM.gapPenalty();	
			if(j!=0)
				l=arr[i][j-1];
			else
				l= (bM==null) ? DELTA*i : i*-bM.gapPenalty();
			
			int a=arr[i][j];
			if((i>0 && j>0) && (a == d+cost(p1.charAt(i-1),p2.charAt(j-1)))) {
				//System.out.println("Align: " + p1.charAt(i-1) + " " + p2.charAt(j-1));
				trace[s][0] = p1.charAt(i-1);
				trace[s][1] = p2.charAt(j-1);
				s--;
				i--;
				j--;
				
			}
			else if(a == l + DELTA) {
				//System.out.println("j: " + j);
				//System.out.println("Align: " + "* " + p2.charAt(j-1));
				trace[s][0] = '*';	
				trace[s][1] = p2.charAt(j-1);
				s--;
				if(j>0)
					j--;
			}
			else if(a == t + DELTA) {
				//System.out.println("Align: " + p1.charAt(i-1) + " *");
				trace[s][0] = p1.charAt(i-1);
				trace[s][1] = '*';
				s--;
				if(i>0)
					i--;
			}
		}	
		//DEBUG	
		/*	
		for(i=0; i<trace.length; i++) {
			if(trace[i][0] != 0 && trace[i][1] != 0) {		
				System.out.print(trace[i][0] + " " + trace[i][1] + " ");
				System.out.println(cost(trace[i][0],trace[i][1]));
			}
		}
		*/
		return arr[p1.length()][p2.length()];

	}

	public static int cost(char a, char b) {
		if(bM==null) {
			if(a==b) return MATCH;
			else return NONMATCH;
		} else {
			return -bM.getValue(a,b);
		}
	}

//MISC READ FILE
        public static String readFile(String file) {
                Path path = Paths.get(file);

                String file2String = "";

                try {
                        file2String = new String(java.nio.file.Files.readAllBytes(path));
                } catch (IOException e) {
                        System.out.println("FASTA file does not exist");
                }

                return file2String;
        }


}
