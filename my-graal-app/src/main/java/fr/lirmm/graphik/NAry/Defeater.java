package fr.lirmm.graphik.NAry;

import fr.lirmm.graphik.NAry.ArgumentationFramework.Argument;

public class Defeater {
	public Argument argument;
	public int defeatType;
	
	Defeater(Argument argument, int defeatType) {
		//Defeater(Argument argument) {
		this.argument = argument;
		this.defeatType = defeatType;
	}

}
