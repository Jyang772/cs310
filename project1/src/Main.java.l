import java.util.Iterator;
import java.io.EOFException;
import java.util.concurrent.TimeoutException;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.slf4j.LoggerFactory;


//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;

import java.util.HashMap; // standard HashTable library
import java.util.ArrayList; // standard flexible array library
import java.util.PriorityQueue; // standard MinPQ library
import java.util.Comparator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.algorithm.ConnectedComponents;


public class Main
{

    public static void main(String[] args) throws PcapNativeException, NotOpenException 
    {

         //open pcap
         /* PcapHandle handle = Pcaps.openOffline("dumps/echoAndEchoReply.pcap");*/
         PcapHandle handle = Pcaps.openOffline(args[0]);
         
         // establish the graph
         Graph graph = new SingleGraph("Network");         
         graph.addAttribute("ui.stylesheet", "graph {text-mode: normal;}");
         HashMap<String, String> hashmap = new HashMap<String, String>(); // key: edge; value: weight of one way
         
         // loop through all packets
        while (true) 
        {
             try 
             {
                  Packet packet = handle.getNextPacketEx();

                   // If packet has IP addresses print it
                  if (packet.contains(IpV4Packet.class))
                  {
                      String src = packet.get(IpV4Packet.class).getHeader().getSrcAddr().getHostAddress();
                      String dst = packet.get(IpV4Packet.class).getHeader().getDstAddr().getHostAddress();                       
                      
                      // check if the node already exist in the graph
                      if (graph.getNode(src) == null)
                          graph.addNode(src).addAttribute("ui.label", src);
                          
                      if (graph.getNode(dst) == null)
                          graph.addNode(dst).addAttribute("ui.label", dst);
                      

                      // update the weight, the edge is still the same way   
                      if (hashmap.containsKey(src +"-"+ dst))
                      {      
                          // "edge.getId()" is equivalent to src +"-"+ dst                           
                          Edge edge1 = graph.getEdge(src +"-"+ dst);  
                          Edge edge2 = graph.getEdge(dst +"-"+ src);
                          Edge edge = (edge1 != null) ? edge1 : edge2; // retrieve the actual one
                          
                          int packages = (edge).getAttribute("weight");

                          //System.out.println(edge.getId());
                          (edge).setAttribute("weight", packages + 1); // update the total weight
                          (edge).setAttribute("ui.label", packages); // display the weight for each edge

                      }
                      
                      // not in the hashmap, build the new edge; might be able to become two-ways edge
                      else
                      {
                          // check if this edge is bi-directions, build the new edge and update the value to be two-ways
                          if (hashmap.containsKey(dst +"-"+ src))      
                          {
                              Edge edge = graph.getEdge(dst +"-"+ src); // no loger build a new edge, avoid edgeRejectedException
                              int packages_bi = (edge).getAttribute("weight");
                              (edge).setAttribute("weight", packages_bi + 1); // update the total weight
                              (edge).setAttribute("ui.label", packages_bi);
                              hashmap.put(src +"-"+ dst, "Two-Ways direction"); // create key with the labe of bi-ways
                              hashmap.put(dst +"-"+ src, "Two-Ways direction"); // update the label of this edge
                              
                          }
                          
                          // simply establish the one-way edge
                          else
                          {
                              Edge edge = graph.addEdge(src +"-"+ dst, src, dst);                        
                              hashmap.put(src +"-"+ dst, "One-Way direction"); // create key with the label 
                              (edge).setAttribute("weight", 1); // create the weight
                          }
                          

                      }
                  }

             } 
             
             catch (TimeoutException e) 
             {
                  
             } 
             
             catch (EOFException e) 
             {
                 System.out.println("Reached end of file.");
                 break; // left the while loop
             }
        }             

        // check each edge's direction and delete the one way edges
        for (String key : hashmap.keySet())
        {
             if ((hashmap.get(key)).equals("One-Way direction")) // it is object, I am not supposed to use "==" to sttnad for equality
             {
                 Edge edge = graph.getEdge(key);
                 graph.removeEdge(edge);
             }

        }
         
        // delete the nodes with no edge
        int numNodes = 0; 
        for (Node node : graph.getEachNode())
        {
            numNodes++;
            if (node.getDegree() == 0)
            {
                graph.removeNode(node);
                //numNode--;  this statement should exist
            }
        }
         
        // print the graph drawing
        graph.display(true);
          
        // number of components
        ConnectedComponents components = new ConnectedComponents();
        components.init(graph);
        int counts = components.getConnectedComponentsCount();
        // Iterator<ExtendedNode> ite = node.getBreadthFirstIterator();


	//Why BFS?
        // Breadth First Search(BFS)
        ArrayList<Node> listVisited = new ArrayList<Node>(); // mark the visited node
        ArrayList<Node>[] list = (ArrayList<Node>[]) new ArrayList[counts]; // store the nodes of each component
        ArrayList<Edge>[] web = (ArrayList<Edge>[]) new ArrayList[counts]; // store the edges of each component
        for(int i = 0; i < list.length; i++) // neccessry initialization, otherwise null exception would occur
            list[i] = new ArrayList<Node>();
        for(int ii = 0; ii < list.length; ii++) // neccessry initialization, otherwise null exception would occur
            web[ii] = new ArrayList<Edge>();
        
        int index = 0; // index the the node in each component
        int listIndex = 0; // index of component

        for (Node node : graph.getEachNode())
        {
            // the node had been visited, check the next one
            if (listVisited.contains(node))
                continue;
            
            // the node had NOT been visited yet, do BFS  
            if (!listVisited.contains(node) && !list[listIndex].contains(node))
            {
                listVisited.add(node);
                list[listIndex].add(node);
                while(index < list[listIndex].size()) // check the nodes in each component
                {
                    
                    Node vertex = list[listIndex].get(index);
                    index++; // call the next node for next round


                    Iterator<Node> iterate = vertex.getNeighborNodeIterator();                
                    while (iterate.hasNext())
                    {                       
                        Node vertices = iterate.next();   
                        Edge path = vertex.getEdgeToward(vertices.getId());
                            
                        if (!web[listIndex].contains(path))
                            web[listIndex].add(path);
                        
                        if (!listVisited.contains(vertices))
                        {
                            listVisited.add(vertices);
                            list[listIndex].add(vertices);
                        }
                                                
                    }                
                }
                
                index = 0;

            }
            
            // count the toal number of nodes in the arraylist
            int total = 0;
            for (int j = 0; j < list.length; j++)
                total += list[j].size(); 

            // all nodes had been visited, break the loop
            if (total == numNodes)
                break;
                               
            // thre are still some nodes not visited, return to the top and continue to execute BFS    
            else if (total < numNodes)
                listIndex++; // change the component
        }


       
        // Minimum Spanning Tree (Kruskal's algorithm)        
        for (int k = 0; k < counts; k++)
        {
            WeightedQuickUnionPathCompressionUF UF = new WeightedQuickUnionPathCompressionUF(list[k].size());
            Graph MST = new SingleGraph("Minimum Spanning Tree"); //subgraph
            MST.addAttribute("ui.stylesheet", "graph {text-mode: normal;}");

            // set each node has a unique integer label for upcoming indexPQ
            for (int q = 0; q < list[k].size(); q++)
                (list[k].get(q)).addAttribute("index", q);
            
            PriorityQueue<Edge> MinPQ_E = new PriorityQueue<Edge>(new Comparator<Edge>() // store the edges pf the same component
            {
                public int compare(Edge arg0, Edge arg1) 
                {
                    int w0 = (arg0).getAttribute("weight");
                    int w1 = (arg1).getAttribute("weight");
   
                    if (w0 < w1)
                        return -1;
                    if (w0 > w1)
                        return 1;
    
                    return 0;
                }
                
            });
            
            PriorityQueue<Node> MaxPQ_N = new PriorityQueue<Node>(new Comparator<Node>() // store the nodes pf the same component
            {
                public int compare(Node arg0, Node arg1) 
                {    
                    int d0 = (arg0).getDegree();
                    int d1 = (arg1).getDegree();
   
                    if (d0 < d1)
                        return 1;
                    if (d0 > d1)
                        return -1;
    
                    return 0;
                }
            });
            PriorityQueue<Edge> MST_E = new PriorityQueue<Edge>(new Comparator<Edge>() // store the edges of MST
            {
                public int compare(Edge arg0, Edge arg1) 
                {
                    int w0 = (arg0).getAttribute("weight");
                    int w1 = (arg1).getAttribute("weight");
   
                    if (w0 < w1)
                        return -1;
                    if (w0 > w1)
                        return 1;
    
                    return 0;
                }
                
            });
            
            IndexMaxPQ<Integer> MST_Dg = new IndexMaxPQ<Integer>(list[k].size()); // store the degree of each node of MST
            PriorityQueue<Node> MST_N = new PriorityQueue<Node>(new Comparator<Node>() // store the nodes of MST
            {                
                public int compare(Node arg0, Node arg1) 
                {    
                    int d0 = (arg0).getDegree();
                    int d1 = (arg1).getDegree();
   
                    if (d0 < d1)
                        return 1;
                    if (d0 > d1)
                        return -1;
    
                    return 0;
                }
            });
                

            int weight_MST = 0, weight_Reg = 0;
            int w = 0;
            int x = -1, a = -1, b = -1; //index of indexPQ
            for (Edge edge : web[k]) // store each edge in MinPQ
                MinPQ_E.add(edge);
                
            // Kruskal's algorithm
            // As long as the edges in MinPQ are not depleted and the size of MST smaller than the size of componets, keep searching 
            while ((MinPQ_E.size() > 0) && (MST_E.size() < list[k].size()-1))
            {
                Edge e = MinPQ_E.poll(); // minimum weight of the edge
                
                // get the nodes of each side of the edge
                String direction = e.getId();
                String[] IP = new String[2];
                IP = direction.split("-");                
                Node thisSide = graph.getNode(IP[0]);
                Node oppositeSide = graph.getNode(IP[1]);
                
                // add nodes of original graph to PQ 
                if (!MaxPQ_N.contains(thisSide))
                    MaxPQ_N.add(thisSide);
 
                if (!MaxPQ_N.contains(oppositeSide))
                    MaxPQ_N.add(oppositeSide);

                // get the index
                a = (thisSide).getAttribute("index");
                b = (oppositeSide).getAttribute("index");
                
                w = (e).getAttribute("weight");
                weight_Reg += w;
                    
  
                if (!UF.connected(a, b))  // ensure the connection of two nodes does not form a cycle in developing MST
                { 
                    UF.union(a, b);  // merge a and b node in the same component
                    
                    // add the node from original graph to the developing MST
                    if (!MST_Dg.contains(a))
                    {
                        Node A = MST.addNode(IP[0]+" ");
                        MST_Dg.insert(a, A.getDegree());
                    }

                    if (!MST_Dg.contains(b))    
                    {
                        Node B = MST.addNode(IP[1]+" ");                       
                        MST_Dg.insert(b, B.getDegree());                      
                    }
                    
                    
                    MST.addEdge(IP[0]+"--"+IP[1], IP[0]+" ", IP[1]+" ");
                    MST_E.add(e);
                    w = (e).getAttribute("weight");
                    weight_MST += w;
                }
            }
            
            // remodeling the MST(subgraph of original graph) in order to retrieve latest degree in PQ)
            for (Node node_MST : MST.getEachNode())
                MST_N.add(node_MST);

            // print the results
            MST.display(true);

            System.out.println("Connected Component:" + k + ", " + list[k].size() + " Nodes in total");
            System.out.println("= Without Maximum Spanning Tree:");

            for (int m = 0; m < 3; m++)
            {
                Node ip = MaxPQ_N.poll();
                System.out.println(ip.getId() + ", Degree : " + (ip).getDegree());  
            } 


            System.out.println("= With Maximum Spanning Tree (Weight : " + weight_MST + "):");
            for (int n = 0; n < 3; n++)
            {
               Node ip_MST = MST_N.poll();
               System.out.println((ip_MST.getId()).substring(0, ip_MST.getId().length()-1) + ", Degree : " + (ip_MST).getDegree());
            }

            System.out.println();
        }
        
        
    }    
    
    /*
    private class EdgeComparator implements Comparator<Edge> 
    {
  public int compare(Edge arg0, Edge arg1) 
  {
   int w0 = (arg0).getAttribute("weight");
   int w1 = (arg1).getAttribute("weight");
   
   if (w0 < w1)
    return -1;
   if (w0 > w1)
    return 1;
    
   return 0;
  }
 }
 
 private class DegreeComparator implements Comparator<Node> 
    {
  public int compare(Node arg0, Node arg1) 
  {
   int d0 = (arg0).getDegree();
   int d1 = (arg1).getDegree();
   
   if (d0 < d1)
    return -1;
   if (d0 > d1)
    return 1;
    
   return 0;
  }
 }
 */
 
}
