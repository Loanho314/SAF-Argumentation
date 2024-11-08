package fr.lirmm.graphik.NAry;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.NAry.ArgumentationFramework.StructuredArgument;
import fr.lirmm.graphik.NAry.ArgumentationFramework.SetAttack;
import fr.lirmm.graphik.graal.api.backward_chaining.QueryRewriter;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
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
import fr.lirmm.graphik.util.stream.CloseableIteratorWithoutException;
import fr.lirmm.graphik.util.stream.IteratorException;

public class Experiment2 {

	static private String file = "C:/Users/tho310/Data test/test2.dlgp";

	public static void main(String[] args)
			throws IteratorException, ChaseException, AtomSetException, FileNotFoundException, HomomorphismException {
		DefeasibleKB kb = new DefeasibleKB(file);
		DefeasibleKB kbArgs = new DefeasibleKB(file);
		AtomSet initialFacts = new LinkedListAtomSet();
		RuleSet negativeRuleSet = new LinkedListRuleSet();
		RuleSet positiveRuleSet = new LinkedListRuleSet();
		InMemoryAtomSet saturatedAtoms = new LinkedListAtomSet();


		// attackSet = new HashSet<Attack>();

		kbArgs.saturate();
		saturatedAtoms.addAll(kbArgs.facts);
		System.out.println("Saturated Facts: " + saturatedAtoms);
		// kbArgs.unsaturate();

		initialFacts.addAll(kb.facts);
		//System.out.println("Facts: " + initialFacts);
		negativeRuleSet = kb.negativeConstraintSet;
		//System.out.println("Negative rules:" + negativeRuleSet);
		positiveRuleSet = kb.rules;
		//System.out.println("Positive rules:" + positiveRuleSet);

		// listArguments = App1.generateArgs(kbArgs);
		// kb.unsaturate();

		// Check whether premises of arguments are consistent
		// If Yes, remove it from ListArgument, otherwise, keep it.

		/*
		 * AtomSet Test; for (int i = listArguments.size() - 1; i >= 0; i--) { Test =
		 * new LinkedListAtomSet(); for (Atom p : ((Argument)
		 * listArguments.get(i)).getPremises()) { Test.add(p); } kbArgs.strictAtomSet =
		 * Test; if (App1.RIncosistent(kbArgs)) { listArguments.remove(i); } }
		 * 
		 * System.out.println(".......List of arguments......."); for (Argument A :
		 * listArguments) { System.out.println(A); }
		 * 
		 * System.out.println("Number of args: " + listArguments.size());
		 */

		/*
		 * String queryString = "?(X,Y) :-" + " <professor>(X)," + " teach(X,Y).";
		 * 
		 * //String queryString = "?(X) :-" + " professor(X).";
		 * 
		 * //String queryString = "? :- postdoc(ann).";
		 * 
		 * ArrayList<AtomSet> result = new ArrayList<AtomSet>(); //new
		 * LinkedListAtomSet(); ConjunctiveQuery query =
		 * DlgpParser.parseQuery(queryString); System.out.println(query);
		 * 
		 * if (query.getAtomSet().getVariables().isEmpty()) { InMemoryAtomSet atoms =
		 * query.getAtomSet(); //result.add(atoms.iterator().next()); result.add(atoms);
		 * } else {
		 * 
		 * // rewrite a CQ // QueryRewriter rewriter = new PureRewriter(); //
		 * CloseableIteratorWithoutException it = rewriter.execute(query,
		 * positiveRuleSet);
		 * 
		 * 
		 * // while (it.hasNext()) { // ConjunctiveQuery subQ = (ConjunctiveQuery)
		 * it.next(); // System.out.println("sub query: " + subQ);
		 * CloseableIterator<Substitution> substitutions =
		 * SmartHomomorphism.instance().execute(query, saturatedAtoms); while
		 * (substitutions.hasNext()) { Substitution sub = substitutions.next();
		 * InMemoryAtomSet atoms = sub.createImageOf(query.getAtomSet());
		 * System.out.println("atoms: " + atoms); //result.addAll(atoms);
		 * result.add(atoms); } } // }
		 * 
		 * System.out.println("result: " + result);
		 */

		// get answers for the rewrite CQ query
		/*
		 * while (it.hasNext()) { ConjunctiveQuery subQuery = (ConjunctiveQuery)
		 * it.next();
		 */

		/*
		 * CloseableIterator<Substitution> substitutions =
		 * SmartHomomorphism.instance().execute(query, saturatedAtoms); if
		 * (substitutions.hasNext()) { while (substitutions.hasNext()) { Substitution
		 * sub = substitutions.next(); AtomSet ans = new LinkedListAtomSet(); ans =
		 * sub.createImageOf(query.getAtomSet()); result.add(ans.iterator().next());
		 * System.out.println(result); } } else System.out.println("No answers");
		 */

		ArrayList<AtomSet> allMinimalConflicts = new ArrayList<AtomSet>();

		Iterator pr = negativeRuleSet.iterator();
		
		while (pr.hasNext()) {
			Rule r1 = (Rule) pr.next();
			System.out.println("Neg rules:" + r1);
			InMemoryAtomSet bodyRule = new LinkedListAtomSet();
			bodyRule.addAll(r1.getBody());

			// convert the head and body of negative rules to a query
			StringBuffer stringBuff = new StringBuffer(r1.getBody().getVariables().toString());
			stringBuff.delete(0, 1);
			stringBuff.delete(stringBuff.length() - 1, stringBuff.length());
			String head = stringBuff.toString();

			stringBuff = new StringBuffer(App1.AtomSetIntoArrayWithoutArity(bodyRule).toString());
			stringBuff.delete(0, 1);
			stringBuff.delete(stringBuff.length() - 1, stringBuff.length());
			String body = stringBuff.toString();

			// Extract the prefix

			String[] parts = body.split(", ");

			// Extract the prefix from the first part
			int lastSlashIndex = parts[0].lastIndexOf('/');
			String prefix = parts[0].substring(0, lastSlashIndex + 1);
			String replaceBody = body.replaceAll("(https?://[^\\(]+)", "<$1>");

			String stQuery;
			if (prefix.isEmpty()) {
				// there is no prefix
				stQuery = "?(" + head + ") :- " + body + ".";

			} else {
				// the correct syntax of a given query: ?(Variables) : -
				// <prefix/Predicate>(Variables).
				stQuery = "?(" + head + ") :- " + replaceBody + ".";

			}
			
			ConjunctiveQuery query = DlgpParser.parseQuery(stQuery);
			System.out.println("Query: " + query);
		

			// Rewrite the query
			QueryRewriter rewriter = new PureRewriter();
			CloseableIteratorWithoutException it = rewriter.execute(query, positiveRuleSet);

			// UnionOfConjunctiveQueries ucq = new
			// DefaultUnionOfConjunctiveQueries(query.getAnswerVariables(), it);

			// get answers for the re-writed query, which are minimal inconsistent subsets
			
			while (it.hasNext()) {
				ConjunctiveQuery subQuery = (ConjunctiveQuery) it.next();
				System.out.println("subQuery: " + subQuery);
				CloseableIterator<Substitution> substitutions = SmartHomomorphism.instance().execute(subQuery,
						saturatedAtoms);
				while (substitutions.hasNext()) {
					Substitution sub = substitutions.next();
					AtomSet set = new LinkedListAtomSet();
					set = sub.createImageOf(subQuery.getAtomSet());
					System.out.println("Set: " + set);
					allMinimalConflicts.add(set);
				}
			}
		}
		
	

		System.out.println("All inconsistent subsets: " + allMinimalConflicts);

	}

}
