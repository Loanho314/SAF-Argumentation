package fr.lirmm.graphik.NAry;

import java.util.ArrayList;
import java.util.Iterator;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.graal.api.backward_chaining.QueryRewriter;
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

public class FindMinIncSets {

	public static ArrayList<AtomSet> findMinimalIncSubsets(DefeasibleKB kb)
			throws HomomorphismException, IteratorException, ChaseException, AtomSetException {
		RuleSet negativeRuleSet = new LinkedListRuleSet();
		RuleSet positiveRuleSet = new LinkedListRuleSet();
		InMemoryAtomSet saturatedAtoms = new LinkedListAtomSet();
		ArrayList<AtomSet> minInconSets = new ArrayList<AtomSet>();
		negativeRuleSet = kb.negativeConstraintSet;
		positiveRuleSet = kb.rules;
		kb.saturate();
		saturatedAtoms.addAll(kb.facts);
		kb.unsaturate();

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

			// Rewrite the query
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
					AtomSet set = new LinkedListAtomSet();
					set = sub.createImageOf(subQuery.getAtomSet());
					minInconSets.add(set);
				}
			}
		}
		return minInconSets;

	}

}
