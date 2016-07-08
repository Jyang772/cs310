package cs310;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * This is an example of how to use the GraphStream API to build a 
 * graph with weights and then enumerate the nodes and edges.
 * 
 * Licensed under CC-BY-SA
 * 
 * @author Joseph Paul Cohen
 *
 */
public class GraphStreamExample {
	
	public static void main(String[] args){
		
	    // make a new tree/graph and tell it to show labels
	    Graph graphStream = new SingleGraph("");
	    graphStream.addAttribute("ui.stylesheet", "graph {text-mode: normal;}");
			
	    // here we create a node and then color it 
	    {
		    Node n = graphStream.addNode("a");
		    
		    //set name that will be displayed
		    n.addAttribute("ui.label", "a");
		    
		    //set color of node
		    n.addAttribute("ui.style", "fill-color: rgb(0,100,255);");
	    }
			
	    // here we add nodes and then add a label
	    graphStream.addNode("b").addAttribute("ui.label", "b");	
	    graphStream.addNode("c").addAttribute("ui.label", "c");
	    graphStream.addNode("d").addAttribute("ui.label", "d");
	    graphStream.addNode("e").addAttribute("ui.label", "e");

	    // here the edges are added (some name, source, destination)
	    // the name of the edge isn't important, just make it unique
	    // a weight is added to the edge object
	    {
		    Edge e = graphStream.addEdge("a-b", "a", "b");
		    
		    //A weight is added that can be read from the edge later
		    e.setAttribute("weight", 2);
	    }
	    
	    // Add edge from b to everything unless already connected
    	for (Node n : graphStream.getEachNode()){
    		
    		if (n.getEdgeBetween("b") == null){
    			Edge e = graphStream.addEdge("b-" + n.getId(), "b", n.getId());
    			e.setAttribute("weight", 3);
    			e.setAttribute("ui.style", "fill-color: red;");
    		}
    	}
    	
	    // Add edge from a to nodes d and c using a loop
    	for (String n : new String[]{"d","c"}){
    		
    		Edge e = graphStream.addEdge("a-" + n, "a", n);
    		e.setAttribute("weight", 1);
    	}

	    //show the graph
	    graphStream.display(true);
	    
	    
	    //print out nodes and degrees
	    for (Node n : graphStream.getEachNode()){
	    	System.out.println("Node:" + n.getId() + ", Degree:" + n.getDegree());
	    	for (Edge e: n.getEachEdge()){
	    		System.out.println("  Connected to Node:" + e.getTargetNode().getId());
	    	}
	    }
	    
	    // loop through each edge
	    for (Edge e : graphStream.getEachEdge()){
	    	
	    	System.out.println("Edge:" + e.getId() + ", Weight:" + e.getAttribute("weight") + 
	    			", Source:" + e.getSourceNode().getId() + ", Target: " + e.getTargetNode().getId());
	    }
	}
}
