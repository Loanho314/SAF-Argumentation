package fr.lirmm.graphik.NAry;

import java.util.HashSet;

import org.tweetyproject.graphs.Graph;
import org.tweetyproject.graphs.HyperDirEdge;
import org.tweetyproject.graphs.HyperGraph;
import org.tweetyproject.graphs.SimpleNode;

public class experimentHyperGraph {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		 // Create a new hypergraph with nodes of type SimpleNode
        Graph<SimpleNode> g = new HyperGraph<SimpleNode>();

        // Initialize an array of SimpleNode objects
        SimpleNode[] nodes = new SimpleNode[11];
        nodes[0] = new SimpleNode("A");
        nodes[1] = new SimpleNode("B");
        nodes[2] = new SimpleNode("C");
        nodes[3] = new SimpleNode("D");
        nodes[4] = new SimpleNode("E");
        nodes[5] = new SimpleNode("F");
        nodes[6] = new SimpleNode("G");
        nodes[7] = new SimpleNode("H");
        nodes[8] = new SimpleNode("I");
        nodes[9] = new SimpleNode("J");
        nodes[10] = new SimpleNode("K");

        // Add nodes to the hypergraph
        for (SimpleNode n : nodes)
            g.add(n);

        // Create sets of nodes to form hyperedges
        HashSet<SimpleNode> a1 = new HashSet<SimpleNode>();
        a1.add(nodes[1]);
        a1.add(nodes[2]);
        a1.add(nodes[3]);

        HashSet<SimpleNode> a2 = new HashSet<SimpleNode>();
        a2.add(nodes[1]);
        a2.add(nodes[2]);
        a2.add(nodes[4]);

        HashSet<SimpleNode> a3 = new HashSet<SimpleNode>();
        a3.add(nodes[6]);
        a3.add(nodes[7]);
        a3.add(nodes[3]);
        
        HashSet<SimpleNode> a4 = new HashSet<SimpleNode>();
        a4.add(nodes[0]);

        // Add directed hyperedges to the hypergraph
        g.add(new HyperDirEdge<SimpleNode>(a1, nodes[0]));
        
        g.add(new HyperDirEdge<SimpleNode>(a2, nodes[10]));
        g.add(new HyperDirEdge<SimpleNode>(a3, nodes[4]));
        g.add(new HyperDirEdge<SimpleNode>(a4, nodes[1]));

        // Generate and print the complement of the hypergraph
        Graph<SimpleNode> tmp = g.getComplementGraph(0);
        System.out.println(tmp.toString());
    

	}

}
