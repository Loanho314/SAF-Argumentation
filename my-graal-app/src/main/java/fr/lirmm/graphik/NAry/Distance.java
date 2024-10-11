package fr.lirmm.graphik.NAry;

public class Distance {
	public Argument source;
	public Argument target;
	public int dist;
	
	public Distance(Argument source, Argument target, int dist) {
		this.source = source;
		this.target = target;
		this.dist = dist;
	}
	 public String toString() {
		    String result = "(" + this.source + ", " + this.target + ", " + this.dist + ")";
		    return result;
		  }
}
