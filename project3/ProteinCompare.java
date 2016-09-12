import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;


/* References
 
Reading files from directory: 
http://stackoverflow.com/questions/1844688/read-all-files-in-a-folder

*/

public class ProteinCompare {

	public static BlosumMatrix M;
	public static int MATCH = 0;
	public static int NONMATCH = 1;
	public static int DELTA = 1;

	public static void main(String[] args) {

	try {
		if(args.length > 1)
			M = new BlosumMatrix(args[1]);

		//Debug
		/*System.out.println(getDistance("EEPQSDPSV","MEEPQSDPSV"));
		System.out.println(getDistance("EEPQSDPSV","MTAMEESQSD"));
		System.out.println(getDistance("TAMEESQSD","MEEPQSDPSV"));
		System.out.println(getDistance("TAMEESQSD","MTAMEESQSD"));
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

                        //Get protein name inside []
                        protein = temp; //endIndex exclusive
                        protein = protein.substring(protein.indexOf("[")+1,protein.indexOf("]"));
                        proteinNames.add(protein);
		}

		for(int i=0; i<proteinList.size(); i++)
			for(int j=i; j<proteinList.size(); j++) {
				int distance = getDistance(proteinList.get(i), proteinList.get(j));
				//System.out.println("Protein" + (i+1) + "\t" + "Protein" + (j+1) + "\t" + distance);
				System.out.println(proteinNames.get(i) + "\t" + proteinNames.get(j) + "\t" + distance);
			}
	
	} catch (IOException e) {
		System.out.println("BLOSUM file does not exist");
		} 
	}

	public static int getDistance(String a, String b) {
	
			//System.out.println("No cost matrix supplied");
	
			int[][] arr = new int[a.length() + 1][b.length() + 1];
	
			arr[0][0] = 0;	
			
			//Initial delta values	top row, left column
				for(int i=1; i<=a.length(); i++)
					arr[i][0] = (M==null) ? DELTA*i : -M.gapPenalty()*i;
				for(int i=1; i<=b.length(); i++)
					arr[0][i] = (M==null) ? DELTA*i : -M.gapPenalty()*i;
							
			for(int i=1; i<=a.length(); i++)
				for(int j=1; j<=b.length(); j++) {
					int l = arr[i][j-1];
					int t = arr[i-1][j];
					int d = arr[i-1][j-1];
			
					if(M == null) {	
						t+=DELTA;
						l+=DELTA;
					} else { //Aligning characters to gap penalty
						t+= -M.getValue(b.charAt(j-1),'*');
						l+= -M.getValue(a.charAt(i-1),'*');
						//t+= -M.gapPenalty();
						//l+= -M.gapPenalty();
					}
				
					d+=cost(a.charAt(i-1),b.charAt(j-1));
					
					arr[i][j] = Math.min(t,Math.min(l,d));
				}
			return arr[a.length()][b.length()];	
	}

        public static int cost(char a, char b) {
                if(M == null) {
                        if(a == b) return MATCH;
                        else return NONMATCH;
                }
                else {
                        return -M.getValue(a,b);
		}
        }



//MISC READING FILES
	public static ArrayList<String> readDirectoryFiles(String dir) {
		
		File d = new File(dir);
		ArrayList<String> list = new ArrayList<String>();
		
		File[] ls = d.listFiles();
		//Arrays.sort(ls);
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
	
		//System.out.println(Arrays.deepToString(ls));
	
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
