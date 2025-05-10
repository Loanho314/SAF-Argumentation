package fr.lirmm.graphik.NAry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.*;

import org.apache.commons.compress.harmony.pack200.NewAttribute;
import org.tweetyproject.graphs.HyperDirEdge;
import org.tweetyproject.graphs.HyperGraph;
import org.tweetyproject.graphs.util.GraphUtil;

import ch.qos.logback.core.pattern.parser.Node;
import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.NAry.ArgumentationFramework.StructuredArgument;
import fr.lirmm.graphik.NAry.ArgumentationFramework.ArgumentNode;
import fr.lirmm.graphik.NAry.ArgumentationFramework.ArgumentTree;
import fr.lirmm.graphik.NAry.ArgumentationFramework.Attack;
import fr.lirmm.graphik.graal.api.backward_chaining.QueryRewriter;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.backward_chaining.pure.PureRewriter;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.homomorphism.SmartHomomorphism;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

public class readFacts {
	static private String file = "C:\\Users\\tho310\\Data\\YAGO\\yago-inc5000.dlgp";
	//static private String file = "C:/Users/tho310/Data/DBpedia/Data-Ontology/wikipedia_links/output-aa.dlgp";
	// static private String file = "fileab.dlgp";
	public static ArrayList<StructuredArgument> listArguments;
	public static Set<StructuredArgument> arguments;
	//// Initialise an attack set
	public static Set<Attack> attackSet;

	// private static int count;

	public static <T> void main(String[] args)
			throws AtomSetException, ChaseException, HomomorphismException, IOException {
		// TODO Auto-generated method stub
		DefeasibleKB kb = new DefeasibleKB(file);
		AtomSet initialFacts = new LinkedListAtomSet();
		RuleSet negativeRuleSet = new LinkedListRuleSet();
		RuleSet positiveRuleSet = new LinkedListRuleSet();

		// kbArgs.saturate();
		// saturatedAtoms.addAll(kbArgs.facts);
		// System.out.println("Saturated Facts: " + saturatedAtoms);
		// kbArgs.unsaturate();
		// System.out.println("Chase" + saturatedAtoms);

		// initialFacts.addAll(kb.facts);
		System.out.println("Facts: ");
		CloseableIterator<Atom> at = kb.facts.iterator();
		while (at.hasNext()) {
			Atom a = at.next();
//			if (a == null) {
//				System.out.println("Null");
//				break;
//			}
			//System.out.println(a);
			//System.out.println(a.getTerms());

			// }

		}
		
		System.out.println("Done");

		negativeRuleSet = kb.negativeConstraintSet;
		positiveRuleSet = kb.rules;
	}
}