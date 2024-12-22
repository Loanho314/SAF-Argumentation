package fr.lirmm.graphik.NAry;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.graal.api.core.*;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.DefaultUnionOfConjunctiveQueries;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.atomset.graph.DefaultInMemoryGraphStore;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.homomorphism.SmartHomomorphism;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;
import fr.lirmm.graphik.util.stream.IteratorException;
import fr.lirmm.graphik.graal.io.dlp.DlgpWriter;
import fr.lirmm.graphik.graal.api.backward_chaining.QueryRewriter;
import fr.lirmm.graphik.graal.backward_chaining.pure.PureRewriter;

public class FinderMis {
	// static private String file = "C:/Users/tho310/Data test/Lum test.dlgp";
	static private String file = "C:/Users/tho310/Data test/Ex-two-variables.dlgp";

	public static void main(String[] args)
			throws FileNotFoundException, IteratorException, AtomSetException, ChaseException, HomomorphismException {
		// TODO Auto-generated method stub
		DefeasibleKB kb = new DefeasibleKB(file);
		DefeasibleKB kbSat = new DefeasibleKB(file);
		AtomSet initialFacts = new LinkedListAtomSet();
		RuleSet negativeRuleSet = new LinkedListRuleSet();
		RuleSet positiveRuleSet = new LinkedListRuleSet();
		InMemoryAtomSet saturatedAtoms = new LinkedListAtomSet();
		ArrayList<AtomSet> allMinimalConflicts = new ArrayList<AtomSet>();

		kbSat.saturate();
		saturatedAtoms.addAll(kbSat.facts);
		System.out.println("Saturated Facts: " + saturatedAtoms);

		initialFacts.addAll(kb.facts);
		System.out.println("Facts: " + initialFacts);
		negativeRuleSet = kb.negativeConstraintSet;
		System.out.println("Negative rules:" + negativeRuleSet);
		positiveRuleSet = kb.rules;
		System.out.println("Positive rules:" + positiveRuleSet);

		/*
		 * Iterator pr = negativeRuleSet.iterator(); while (pr.hasNext()) { Rule r1 =
		 * (Rule) pr.next(); InMemoryAtomSet bodyRule = new LinkedListAtomSet();
		 * bodyRule.addAll(r1.getBody());
		 * 
		 * convert the head and body of negative rules to a query StringBuffer
		 * stringBuff = new StringBuffer(r1.getBody().getVariables().toString());
		 * stringBuff.delete(0, 1); stringBuff.delete(stringBuff.length() - 1,
		 * stringBuff.length()); String head = stringBuff.toString();
		 * 
		 * stringBuff = new
		 * StringBuffer(App1.AtomSetIntoArrayWithoutArity(bodyRule).toString());
		 * stringBuff.delete(0, 1); stringBuff.delete(stringBuff.length() - 1,
		 * stringBuff.length()); String body = stringBuff.toString(); String stQuery =
		 * "?(" + head + ") :- " + body + "."; ConjunctiveQuery query =
		 * DlgpParser.parseQuery(stQuery); System.out.println(query);
		 * 
		 * /* Rewrite the query QueryRewriter rewriter = new PureRewriter();
		 * CloseableIteratorWithoutException it = rewriter.execute(query,
		 * positiveRuleSet);
		 * 
		 * // UnionOfConjunctiveQueries ucq = new //
		 * DefaultUnionOfConjunctiveQueries(query.getAnswerVariables(), it);
		 * 
		 * 
		 * get answers for the re-writed query, which are minimal inconsistent subsets
		 * 
		 * while (it.hasNext()) { ConjunctiveQuery subQuery = (ConjunctiveQuery)
		 * it.next(); System.out.println(subQuery); CloseableIterator<Substitution>
		 * substitutions = SmartHomomorphism.instance().execute(subQuery,
		 * saturatedAtoms); while (substitutions.hasNext()) { Substitution sub =
		 * substitutions.next(); AtomSet atoms = new LinkedListAtomSet(); atoms =
		 * sub.createImageOf(subQuery.getAtomSet()); allMinimalConflicts.add(atoms); } }
		 * 
		 * }
		 */
		allMinimalConflicts = findMinimalIncSubsets(negativeRuleSet, positiveRuleSet, saturatedAtoms);
		System.out.println("All inconsistent subsets: " + allMinimalConflicts);

	}

	public static ArrayList<AtomSet> findMinimalIncSubsets(RuleSet negativeRuleSet, RuleSet positiveRuleSet,
			InMemoryAtomSet saturatedAtoms) throws HomomorphismException, IteratorException {
		ArrayList<AtomSet> allMis = new ArrayList<AtomSet>();

		/*
		 * Find minimal inconsistent sets Idea: 1. Treat negative rules Q as conjunctive
		 * queries CQ 2. Rewrite the conjunctive query CQ - union of the conjunctive
		 * queries UCQ 3. Compute answers for the UCQ 4. The answers are minimal
		 * inconsistent sets
		 */

		Iterator pr = negativeRuleSet.iterator();
		while (pr.hasNext()) {
			Rule r1 = (Rule) pr.next();
			InMemoryAtomSet bodyRule = new LinkedListAtomSet();
			bodyRule.addAll(r1.getBody());

			/* convert the head and body of negative rules to a query */
			StringBuffer stringBuff = new StringBuffer(r1.getBody().getVariables().toString());
			stringBuff.delete(0, 1);
			stringBuff.delete(stringBuff.length() - 1, stringBuff.length());
			String head = stringBuff.toString();

			stringBuff = new StringBuffer(App1.AtomSetIntoArrayWithoutArity(bodyRule).toString());
			stringBuff.delete(0, 1);
			stringBuff.delete(stringBuff.length() - 1, stringBuff.length());
			String body = stringBuff.toString();
			String stQuery = "?(" + head + ") :- " + body + ".";
			ConjunctiveQuery query = DlgpParser.parseQuery(stQuery);

			/* Rewrite the query */
			QueryRewriter rewriter = new PureRewriter();
			CloseableIteratorWithoutException it = rewriter.execute(query, positiveRuleSet);

			// UnionOfConjunctiveQueries ucq = new
			// DefaultUnionOfConjunctiveQueries(query.getAnswerVariables(), it);

			/*
			 * get answers for the re-writed query, which are minimal inconsistent subsets
			 */
			while (it.hasNext()) {
				ConjunctiveQuery subQuery = (ConjunctiveQuery) it.next();
				CloseableIterator<Substitution> substitutions = SmartHomomorphism.instance().execute(subQuery,
						saturatedAtoms);
				while (substitutions.hasNext()) {
					Substitution sub = substitutions.next();
					AtomSet atoms = new LinkedListAtomSet();
					atoms = sub.createImageOf(subQuery.getAtomSet());
					allMis.add(atoms);
				}
			}

		}
		return allMis;

	}

}
