package fr.lirmm.graphik.NAry.ArgumentationFramework;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Rule;

import java.util.ArrayList;

public class Argument {

	public ArrayList<Argument> body;
	public Atom head;
	public Boolean IsPremise;
	private static int numberArgs = 0;
	public final int myID;

	public Argument() {
		this.body = null;
		this.head = null;
		this.IsPremise = Boolean.valueOf(true);
		this.myID = numberArgs;
		numberArgs += 1;
	}

	public Argument(ArrayList<Argument> body, Atom head, Boolean isPremise) {
		this.body = body;
		this.head = head;
		this.IsPremise = isPremise;

		this.myID = numberArgs;
		numberArgs += 1;
	}

	public ArrayList<Atom> getPremises() {
		ArrayList result = new ArrayList();
		if (this.body.isEmpty()) {
			result.add(this.head);
			return result;
		}

		for (Argument p : this.body) {
			result.addAll(p.getPremises());
		}
		return result;
	}

	public String toString() {
		String result = "a" + this.myID + " : [";
		for (Argument a : this.body) {
			result = result + "a" + a.myID + " ";
		}
		result = result + "] -> " + this.head;
		return result;
	}

	/*
	 * public String toString() { String result = "A" + this.myID + ": {" +
	 * this.body; //for (Argument a : this.body) // { // result = result + a + " ";
	 * // } result = result + "} -> " + "{" + this.head + "}"; return result; }
	 */

}
