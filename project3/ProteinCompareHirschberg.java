import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;


/* References
 
Reading files from directory: 
http://stackoverflow.com/questions/1844688/read-all-files-in-a-folder

*/

public class ProteinCompareHirschberg {

	public static BlosumMatrix bM;
	public static int MATCH = 0;
	public static int NONMATCH = 1;
	public static int DELTA = 1;

	public static void main(String[] args) {

	try {
		if(args.length > 1)
			bM = new BlosumMatrix(args[1]);

		//Debug
		/*System.out.println(editDist("EEPQSDPSV","MEEPQSDPSV"));
		System.out.println(editDist("EEPQSDPSV","MTAMEESQSD"));
		System.out.println(editDist("TAMEESQSD","MEEPQSDPSV"));
		System.out.println(editDist("TAMEESQSD","MTAMEESQSD"));
		*/	

		//Returns an ArrayList of Proteins	
		//Must remove first line (FASTA header)
		//Store proteins into same ArrayList
		//Run Needleman-Wunsch on each protein w/ each other O(n(n-1)/2)
		ArrayList<String> proteinList = readDirectoryFiles(args[0]);
		ArrayList<String> proteinNames = new ArrayList<String>();
		for(int i=0; i<proteinList.size(); i++) {
			String protein = proteinList.get(i);
			String temp = protein;
			int endI = protein.indexOf("\n");
			protein = protein.substring(endI + 1);
			//Remove linebreaks
			protein = protein.replaceAll("[\n\r]","");
			//System.out.println(protein);
			//System.out.println();
			proteinList.set(i,protein);
			
			protein = temp;
			protein = protein.substring(protein.indexOf("[")+1,protein.indexOf("]"));
			proteinNames.add(protein);
		}

		for(int i=0; i<proteinList.size(); i++)
			for(int j=i; j<proteinList.size(); j++) {
				int distance = editDist(proteinList.get(i), proteinList.get(j));
				//System.out.println("Protein" + (i+1) + "\t" + "Protein" + (j+1) + "\t" + distance);
				System.out.println(proteinNames.get(i) + "\t" + proteinNames.get(j) + "\t" + distance);
			}
	
	} catch (IOException e) {
		System.out.println("BLOSUM file does not exist");
		}
	}


	public static int editDist(String str1, String str2) {
	
		int M = str1.length()+1;
		int N = str2.length()+1;
		int[][] B = new int[M][2];

		if(bM != null)
			DELTA = -bM.gapPenalty();
		
		for(int i=0; i<M; i++) 
			B[i][0] = DELTA*i;
		
		for(int j=1; j<N; j++) {
			B[0][1] = DELTA*j;

			for(int i=1; i<M; i++) 
				B[i][1] = Math.min(cost(str1.charAt(i-1),str2.charAt(j-1)) + B[i-1][0],
					 Math.min(DELTA+B[i-1][1],DELTA+B[i][0]));

			for(int k=0; k<M; k++)
				B[k][0] = B[k][1];
		}

		return B[M-1][1];
	}
	
	public static int cost(char a, char b) {
		if(bM == null) {
			if(a == b) return MATCH;
			else return NONMATCH;
		}
		else
			return -bM.getValue(a,b);
	}
	

//MISC READING FILES
	public static ArrayList<String> readDirectoryFiles(String dir) {
		
		File d = new File(dir);
		ArrayList<String> list = new ArrayList<String>();
		
		File[] ls = d.listFiles();
		Arrays.sort(ls,new Comparator<File>() {
			public int compare(File f1, File f2) {
				try {
					int i1 = extractNumber(f1.getName());
					int i2 = extractNumber(f2.getName());
					return i1-i2;
				} catch(NumberFormatException e) {
					throw new AssertionError(e);
				}
			}
			private int extractNumber(String name) {
					int i = 0;
					try {
					    String subS = new String("Protein");
					    int s = name.indexOf(subS)+subS.length();//+1;
					    int e = name.lastIndexOf('.');
					    String number = name.substring(s, e);
					    i = Integer.parseInt(number);
					} catch(Exception e) {
					    i = 0; // if filename does not match the format
						   // then default to 0
					}
					return i;
				    }
		});

		if(ls != null) {
			for(File child : ls)
				list.add(readFile(child.getPath()));
		}
		else
			System.out.println("Directory Missing!");
	
		return list;
	}	
		
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
