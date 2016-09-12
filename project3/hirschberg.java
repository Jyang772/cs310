
//Pg 285 Algorithm Design
//Justin Yang

public class hirschberg {

	public static void main(String[] args) {

		//String str1 = "EEPQSDPSV";
		//String str2 = "MEEPQSDPSV";
		String str1 = args[0];
		String str2 = args[1];
		System.out.println(editDist(str1, str2));
	}

	public static int editDist(String str1, String str2) {

		int M = str1.length()+1;
		int N = str2.length()+1;
		int[][] B = new int[M][2];
	
		for(int i=0; i<M; i++) 
			B[i][0] = i*1;
		
		for(int j=1; j<N; j++) {
			B[0][1] = j*1;
			for(int i=1; i<M; i++) 
				B[i][1] = Math.min(cost(str1.charAt(i-1),str2.charAt(j-1)) + B[i-1][0],
					 Math.min(1+B[i-1][1],1+B[i][0]));

			for(int k=0; k<M; k++)
				B[k][0] = B[k][1];
		}

		return B[M-1][1];
	}
	
	public static int cost(char a, char b) {
		if(a == b) return 0;
		else return 1;
	}

}
