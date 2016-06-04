public class O
{
    static int x = 1;

    public static void main(String args[]){
        O o = new O();
        o.call(3);
    }

    public void call(int x){
        O o = new O();
        this.x = 5;
        O s = new O();   
	s.x = 10;
	System.out.println("Output: " + x);
        System.out.println("Output: " + O.x);
        System.out.println("Output: " + o.x);
    }
}
