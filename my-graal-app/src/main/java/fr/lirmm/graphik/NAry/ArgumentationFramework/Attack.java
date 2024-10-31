package fr.lirmm.graphik.NAry.ArgumentationFramework;

import java.util.ArrayList;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.RuleSet;

public class Attack {
	public ArrayList<Argument> source;
	public Argument target;
//	public RuleSet negRule;

	public Attack() {
		this.source = null;
		this.target = null;
//		this.negRule = null;
	}

	public Attack(ArrayList<Argument> source, Argument target) {
		this.source = source;
		this.target = target;
	//	this.negRule = negRule;
	}

	public String toString() {
		String result = "({";
		for (Argument a : this.source) {
			result = result + "a" + a.myID + " ";
		}
		result = result + "}, a" + this.target.myID + ")";
		return result;
	}
}
