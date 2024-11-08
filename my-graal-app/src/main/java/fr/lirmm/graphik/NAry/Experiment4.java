package fr.lirmm.graphik.NAry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.NAry.ArgumentationFramework.SetAttack;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;

public class Experiment4 {
	static private String file = "C:/Users/tho310/Data test/DBPedia/C0C1C2a0-5935.dlgp";

	public static void main(String[] args) throws AtomSetException, ChaseException, HomomorphismException, IOException{
		// count a number of predicates in KBs
		
		
		DefeasibleKB kb = new DefeasibleKB(file);
		DefeasibleKB kbArgs = new DefeasibleKB(file);
		AtomSet initialFacts = new LinkedListAtomSet();
		RuleSet negativeRuleSet = new LinkedListRuleSet();
		RuleSet positiveRuleSet = new LinkedListRuleSet();
		InMemoryAtomSet saturatedAtoms = new LinkedListAtomSet();
		
		Set<Predicate> predicates = new HashSet<Predicate>();



		// System.out.println("Chase" + saturatedAtoms);

		initialFacts.addAll(kb.facts);
		// System.out.println("Facts: " + initialFacts);
		negativeRuleSet = kb.negativeConstraintSet;
		// System.out.println("Negative rules:" + negativeRuleSet);
		positiveRuleSet = kb.rules;
		for(Rule r : positiveRuleSet) {
			predicates.addAll(r.getBody().getPredicates());
			predicates.addAll(r.getHead().getPredicates());
		}
		
		System.out.println("Number of predicates: " + predicates.size());
		System.out.println(predicates);
		// System.out.println("Positive rules:" + positiveRuleSet);

	}

}
