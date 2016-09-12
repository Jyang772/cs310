import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class BlosumMatrix {

	private BufferedReader reader;
	private String amino;
	private int[][] matrix;

	public BlosumMatrix(String file) throws IOException{
		reader = new BufferedReader(new FileReader(file));
		String currLine = reader.readLine();
		
		//Skip lines starting with #
		while(currLine.charAt(0) == '#'){
			currLine = reader.readLine();
		}
	
		//currLine now holds A R N D C Q E ...	
		//Match and replace all whitespace
		amino = currLine.replaceAll("\\s","");
		matrix = new int[amino.length()][amino.length()];

		//Read in matrix row by row
		//Read in another line --> values now
		int i = 0;
		while ((currLine = reader.readLine()) != null) { //'+' one or more of the previous character
			String[] values = currLine.trim().replaceAll(" +", " ").split(" "); // remove all the additional whitespaces
			for (int j = 1; j < values.length; j++) {
				matrix[i][j-1] = Integer.parseInt(values[j]); 
				//System.out.println(Integer.parseInt(values[i]));
				// parse Strings to Integers
			}
			i++;
		}
		//System.out.println(getValue('A','R'));
	}

	public int getValue(char a, char b) {
		int index1 = amino.indexOf(a);
		int index2 = amino.indexOf(b);
		return matrix[index1][index2];
	}
	
	public int gapPenalty() {
		return matrix[matrix.length-1][0];
	}
}
