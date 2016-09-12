
public class MED2
{
    static int min = Integer.MAX_VALUE;
    static String ans = "";

    static int T(String x, String y, int i, int j, int n, String s)
    {
       int sol1, sol2, sol3;



       /* ---------------------------
	  Base cases
	  --------------------------- */
       if ( i == 0 )
       {
           if ( n + j <= min )
           {
	      min = n + j;

	      if ( j > 0 )
	         ans = s + j + " ins()";
	      else
	         ans = s;
           }

	   return(j);
       }

       if ( j == 0 )
       {
           if ( n + i <= min )
           {
	      min = n + i;

	      if ( i > 0 )
	         ans = s + i + " del()";
	      else
	         ans = s;
           }

	   return(i);
       }
   	   
       if ( x.charAt(i-1) == y.charAt(j-1) )
       {	
   	  sol1 = T(x, y, i-1, j-1, n, s);

	  return(sol1);
       }	
       else
       {
          /* ------------------------
	     Divide step
	     ------------------------ */
          sol1 = T(x, y, i-1, j, n+1, s + "del(" + (i-1) + ");"); 
			// Try delete step as last
	  sol2 = T(x, y, i, j-1, n+1, s + "ins("+ y.charAt(j-1)+"," + (i-1) + ");"); 
			// Try insert step as last
	  sol3 = T(x, y, i-1, j-1, n+1, s + "sub("+ x.charAt(i-1) + "->"+ y.charAt(j-1)+"," + (i-1) + ");");  
			// Try replace step as last              

	  /* ---------------------------------------
	     Conquer: solve original problem using
	     solution from smaller problems
	     --------------------------------------- */
	  sol1 = sol1 + 1;
	  sol2 = sol2 + 1;
	  sol3 = sol3 + 1;

	
	  return Math.min(sol1,Math.min(sol2,sol3));
	/*
	  if ( sol1 <= sol2 && sol1 <= sol3 )
	     return(sol1);

	  if ( sol2 <= sol1 && sol2 <= sol3 )
	     return(sol2);

	  if ( sol3 <= sol1 && sol3 <= sol2 )
	     return(sol3);
	*/
       }

       //return(0);   // To please the stupid Java compiler
   }


   public static void main(String[] args)
   {

//     String x = "man";
//     String y = "moon";

       String x = "kitten";
       String y = "sitting";

       int r;


       r = T(x, y, x.length(), y.length(), 0, "");

       System.out.println("x = " + x);
       System.out.println("y = " + y);
       System.out.println();
       System.out.println("Min. Edit Distance = " + r);
       System.out.println();
       System.out.println("Sequence of edit operations:");
       System.out.println("   " + ans);
   }

}
