package fr.lirmm.graphik.NAry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.lirmm.graphik.DEFT.core.DefeasibleKB;
import fr.lirmm.graphik.DEFT.gad.Derivation;
import fr.lirmm.graphik.DEFT.gad.GADEdge;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.AtomSet;
import fr.lirmm.graphik.graal.api.core.AtomSetException;
import fr.lirmm.graphik.graal.api.core.ConjunctiveQuery;
import fr.lirmm.graphik.graal.api.core.InMemoryAtomSet;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Rule;
import fr.lirmm.graphik.graal.api.core.RuleSet;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.DefaultConjunctiveQuery;
import fr.lirmm.graphik.graal.core.atomset.LinkedListAtomSet;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.io.owl.OWL2ParserException;
import fr.lirmm.graphik.util.stream.CloseableIterator;
import fr.lirmm.graphik.util.stream.IteratorException;

import fr.lirmm.graphik.NAry.FinderMis;

public class GenerateArgsAtts {
	static private String file = "C:/Users/tho310/Data test/Lum test.dlgp";
	private static boolean haveParameters = true;
	private static String filePath = null;

	public static InMemoryAtomSet bottomAtomset = new LinkedListAtomSet();
	public static InMemoryAtomSet equalityAtomset = new LinkedListAtomSet();
	public static ConjunctiveQuery bottomQuery = new DefaultConjunctiveQuery(bottomAtomset, Collections.emptyList());

	private static ArrayList<Argument> ListArgument;

	public static void main(String[] args) throws HomomorphismException {


		if ((haveParameters) && (args.length != 0)) {
			file = args[0];
		}

		try {
			DefeasibleKB kb = new DefeasibleKB(file);
			DefeasibleKB kb1 = new DefeasibleKB(file);
			DefeasibleKB kbGenArgs = new DefeasibleKB();
			AtomSet initialFacts = new LinkedListAtomSet();
			RuleSet negativeRules = new LinkedListRuleSet();
			RuleSet positiveRules = new LinkedListRuleSet();
			RuleSet rules = new LinkedListRuleSet();
			RuleSet functionalruleset = new LinkedListRuleSet();
			RuleSet ruleset = new LinkedListRuleSet();
			initialFacts.addAll(kb.facts);
			System.out.println("Facts: " + initialFacts);

//			 Read all prioritized instances
//			CloseableIterator<Atom> iter1 = InitialFacts.iterator();
//			if (filePath != null) {
//				HashMap<String, Integer> preAtoms = readPreferenceAtoms(filePath, InitialFacts);
//				System.out.println(preAtoms);
//			}

			// read all rules
			negativeRules = kb.negativeConstraintSet;
			System.out.println("Negative rules:" + negativeRules);
			positiveRules = kb.rules;
			System.out.println("Positive rules:" + positiveRules);

			// get functional rules (equality-EGD)
			Iterator ck = positiveRules.iterator();
			while (ck.hasNext()) {
				Rule r1 = (Rule) ck.next();
				if (r1.getHead().iterator().next().getPredicate().equals(Predicate.EQUALITY)) {
					functionalruleset.add(r1);
				} else {
					rules.add(r1);
				}
			}

			ruleset.addAll(positiveRules.iterator());
			ruleset.addAll(negativeRules.iterator());

			/* Since quality rules can not be used for generating arguments,
			 * we create a KB2 that has only TGDs, no equality and negative rules
			 * to generate arguments.
			 */
			
			CloseableIterator<Atom> it = (CloseableIterator<Atom>) initialFacts.iterator();
			while (it.hasNext()) {
				Atom a = it.next();
				kbGenArgs.addAtom(a);
			}
			Iterator it2 = positiveRules.iterator();
			while (it2.hasNext()) {
				Rule r2 = (Rule) it2.next();
				if (!r2.getHead().iterator().next().getPredicate().equals(Predicate.EQUALITY)) {
					kbGenArgs.addRule(r2);
				}
			}
			
			kbGenArgs.saturate();
			InMemoryAtomSet saturatedAtoms = new LinkedListAtomSet();
			saturatedAtoms.addAll(kbGenArgs.facts);
			

			/* Generate arguments deduced from a KB */
			ListArgument = generateArgs(kbGenArgs);
			/* Check whether premises of arguments are consistent
			 * If Yes, remove it from ListArgument,
			 * otherwise, keep it.
			 */
			AtomSet Test;
			for (int i = ListArgument.size() - 1; i >= 0; i--) {
				Test = new LinkedListAtomSet();
				for (Atom p : ((Argument) ListArgument.get(i)).getPremises()) {
					Test.add(p);
				}
				kbGenArgs.strictAtomSet = Test;
				if (App1.RIncosistent(kbGenArgs)){
					ListArgument.remove(i);
				}
			}

			System.out.println("Number of args: " + ListArgument.size() + ".......List of arguments.......");
			for (Argument A : ListArgument) {
				System.out.println(A);
			}
			
			/* Generate attacks
			 * 1. For each argument A, we perform: 
			 * 1a) check whether the premises are in any minimal inconsistent subsets S.
			 * 1b) if Yes, compute attackers for the argument from the S \ {a}, where a is in the premises of A.
			 * 1c) if No, continue searching until no minimal inconsistent subset is found.
			 * 2. add (attackers, attacked) to a list of attacks.
			 */
			
			ArrayList<AtomSet> minConflicts = new ArrayList<>();				
			minConflicts = FinderMis.findMinimalIncSubsets(negativeRules, positiveRules, saturatedAtoms);
			System.out.println("Minimal inconsistent: " + minConflicts);
			ArrayList<Argument> tempArgs = ListArgument;
			ArrayList<AtomSet> tempMis = minConflicts;
			ArrayList<Argument> argG = new ArrayList<>();
			for (Argument A : tempArgs) {
				ListArgument = tempArgs \ {A}
				for(AtomSet Mis : tempMis) {
					if (check 1a - function-1) {
						AtomSet newMis = Mis.remove;
						Generate arguments for newMis;
						Add to attacks;	
						tempArgs = tempArgs \ the attacker;
					}
				}				
			}			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IteratorException e) {
			e.printStackTrace();
		} catch (AtomSetException e) {
			e.printStackTrace();
		} catch (ChaseException e) {
			e.printStackTrace();
		}
	}
	
	/* Functions to generate attacks */
	
	
	
	/*Functions to generate arguments*/	
	
	private static void AllSubset(ArrayList<ArrayList<Argument>> S, ArrayList<Argument> F)
			throws AtomSetException, ChaseException, HomomorphismException {
		ArrayList<Argument> F2 = new ArrayList<Argument>();
		F2.addAll(F);

		if (!F2.isEmpty()) {
			Argument a = (Argument) F2.get(0);

			ArrayList<ArrayList<Argument>> Temp = new ArrayList<ArrayList<Argument>>();
			for (ArrayList<Argument> s : S) {
				ArrayList<Argument> sTemp = new ArrayList<Argument>();
				sTemp.addAll(s);
				sTemp.add(a);
				Temp.add(sTemp);
			}

			for (ArrayList<Argument> s : Temp) {
				S.add(s);
			}
			F2.remove(0);
			AllSubset(S, F2);
		}
	}

	protected static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
		List<List<T>> resultLists = new ArrayList<>();
		if (lists.size() == 0) {
			resultLists.add(new ArrayList());
			return resultLists;
		}
		List firstList = (List) lists.get(0);
		List remainingLists = cartesianProduct(lists.subList(1, lists.size()));
		Iterator localIterator1 = firstList.iterator();
		while (localIterator1.hasNext()) {
			Object condition = (Object) localIterator1.next();
			Iterator localIterator2 = remainingLists.iterator();

			while (localIterator2.hasNext()) {
				List remainingList = (List) localIterator2.next();
				ArrayList resultList = new ArrayList();
				resultList.add(condition);
				resultList.addAll(remainingList);
				resultLists.add(resultList);
			}
		}
		return resultLists;
	}

	private static void recurSiveArgs(Atom a, HashMap<Atom, ArrayList<Argument>> dico, DefeasibleKB kb) {
		try {
			Iterator localIterator2;
			Iterator localIterator1 = kb.gad.getDerivations(a).iterator();
			while (localIterator1.hasNext()) {
				Derivation d = (Derivation) localIterator1.next();
				localIterator2 = d.iterator();
				while (localIterator2.hasNext()) {
					GADEdge ge = (GADEdge) localIterator2.next();

					if ((ge.getSources() == null) && (ge.getTarget().equals(a))) // this is a fact
					{
						if (dico.get(a) == null) {
							dico.put(a, new ArrayList<Argument>());
						}
						boolean contain = false;
						for (Argument p : (ArrayList<Argument>) dico.get(a)) {
							if (((p.IsPremise = Boolean.valueOf(true)).booleanValue()) && (p.head.equals(a))) {
								contain = true;
							}
						}
						if (!contain)
							((ArrayList<Argument>) dico.get(a)).add(new Argument(new ArrayList<Argument>(), a, Boolean.valueOf(true)));
					}

					else if (ge.getTarget().equals(a)) {
						ArrayList<Atom> Source = new ArrayList<Atom>();

						if (ge.getSources() != null) {
							CloseableIterator so = ge.getSources().iterator();
							while (so.hasNext()) {
								Atom nextOne = (Atom) so.next();
								Source.add(nextOne);
								recurSiveArgs(nextOne, dico, kb);
							}
						}

						if (dico.get(a) == null) {
							dico.put(a, new ArrayList<Argument>());
						}
						List T = new ArrayList();
						for (Atom m : Source) {
							T.add((List) dico.get(m));
						}
						//for (List p : cartesianProduct(T)) {

						for (Object p1 : cartesianProduct(T)) {
							List p = (List) p1;
							ArrayList copy = new ArrayList();
							copy.addAll(p);

							boolean contain = false;
							 for (Argument z : (ArrayList<Argument>)dico.get(a)) {
								if ((z.body.containsAll(copy)) && (copy.containsAll(z.body))) {
									contain = true;
								}
							}

							if (!contain) {
								((ArrayList<Argument>) dico.get(a)).add(new Argument(copy, a, Boolean.valueOf(false)));
							}
						}
					}
				}
			}
		} catch (IteratorException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<Argument> generateArgs(DefeasibleKB kb) {
		ArrayList<Argument> result = new ArrayList<>();
		HashMap<Atom, ArrayList<Argument>> dictionnary = new HashMap<>();

		for (Atom a : kb.gad.getVertices()) {
			recurSiveArgs(a, dictionnary, kb);
		}

		 for (Atom a : dictionnary.keySet()) {
			result.addAll((Collection) dictionnary.get(a));
		}

		return result;
	}
}

