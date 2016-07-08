import java.io.EOFException;
import java.util.concurrent.TimeoutException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Collections;
//Import Graphstream packages
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;

public class Main {
	//DFS	
	static boolean cycleFound;
	static HashSet <Node> visited = new HashSet<Node>();	
	static HashSet <Edge> visitedEdge = new HashSet<Edge>();
	//Finding components
	static HashSet<Node> toAdd = new HashSet<Node>();	
	static HashSet<Node> subGraphed = new HashSet<Node>();	
	public static void main(String[] args) throws PcapNativeException, NotOpenException {
	
		Graph graph = new SingleGraph("");
		PcapHandle handle = Pcaps.openOffline(args[0]);//"dumps/outside.tcpdump-small"); //args[0]);

	int node = 0;
	//int counter = 0;	
	//while(counter++ <= 10000) 
	while(true) {
		try {
		Packet packet = handle.getNextPacketEx();
		if(packet.contains(IpV4Packet.class)) {
			//Get address for src
			//Get address for dst
			String src = packet.get(IpV4Packet.class).getHeader().getSrcAddr().getHostAddress();
			String dst = packet.get(IpV4Packet.class).getHeader().getDstAddr().getHostAddress();


			//Check if src exists in graph, if not create it	
			//Check if dst exists in graph, if not create it
			//Check if edge exists in graph, if not create it, set weight to 0
			//(src-dst, dst-src)
			if(graph.getNode(src) == null) {
				node++;
				graph.addNode(src).addAttribute("ui.label",src);	
			}
			if(graph.getNode(dst) == null){
				node++;
				graph.addNode(dst).addAttribute("ui.label",dst);
			}
		

			//Check if there exists an edge (src-dst)	
			//If not, check if there exists an edge (dst-src)
			//If there is, mark edge attribute as two-way (!isOneWay)
			//If there isn't, add Edge (src-dst) and set attr. to isOneWay
			Edge e = graph.getEdge(src + "-" + dst);
			if(e == null) {
				e = graph.getEdge(dst + "-" + src);
				if(e != null)
					e.addAttribute("isOneWay",0);
			}
			if(e == null) {	//Checking for (dst-src)
					//If doesn't exist, add weight=0, attr=isOneWay
				e = graph.addEdge(src + "-" + dst,src,dst);
				e.addAttribute("weight",0);
				e.addAttribute("isOneWay",1);
			}
		
		//	double weight = e.getNumber("weight");
		//	weight++;
		//	e.setAttribute("weight",weight);	
			e.addAttribute("weight",e.getNumber("weight")+1);
		}
		} catch (TimeoutException e) {
		} catch (EOFException e) {
	  		System.out.println("Reached end of file.");
	  		break;
		}
	}
					
			//Iterate through all edges looking for ones that have attribute oneWay==true
			//Removing an edge in a for-loop causes the size to decrease, therefore never reaching the end
		int size = graph.getEdgeCount();
		while(size-- >0) {
			Edge e = graph.getEdge(size);	
			double weight = e.getNumber("weight");
			e.addAttribute("ui.label", weight);
			if(e.getNumber("isOneWay") == 1) {
				graph.removeEdge(e);
			}
		}

		size = graph.getNodeCount();	
		while(size-- > 0) {	
			Node n = graph.getNode(size);
			if(n.getDegree() == 0) 
				graph.removeNode(n);
		}
	
	//Part 2
	//Implement BFS or DFS to find connected components (subgraphs)	
	//Loop until the number of edges in subgraphs equals the total number of edges in the graph	
	//This means that all edges have been added to a component
	//Add all components(subgraphs) to an ArrayList of graphs	

	//toAdd keeps track of nodes that are found to be in a component
	//subGraphed keeps track of # of nodes that have been traversed/visited
	
	int i=0; //For keeping track of new graphs
	ArrayList<Graph> graphList = new ArrayList<Graph>();
	System.out.println("Node Count: " + graph.getNodeCount());	
	while(subGraphed.size() != graph.getNodeCount()) {
		Node start = null;
		
		for(Node n: graph.getEachNode()) {
			if(!subGraphed.contains(n)) {
				start = n;
				break;
			}
		}
		
		checkConnection(start);
	
		Graph nGraph = new SingleGraph(Integer.toString(i)); //Keep track of index
		//Node x;
		for(Node n : toAdd){  //Add nodes in toAdd to new Graph
			nGraph.addNode(n.getId()).addAttribute("ui.label",n.getId());
			//x.addAttribute("ui.label",n.getId());
		}
		
		for(Node n : toAdd)
			for(Edge e : n.getEachLeavingEdge()) {
				if(nGraph.getEdge(e.getId()) == null) { //Check if nGraph has an edge e
					Edge nEdge = nGraph.addEdge(e.getId(),e.getSourceNode().getId(),e.getTargetNode().getId());
					nEdge.addAttribute("weight",(double)e.getNumber("weight"));
				}
			}
		
		//Add new graph to list of graphs
		graphList.add(nGraph);
		toAdd.clear();
		i++;
	} //while()	

	
	//	System.out.println("Components: " + graphList.size());
	//Sort graphList by nodecount
	Collections.sort(graphList,new Comparator<Graph>(){
		public int compare(Graph a, Graph b){
			return b.getNodeCount() - a.getNodeCount();
		}});
	int component=0;
	for(Graph g: graphList) {

		System.out.println("CC: " + component + ", " + g.getNodeCount() + " Nodes Total");
		System.out.println("-Without Maximum Spanning Tree");
				
		//Use a PriorityQueue to get nodes that have the most connected edges
		PriorityQueue<Node> maxEdges = new PriorityQueue<Node>(10,new Comparator<Node>(){
			public int compare(Node a, Node b) {
				if(b.getDegree() > a.getDegree())
					return 1;
				if(b.getDegree() == a.getDegree())
					return 0;
				return -1;	
			}
		});
		maxEdges.addAll(g.getNodeSet());

		for(int j=0; j<3;j++) {
			Node n = maxEdges.poll();
			System.out.println("\t" + n.getId() + "," + n.getDegree());
		}

		int weight = 0;
		Graph t = maxTree(g);
		for(Edge e: t.getEdgeSet())
			weight += e.getNumber("weight");
		System.out.print("-With Maximum Spanning Tree");
		System.out.println(" (Weight:" + weight + ")");
		
		
		 maxEdges = new PriorityQueue<Node>(10,new Comparator<Node>(){
                        public int compare(Node a, Node b) {
               		return b.getDegree() - a.getDegree();                                 
			}
		});	

		maxEdges.addAll(t.getNodeSet());
		t.display(true);	
	
		//Display MST within original graph
		for(Edge e : graph.getEachEdge()) {
			Edge e_t = t.getEdge(e.getId());
			if(e_t != null) {
				e.addAttribute("ui.style", "size: 5px; fill-color: red;");
				//e.addAttribute("ui.style", "fill-color: red;");	
			}
		}


		for(int j=0; j<3;j++) {
                        Node n = maxEdges.poll();
                        System.out.println("\t" + n.getId() + "," + n.getDegree());
                }
	
	component++;
	}	

	graph.display(true);

} //Main

public static Graph maxTree(Graph g) {

	//Kruskal's Algorithm
	//Sort edges in descending order (equivalent to negating for min tree)
	//Add first edge to set T
	//Add another edge to T, check if it forms a cycle
	//If it doesn't, add edge to tree
	//If it does, add next edge
	//And so on
	Graph result = new SingleGraph("");
	
	PriorityQueue<Edge> PQ = new PriorityQueue<Edge>(10, new Comparator<Edge>() {
		public int compare (Edge a, Edge b) {
			if(b.getNumber("weight") > a.getNumber("weight"))
				return 1;
			if(b.getNumber("weight") == a.getNumber("weight"))
				return 0;
			return -1;
		}
	});

	PQ.addAll(g.getEdgeSet());
	//Add all edges to PQ	
	    
	cycleFound = false;	
	
        while (PQ.size() > 0){
		Edge curEdge = PQ.poll();
	       
		Node n1 = curEdge.getTargetNode();
		Node n2 = curEdge.getSourceNode();
		
		if(result.getNode(n1.getId()) == null)
			result.addNode(n1.getId()).addAttribute("ui.label",n1.getId());
		if(result.getNode(n2.getId()) == null)
			result.addNode(n2.getId()).addAttribute("ui.label",n2.getId());
	   		//Do DFS for cycle
		Edge added = result.addEdge(curEdge.getId(),curEdge.getSourceNode().getId(),curEdge.getTargetNode().getId());
		        added.addAttribute("ui.style", "fill-color: blue;");

			added.addAttribute("ui.label",curEdge.getNumber("weight"));
			added.addAttribute("weight", curEdge.getNumber("weight"));

	
		//Check if adding edge forms cycle		
		dfs(result,result.getNode(0)); 
		
		if(cycleFound){	
			//System.out.println("Node 0: " + result.getNode(0).getId());
			//System.out.println("CYCLE FOUND");	
			//System.out.println("Edge: " + added.getId());
			result.removeEdge(added);
			cycleFound = false;
		}
		visited.clear();
		visitedEdge.clear();
       }
		
		return result;

/*
	while(PQ.size() > 0){ 
		Edge e = PQ.poll();
		if(result.getNode(e.getSourceNode().getId()) == null || result.getNode(e.getTargetNode().getId()) == null) {
				if(result.getNode(e.getSourceNode().getId()) == null)
					result.addNode(e.getSourceNode().getId());
				if(result.getNode(e.getTargetNode().getId()) == null)
					result.addNode(e.getTargetNode().getId());
				
				result.addEdge(e.getId(),e.getTargetNode().getId(),e.getSourceNode().getId());
		}
	}
	

	return result;
*/

}
	
public static void dfs(Graph g, Node n) {

	if(visited.contains(n)) {
		//System.out.println("FOUND");
		cycleFound = true;
		return;	
	}
	visited.add(n);

	Node n1, n2;	
	for(Edge w : n.getEachEdge()) {
		
		if(visitedEdge.contains(w))
			continue;
		else
			visitedEdge.add(w);

		n1 = w.getSourceNode();
		n2 = w.getTargetNode();
		
		if(n == n1) {
				dfs(g,n2);
		}
		if(n == n2) {
				dfs(g,n1);
		}
	}
	
	return;
}

public static void checkConnection(Node n) {
	if(toAdd.contains(n))
		return;

	//use toAdd to keep track of nodes in a component	
	//use subGraphed to keep track of # of nodes traversed
	toAdd.add(n);
	subGraphed.add(n);
	
	//Because only one edge is added between nodes, even if there is bidirectional comm.	
	//Must check src and target nodes of E
	//If N is the src node of edge E, check the targetNode
	//If N is the tgt node of edge E, check the srcNode
	//n = A	
	// A->B , B->C
	// (A->B).src = A == n, check(tgt (B)), 
	//(A->B) return, (B->C).src == B, check(tgt(C))
	for(Edge e : n.getEachEdge()) {
		
		if(n == e.getSourceNode()) 
			checkConnection(e.getTargetNode());
		else if(n == e.getTargetNode())
			checkConnection(e.getSourceNode());
	}	
				
} //checkConnection

}



/*
	for(Node n:graph.getEachNode())
			System.out.println(n.getId());	

		System.out.println("---------------------------");
	{	size = graph.getNodeCount();
		int i=0;
		while(i < size) {
			System.out.print("i: " + i + " ");
			Node n = graph.getNode(i);
			System.out.println(n.getId());
			if(n.getDegree() == 0) {
				graph.removeNode(n);
				size--;
			}
		i++;
		}
		System.out.println("i: 28 " + graph.getNode(1));
		System.out.println("i: 29 " + graph.getNode(0));
*/
