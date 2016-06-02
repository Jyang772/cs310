import java.util.Scanner;

public class TermReport {
	
	public static void main(String[] args) {
		LineUsageData[] lines = new LineUsageData[500];
		
		for(int i=0; i<500; i++)
			lines[i] = new LineUsageData();
		
		//Read and process input
		Scanner in = new Scanner(System.in);
		String line, user;
		int lineNumber = 0;
		
		while(in.hasNextLine()) {
			line = in.nextLine();
			Scanner s = new Scanner(line);
			lineNumber = s.nextInt();
			user = s.next();
			lines[lineNumber-1].addObservation(user);
		}

		//Output report
		System.out.println("Line, Most Common User, Count");
		
		for(int i=1; i<=500; i++) {
			System.out.println(i + ", " + lines[i-1].findMaxUsage()); //Call toString
		}
	}
}
