import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

//Import Graphstream packages
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


public class Visualize {

        public static BlosumMatrix M;
        public static int MATCH = 0;
        public static int NONMATCH = 1;
        public static int DELTA = 1;

	public static void main(String[] args) {

	try {
		if(args.length > 1)
			M = new BlosumMatrix(args[1]);

		//Get name of protein to label Node
		ArrayList<String> proteinList = readDirectoryFiles(args[0]);
		ArrayList<String> proteinNames = new ArrayList<String>();
	
		//Same logic as in ProteinCompare	
		for(int i=0; i<proteinList.size(); i++) {

			//Get protein string
			String temp = proteinList.get(i);
			String protein = temp;
			int endI = protein.indexOf("\n");
			protein = protein.substring(endI+1);
			protein = protein.replaceAll("[\n\r]","");
			proteinList.set(i,protein);
			
			//Get protein name inside []
			protein = temp; //endIndex exclusive
			protein = protein.substring(protein.indexOf("[")+1,protein.indexOf("]"));
			proteinNames.add(protein);
		}

		//Threshold == mean minimum edit distance
		int totalDist=0;
		int count = 0;	
		//Create new graph
		//Single graph, there can only be one edge between nodes
		//Therefore, no duplicate species!!
		Graph g = new SingleGraph("");
		
		for(int i=0; i<proteinList.size(); i++) {
			for(int j=i; j<proteinList.size(); j++) {
				int dist = getDistance(proteinList.get(i), proteinList.get(j));
				totalDist += dist;
			
				//If node doesn't exist in graph, then add it
				if(g.getNode(proteinNames.get(i)) == null)
					g.addNode(proteinNames.get(i)).addAttribute("ui.label",proteinNames.get(i));
				
				if(g.getNode(proteinNames.get(j)) == null)
					g.addNode(proteinNames.get(j)).addAttribute("ui.label",proteinNames.get(j));
				
				//Add edge between proteins i and j
				//label & weight == edit distance
				//System.out.println("i: " + proteinNames.get(i));
				//System.out.println("j: " + proteinNames.get(j));
		
				Edge e = g.getEdge(proteinNames.get(i) + "-" + proteinNames.get(j));	
				if(e == null) {
					//System.out.println("Adding edge: " + proteinNames.get(i) + "-" + proteinNames.get(j));
					e = g.addEdge(proteinNames.get(i) + "-" + proteinNames.get(j), proteinNames.get(i), proteinNames.get(j));
					e.addAttribute("weight",dist);
					e.addAttribute("ui.label",dist);
				}	
			if(i!=j)
				count++;
			}
		}
		//mean minimum edit dist	
		int threshold = totalDist/count;
		System.out.println("Threshold: " + threshold);
	
		int size = g.getEdgeCount();
		while(size-- >0) {
			Edge e = g.getEdge(size);
			if(e.getNumber("weight") > threshold)
				g.removeEdge(e);
		}	
				
		g.display(true);		
	} catch (IOException e) {
		System.out.println("BLOSUM file does not exist");
	}

	}

        public static int getDistance(String a, String b) {

                        //System.out.println("No cost matrix supplied");

                        int[][] arr = new int[a.length() + 1][b.length() + 1];

                        arr[0][0] = 0;

                        //Initial delta values  top row, left column
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
                                                //t+= -M.getValue(b.charAt(j-1),'*');
                                                //l+= -M.getValue(a.charAt(i-1),'*');
                                                t+= -M.gapPenalty();
                                                l+= -M.gapPenalty();
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
