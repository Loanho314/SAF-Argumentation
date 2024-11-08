package fr.lirmm.graphik.NAry.HyperGraphAtomConflict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.NAry.ArgumentationFramework.SetAttack;
import fr.lirmm.graphik.NAry.ArgumentationFramework.StructuredArgument;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;

public class Experiment3 {
	// static private String file = "C:/Users/tho310/Data
		// test/DBpedia/3Classes-1DisjointWith.dlgp";
		// static private String file = "C:/Users/tho310/Data
		// test/Ex-two-variables.dlgp";
		//static private String file = "C:/Users/tho310/Data test/Lum test.dlgp";
		static private String file = "C:/Users/tho310/Data test/test2.dlgp";
		public static ArrayList<StructuredArgument> listArguments;
		// Initialise an attack set
		public static Set<SetAttack> attackSet;

	public static void main(String[] args) throws AtomSetException, ChaseException, HomomorphismException, IOException {
			// TODO Auto-generated method stub
			DefeasibleKB kb = new DefeasibleKB(file);
			DefeasibleKB kbArgs = new DefeasibleKB(file);
			AtomSet initialFacts = new LinkedListAtomSet();
			RuleSet negativeRuleSet = new LinkedListRuleSet();
			RuleSet positiveRuleSet = new LinkedListRuleSet();
			InMemoryAtomSet saturatedAtoms = new LinkedListAtomSet();

			attackSet = new HashSet<SetAttack>();

			kbArgs.saturate();
			saturatedAtoms.addAll(kbArgs.facts);
			//System.out.println("Saturated Facts: " + saturatedAtoms);

			initialFacts.addAll(kb.facts);
			// System.out.println("Facts: " + initialFacts);
			negativeRuleSet = kb.negativeConstraintSet;
			// System.out.println("Negative rules:" + negativeRuleSet);
			positiveRuleSet = kb.rules;
			// System.out.println("Positive rules:" + positiveRuleSet);

	}

}
