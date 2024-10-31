package fr.lirmm.graphik.NAry.ArgumentationFramework;

import java.util.ArrayList;

import org.tweetyproject.graphs.Node;

import fr.lirmm.graphik.graal.api.core.Atom;

/*public class ArgumentNode extends Argument implements Node {

	// A global counter for IDs. 
	private static int counter = 0;

	// The id of this node.
	private int nodeId;


	/**
	 * Creates a new argument node with the given support and claim (a unique ID is
	 * generated)
	 * 
	 * @param support a set of formulas.
	 * @param claim   a formula.
	 */

/*public ArgumentNode(ArrayList<Argument> body, Atom head, Boolean isPremises) {
	super(body, head, isPremises);
	ArgumentNode.counter++;
	this.nodeId = ArgumentNode.counter;
}*/

/**
 * Creates a new deductive argument node from the given argument (a unique ID is
 * generated)
 * 
 * @param arg an argument.
 */
/*
 * public ArgumentNode(Argument argument) { // Pass the argument details to the
 * superclass super(argument.body, argument.head, argument.IsPremise);
 * ArgumentNode.counter++; this.nodeId = ArgumentNode.counter;
 * 
 * }
 * 
 * @Override public int hashCode() { final int prime = 31; int result =
 * super.hashCode(); result = prime * result + nodeId; return result; }
 * 
 * @Override public boolean equals(Object obj) { if (this == obj) return true;
 * if (!super.equals(obj)) return false; if (getClass() != obj.getClass())
 * return false; ArgumentNode other = (ArgumentNode) obj; if (nodeId !=
 * other.nodeId) return false; return true; }
 * 
 * }
 */

public class ArgumentNode extends Argument implements Node {

	private Argument argument; // The argument associated with this node
	private ArrayList<ArgumentNode> children; // List of child nodes
	private static int idCounter = 0; // Static counter for auto-incremented node IDs
	private int nodeID; // Unique ID for each ArgumentNode

	// Constructor that accepts an Argument
	public ArgumentNode(Argument argument) {
		this.argument = argument; // Assign the provided argument to the node
		this.children = new ArrayList<>(); // Initialize the children list
		this.nodeID = idCounter++; // Assign a unique ID and increment the counter
	}

	// Method to add a child node
	public void addChild(ArgumentNode childNode) {
		children.add(childNode); // Add the child node to the list
	}
	

    // Getter for the unique node ID
    public int getNodeID() {
        return nodeID;
    }

	// Getters for the argument properties
	public ArrayList<Argument> getBody() {
		return argument.body; // Get the body of the argument
	}

	public ArrayList<Atom> getPremises() {
		ArrayList<Atom> result = new ArrayList();
		if (this.argument.body.isEmpty()) {
			result.add(this.argument.head);
			return result;
		}

		for (Argument p : this.argument.body) {
			result.addAll(p.getPremises());
		}
		return result;
	}

	public Atom getHead() {
		return argument.head; // Get the head of the argument
	}

	public Boolean isPremise() {
		return argument.IsPremise; // Get the premise status
	}

	public int getID() {
		return argument.myID; // Get the unique ID of the argument
	}

	public ArrayList<ArgumentNode> getChildren() {
		return children; // Return the list of child nodes
	}

	@Override
	public String toString() {

		String result = "a" + getID() + " : [";
		for (Argument a : this.argument.body) {
			result = result + "a" + a.myID + " ";
		}
		result = result + "] -> " + getHead();
		return result;
	}
}
