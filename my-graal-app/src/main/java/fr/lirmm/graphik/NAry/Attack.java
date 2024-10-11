package fr.lirmm.graphik.NAry;

import java.util.ArrayList;

public class Attack {
	public ArrayList<Argument> source;
	  public Argument target;

	  public Attack()
	  {
	    this.source = null;
	    this.target = null;
	  }

	  public Attack(ArrayList<Argument> source, Argument target)
	  {
	    this.source = source;
	    this.target = target;
	  }

	  public String toString() {
	    String result = "({";
	    for (Argument a : this.source) {
	      result = result + "A" + a.myID + " ";
	    }
	    result = result + "}, A" + this.target.myID + ")";
	    return result;
	  }
}
