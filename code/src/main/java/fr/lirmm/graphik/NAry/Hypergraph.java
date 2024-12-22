package fr.lirmm.graphik.NAry;

import java.util.ArrayList;

import fr.lirmm.graphik.DEFT.gad.GADEdge;

public class Hypergraph {
	public ArrayList<String> Vertices;
	  public ArrayList<ArrayList<String>> Hyperedges;

	  public Hypergraph()
	  {
	    this.Vertices = new ArrayList();
	    this.Hyperedges = new ArrayList();
	  }

	  public Hypergraph(ArrayList<String> vertices, ArrayList<ArrayList<String>> hyperedges)
	  {
	    this.Vertices = vertices;
	    this.Hyperedges = hyperedges;
	  }

	  public ArrayList<String> N(ArrayList<String> C) {
	    ArrayList result = new ArrayList();

	    for (String v : this.Vertices) {
	      if (!C.contains(v)) {
	        for (ArrayList E : this.Hyperedges)
	        {
	          if (E.contains(v)) {
	            Integer cb = Integer.valueOf(0);
	            for (Object e1 : E) {
	            	String e = (String)e1;	            	
	              if (C.contains(e))
	                cb = Integer.valueOf(cb.intValue() + 1);
	            }
	            if (E.size() == cb.intValue() + 1) {
	              result.add(v);
	            }
	          }
	        }
	      }
	    }

	    return result;
	  }

	  public void addEdge(ArrayList<String> toAdd) {
	    this.Hyperedges.add(toAdd);
	  }

	  public void addVertex(String v) {
	    this.Vertices.add(v);
	  }

	  public ArrayList<String> getVertices() {
	    return this.Vertices;
	  }

	  public ArrayList<ArrayList<String>> getHyperedges() {
	    return this.Hyperedges;
	  }

	  public void setHyperedges(ArrayList<ArrayList<String>> hyperedges) {
	    this.Hyperedges = hyperedges;
	  }

	  public String toString() {
	    String result = "Vertices: ";
	    if (!this.Vertices.isEmpty())
	      result = result + (String)this.Vertices.get(0);
	    for (int i = 1; i < this.Vertices.size(); i++) {
	      result = result + ", " + (String)this.Vertices.get(i);
	    }
	    if (!this.Hyperedges.isEmpty())
	      result = result + "\nHyperedges: \n" + this.Hyperedges.get(0);
	    for (int i = 1; i < this.Hyperedges.size(); i++)
	      result = result + ", \n" + this.Hyperedges.get(i);
	    return result;
	  }

}
